package project.raj.api.ecogrow.services.product;

import project.raj.api.ecogrow.dto.ProductDto;
import project.raj.api.ecogrow.models.Product;
import project.raj.api.ecogrow.requests.ProductCreateRequest;
import project.raj.api.ecogrow.requests.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(ProductCreateRequest productCreateRequest);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    void deleteProductById(Long id);

    Product updateProductById(Long id, ProductUpdateRequest request);

    //    Filter Queries
    List<Product> getAllProductByName(String productName);

    List<Product> getAllProductByCategory(String categoryName);

    List<Product> getAllProductByBrand(String brandName);

    List<Product> getAllProductByCategoryNameAndBrand(String categoryName, String brandName);

    List<Product> getAllProductByNameAndBrand(String name, String brandName);

    Long countProductByNameAndBrand(String name, String brandName);

    ProductDto convertToDto(Product product);

    List<ProductDto> convertToDtos(List<Product> products);
}
