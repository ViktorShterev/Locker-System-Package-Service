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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageServiceImplTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private KafkaTemplate<String, PackageEventDTO> kafkaTemplate;

    @InjectMocks
    private PackageServiceImpl packageService;

    @Test
    void createPackage_ShouldReturnTrue_WhenPackageIsCreatedSuccessfully() {
        CreatePackageDTO dto = new CreatePackageDTO();
        dto.setCourierEmail("courier@example.com");
        dto.setRecipientEmail("recipient@example.com");
        dto.setLockerSize("MEDIUM");
        dto.setLockerLocation("Location A");
        dto.setDescription("Description");
        dto.setLockerUnitId(123L);

        Package savedPackage = new Package(dto.getCourierEmail(),
                dto.getRecipientEmail(),
                PackageSizeEnum.valueOf(dto.getLockerSize()),
                dto.getLockerLocation(),
                dto.getDescription(),
                AccessCodeGeneratorUtil.generateAlphanumericCode(),
                dto.getLockerUnitId(),
                PackageStatusEnum.DELIVERED,
                LocalDateTime.now());

        when(packageRepository.save(any())).thenReturn(savedPackage);

        boolean result = packageService.createPackage(dto);

        assertTrue(result);
        verify(packageRepository).save(any());
        verify(kafkaTemplate).send(eq("package-placed"), any(PackageEventDTO.class));
    }

    @Test
    void createPackage_ShouldReturnFalse_WhenExceptionThrown() {

        CreatePackageDTO dto = new CreatePackageDTO();
        dto.setCourierEmail("courier@example.com");
        dto.setRecipientEmail("recipient@example.com");
        dto.setLockerSize("INVALID_SIZE");

        boolean result = packageService.createPackage(dto);

        assertFalse(result);
        verify(packageRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void receivePackage_ShouldReturnTrue_WhenAccessCodeAndEmailMatch() {
        ReceivePackageDTO dto = new ReceivePackageDTO();
        dto.setRecipientEmail("recipient@example.com");
        dto.setAccessCode("ABC123");

        Package existingPackage = new Package();
        existingPackage.setAccessCode(dto.getAccessCode());
        existingPackage.setRecipientEmail(dto.getRecipientEmail());
        existingPackage.setLockerUnitId(123L);
        existingPackage.setLocation("Locker A");
        existingPackage.setId(10L);

        when(packageRepository.findByAccessCode(dto.getAccessCode())).thenReturn(Optional.of(existingPackage));

        boolean result = packageService.receivePackage(dto);

        assertTrue(result);

        verify(packageRepository).save(argThat(pkg ->
                pkg.getPackageStatus() == PackageStatusEnum.PICKED_UP &&
                        pkg.getPickedUpAt() != null
        ));

        verify(kafkaTemplate).send(eq("package-received"), any(PackageEventDTO.class));
    }

    @Test
    void receivePackage_ShouldReturnFalse_WhenPackageNotFound() {
        ReceivePackageDTO dto = new ReceivePackageDTO();
        dto.setRecipientEmail("recipient@example.com");
        dto.setAccessCode("ABC123");

        when(packageRepository.findByAccessCode(dto.getAccessCode())).thenReturn(Optional.empty());

        assertFalse(packageService.receivePackage(dto));
    }

    @Test
    void getAllPackages_ShouldReturnListOfPackageDTOs() {
        Package pkg = new Package();
        pkg.setAccessCode("ABC123");
        pkg.setRecipientEmail("recipient@example.com");
        pkg.setLockerUnitId(123L);
        pkg.setLocation("Locker A");
        pkg.setId(10L);
        pkg.setPackageStatus(PackageStatusEnum.DELIVERED);
        pkg.setPackageSize(PackageSizeEnum.MEDIUM);
        pkg.setPickedUpAt(null);

        when(packageRepository.findTop10ByRecipientEmailOrderByCreatedAtDesc("recipient@example.com"))
                .thenReturn(List.of(pkg));

        List<PackageDTO> result = packageService.getAllPackages("recipient@example.com");

        assertEquals(1, result.size());
        assertEquals("-", result.get(0).getPickedUpAt());
    }
}