package my.projects.lockersystempackagemicroservice.service;

import my.projects.lockersystempackagemicroservice.dto.CreatePackageDTO;
import my.projects.lockersystempackagemicroservice.dto.PackageDTO;
import my.projects.lockersystempackagemicroservice.dto.ReceivePackageDTO;

import java.util.List;

public interface PackageService {

    boolean createPackage(CreatePackageDTO createPackageDTO);

    boolean receivePackage(ReceivePackageDTO receivePackageDTO);

    List<PackageDTO> getAllPackages(String email);
}
