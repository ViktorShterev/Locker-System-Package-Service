package my.projects.lockersystempackagemicroservice.web;

import my.projects.lockersystempackagemicroservice.dto.CreatePackageDTO;
import my.projects.lockersystempackagemicroservice.dto.ReceivePackageDTO;
import my.projects.lockersystempackagemicroservice.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("http://localhost:8082/packages")
public class PackageRestController {

    private final PackageService packageService;

    public PackageRestController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping("/create-package")
    public ResponseEntity<Boolean> createPackage(@RequestBody CreatePackageDTO createPackageDTO) {
        boolean isSuccessful = this.packageService.createPackage(createPackageDTO);
        return ResponseEntity.ok(isSuccessful);
    }

    @PostMapping("/receive-package")
    public ResponseEntity<Boolean> receivePackage(@RequestBody ReceivePackageDTO receivePackageDTO) {
        boolean isSuccessful = this.packageService.receivePackage(receivePackageDTO);
        return ResponseEntity.ok(isSuccessful);
    }
}
