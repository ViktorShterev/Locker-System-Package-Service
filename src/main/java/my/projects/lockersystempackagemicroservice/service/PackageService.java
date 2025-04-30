package my.projects.lockersystempackagemicroservice.service;

import my.projects.lockersystempackagemicroservice.dto.CreatePackageDTO;
import my.projects.lockersystempackagemicroservice.dto.ReceivePackageDTO;

public interface PackageService {

    boolean createPackage(CreatePackageDTO createPackageDTO);

    boolean receivePackage(ReceivePackageDTO receivePackageDTO);
}
