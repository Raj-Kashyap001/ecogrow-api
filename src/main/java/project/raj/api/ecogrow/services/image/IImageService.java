package project.raj.api.ecogrow.services.image;

import org.springframework.web.multipart.MultipartFile;
import project.raj.api.ecogrow.dto.ImageDto;
import project.raj.api.ecogrow.models.Image;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
