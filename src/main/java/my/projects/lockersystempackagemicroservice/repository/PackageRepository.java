package my.projects.lockersystempackagemicroservice.repository;

import my.projects.lockersystempackagemicroservice.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

}
