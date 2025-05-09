package my.projects.lockersystempackagemicroservice.service.impl;

import my.projects.lockersystempackagemicroservice.dto.CreatePackageDTO;
import my.projects.lockersystempackagemicroservice.dto.PackageDTO;
import my.projects.lockersystempackagemicroservice.dto.ReceivePackageDTO;
import my.projects.lockersystempackagemicroservice.dto.kafka.PackageEventDTO;
import my.projects.lockersystempackagemicroservice.entity.Package;
import my.projects.lockersystempackagemicroservice.enums.PackageSizeEnum;
import my.projects.lockersystempackagemicroservice.enums.PackageStatusEnum;
import my.projects.lockersystempackagemicroservice.repository.PackageRepository;
import my.projects.lockersystempackagemicroservice.service.PackageService;
import my.projects.lockersystempackagemicroservice.util.AccessCodeGeneratorUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final KafkaTemplate<String, PackageEventDTO> kafkaTemplate;

    public PackageServiceImpl(PackageRepository packageRepository, KafkaTemplate<String, PackageEventDTO> kafkaTemplate) {
        this.packageRepository = packageRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean createPackage(CreatePackageDTO createPackageDTO) {

        try {
            Package package1 = new Package(
                    createPackageDTO.getCourierEmail(),
                    createPackageDTO.getRecipientEmail(),
                    PackageSizeEnum.valueOf(createPackageDTO.getLockerSize()),
                    createPackageDTO.getLockerLocation(),
                    createPackageDTO.getDescription(),
                    AccessCodeGeneratorUtil.generateAlphanumericCode(),
                    createPackageDTO.getLockerUnitId(),
                    PackageStatusEnum.DELIVERED,
                    LocalDateTime.now()
            );

            Package saved = this.packageRepository.save(package1);

            PackageEventDTO event = new PackageEventDTO();
            event.setLockerUnitId(createPackageDTO.getLockerUnitId());
            event.setLockerLocation(createPackageDTO.getLockerLocation());
            event.setAccessCode(saved.getAccessCode());
            event.setPackageId(saved.getId());
            event.setRecipientEmail(createPackageDTO.getRecipientEmail());

            this.kafkaTemplate.send("package-placed", event);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean receivePackage(ReceivePackageDTO receivePackageDTO) {

        Optional<Package> optionalPackage = this.packageRepository.findByAccessCode(receivePackageDTO.getAccessCode());

        if (optionalPackage.isPresent() && optionalPackage.get().getRecipientEmail().equals(receivePackageDTO.getRecipientEmail())) {
            Package package1 = optionalPackage.get();

            package1.setPackageStatus(PackageStatusEnum.PICKED_UP);
            package1.setPickedUpAt(LocalDateTime.now());

            this.packageRepository.save(package1);

            PackageEventDTO event = new PackageEventDTO();
            event.setLockerUnitId(package1.getLockerUnitId());
            event.setLockerLocation(package1.getLocation());
            event.setAccessCode(receivePackageDTO.getAccessCode());
            event.setPackageId(package1.getId());
            event.setRecipientEmail(receivePackageDTO.getRecipientEmail());

            this.kafkaTemplate.send("package-received", event);

            return true;
        }

        return false;
    }

    @Override
    public List<PackageDTO> getAllPackages(String email) {

        List<Package> allPackages = this.packageRepository.findTop10ByRecipientEmailOrderByCreatedAtDesc(email);
        List<PackageDTO> packageDTOList = new ArrayList<>();

        for (Package aPackage : allPackages) {

            PackageDTO packageDTO = new PackageDTO(aPackage.getLocation(),
                    aPackage.getDescription(),
                    aPackage.getAccessCode(),
                    aPackage.getLockerUnitId(),
                    aPackage.getPackageSize().name(),
                    aPackage.getPackageStatus().name());

            if (aPackage.getPickedUpAt() == null) {
                packageDTO.setPickedUpAt("-");
            } else {
                packageDTO.setPickedUpAt(aPackage.getPickedUpAt().toString());
            }

            packageDTOList.add(packageDTO);
        }

        return packageDTOList;
    }
}
