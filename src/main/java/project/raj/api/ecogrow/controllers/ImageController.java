package project.raj.api.ecogrow.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import project.raj.api.ecogrow.dto.ImageDto;
import project.raj.api.ecogrow.models.Image;
import project.raj.api.ecogrow.response.ApiResponse;
import project.raj.api.ecogrow.services.image.IImageService;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {
    private  final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files,productId);
            return ResponseEntity
                    .ok(new ApiResponse
                    ("Upload success",imageDtos));
        } catch (Exception e) {
           return ResponseEntity
                   .status(INTERNAL_SERVER_ERROR)
                   .body(new ApiResponse("Upload failed", e.getMessage()));
        }

    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource
                (image.getImageBlob().getBytes(1,(int) image.getImageBlob().length()));
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" +image.getFileName()+"\"")
                .body(resource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> uploadImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
               imageService.updateImage(file, imageId);
               return ResponseEntity.ok(new ApiResponse("Update Success", null));
            }
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(
                            e.getMessage(),
                            null)) ;
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse
                        ("Update Failed!",
                            INTERNAL_SERVER_ERROR));
    }
    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Delete Success", null));
            }
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(
                            e.getMessage(),
                            null)) ;
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse
                        ("Delete Failed!",
                                INTERNAL_SERVER_ERROR));
    }
}
