package project.raj.api.ecogrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.raj.api.ecogrow.models.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryNameIgnoreCase(String categoryName);

    List<Product> findByBrandIgnoreCase(String brandName);

    List<Product> findByCategoryNameAndBrandIgnoreCase(String categoryName, String brandName);

    List<Product> findByNameAndBrandIgnoreCase(String name, String brandName);

    List<Product> findByNameIgnoreCase(String name);

    boolean existsByNameAndBrand(String name, String brandName);
}
