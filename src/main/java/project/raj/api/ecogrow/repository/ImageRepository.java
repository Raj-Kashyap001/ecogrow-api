package project.raj.api.ecogrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.raj.api.ecogrow.models.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image getImageById(Long id);

    List<Image> findByProductId(Long id);
}
