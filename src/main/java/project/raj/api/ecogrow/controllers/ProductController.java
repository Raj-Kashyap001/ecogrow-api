package project.raj.api.ecogrow.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.raj.api.ecogrow.dto.ProductDto;
import project.raj.api.ecogrow.exceptions.AlreadyExistsException;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.models.Product;
import project.raj.api.ecogrow.requests.ProductCreateRequest;
import project.raj.api.ecogrow.requests.ProductUpdateRequest;
import project.raj.api.ecogrow.response.ApiResponse;
import project.raj.api.ecogrow.services.product.IProductService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/product")
@RequiredArgsConstructor
public class ProductController {
   private final IProductService productService;
    private final ModelMapper modelMapper;

    @GetMapping("/products/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
       try {
           List<Product> products = productService.getAllProducts();
           List<ProductDto> productDtos = productService.convertToDtos(products);
           return ResponseEntity.ok(new ApiResponse("success", productDtos));
       } catch (Exception e) {
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: ", e.getMessage()));
       }
   }

   @GetMapping("/{productId}")
   public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
       try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("success", productDto));
       } catch (ResourceNotFoundException e) {
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
       }
   }

    // Get all the product with query parameters
    @GetMapping("/products/by")
    public ResponseEntity<ApiResponse> getProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) String categoryName) {

        List<Product> products;

        if (productName != null && brandName != null) {
            products = productService.getAllProductByNameAndBrand(productName, brandName);
        } else if (categoryName != null && brandName != null) {
            products = productService.getAllProductByCategoryNameAndBrand(categoryName, brandName);
        } else if (productName != null) {
            products = productService.getAllProductByName(productName);
        } else if (brandName != null) {
            products = productService.getAllProductByBrand(brandName);
        } else if (categoryName != null) {
            products = productService.getAllProductByCategory(categoryName);
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse("Missing or invalid query parameters", null));
        }

        List<ProductDto> productDtos = productService.convertToDtos(products);
        return ResponseEntity.ok(new ApiResponse("success", productDtos));
    }

    // Get the product count by name and brandName.
    @GetMapping("products/count")
    public ResponseEntity<ApiResponse> getProductCountByNameAndBrand(@RequestParam String productName, @RequestParam String brandName) {
        try {
            var productCount = productService.countProductByNameAndBrand(productName, brandName);
            return ResponseEntity.ok(new ApiResponse("success", productCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: ", e.getMessage()));
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductCreateRequest request) {
      try {
          Product product = productService.addProduct(request);
          ProductDto productDto = modelMapper.map(product, ProductDto.class);
          return ResponseEntity.ok(new ApiResponse("product added successfully!", productDto));
      } catch (AlreadyExistsException e) {
          return ResponseEntity.status(CONFLICT)
                  .body(new ApiResponse(e.getMessage(), null));
      }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/{productId}/update")
  public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
      try {
          Product product = productService.getProductById(productId);
          ProductDto productDto = productService.convertToDto(product);
          return ResponseEntity.ok(new ApiResponse("product updated successfully!", productDto));
      } catch (ResourceNotFoundException e) {
          return ResponseEntity.status(NOT_FOUND)
                  .body(new ApiResponse(e.getMessage(), null));
      }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/{productId}/delete")
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
      try {
          productService.deleteProductById(productId);
          return ResponseEntity.ok(new ApiResponse("product deleted successfully!", null));
      } catch (ResourceNotFoundException e) {
          return ResponseEntity.status(NOT_FOUND)
                  .body(new ApiResponse(e.getMessage(), null));
      }
  }

}

