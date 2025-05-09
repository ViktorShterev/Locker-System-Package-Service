package my.projects.lockersystempackagemicroservice.web;

import my.projects.lockersystempackagemicroservice.dto.CreatePackageDTO;
import my.projects.lockersystempackagemicroservice.dto.PackageDTO;
import my.projects.lockersystempackagemicroservice.dto.ReceivePackageDTO;
import my.projects.lockersystempackagemicroservice.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/packages")
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

    @GetMapping("/view-packages")
    public ResponseEntity<List<PackageDTO>> viewPackages(@RequestParam String email) {
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
        List<PackageDTO> allPackages = this.packageService.getAllPackages(decodedEmail);
        return ResponseEntity.ok(allPackages);
    }
}
