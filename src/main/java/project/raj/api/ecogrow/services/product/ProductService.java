package project.raj.api.ecogrow.services.product;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.raj.api.ecogrow.dto.ImageDto;
import project.raj.api.ecogrow.dto.ProductDto;
import project.raj.api.ecogrow.exceptions.AlreadyExistsException;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.models.Image;
import project.raj.api.ecogrow.repository.ImageRepository;
import project.raj.api.ecogrow.requests.ProductCreateRequest;
import project.raj.api.ecogrow.models.Category;
import project.raj.api.ecogrow.models.Product;
import project.raj.api.ecogrow.repository.CategoryRepository;
import project.raj.api.ecogrow.repository.ProductRepository;
import project.raj.api.ecogrow.requests.ProductUpdateRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(ProductCreateRequest request) {
        // If a category is found on db then set it that category
        // creates a new category and then save it.
        if (productExist(request.getName(), request.getBrand())) {
            throw new AlreadyExistsException("Product already exist");
        }
        Category category = Optional.ofNullable(
                categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));

    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
           existingProduct.setId(request.getId());
           existingProduct.setName(request.getName());
           existingProduct.setDescription(request.getDescription());
           existingProduct.setBrand(request.getBrand());
           existingProduct.setPrice(request.getPrice());
           existingProduct.setInventory(request.getInventory());
           Category category = categoryRepository.findByName(request.getCategory().getName());
           existingProduct.setCategory(category);
           return existingProduct;
    }

    private Product createProduct(ProductCreateRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
             category
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {throw new ResourceNotFoundException("Product not found!");});
    }

    @Override
    public Product updateProductById(Long id, ProductUpdateRequest request) {
        return productRepository.findById(id)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public List<Product> getAllProductByName(String productName) {
        return productRepository.findByNameIgnoreCase(productName);
    }

    @Override
    public List<Product> getAllProductByCategory(String categoryName) {
        return productRepository.findByCategoryNameIgnoreCase(categoryName);
    }

    @Override
    public List<Product> getAllProductByBrand(String brandName) {
        return productRepository.findByBrandIgnoreCase(brandName);
    }

    @Override
    public List<Product> getAllProductByCategoryNameAndBrand(String categoryName, String brandName) {
        return productRepository.findByCategoryNameAndBrandIgnoreCase(categoryName, brandName);
    }

    @Override
    public List<Product> getAllProductByNameAndBrand(String name, String brandName) {
        return productRepository.findByNameAndBrandIgnoreCase(name,brandName);
    }

    @Override
    public Long countProductByNameAndBrand(String name, String brandName) {
        return (long) productRepository.findByNameAndBrandIgnoreCase(name, brandName).size();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper
                        .map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

    @Override
    public List<ProductDto> convertToDtos(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    public boolean productExist(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }
}
