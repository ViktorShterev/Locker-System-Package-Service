package my.projects.lockersystempackagemicroservice.repository;

import my.projects.lockersystempackagemicroservice.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    Optional<Package> findByAccessCode(String accessCode);
}
