package project.raj.api.ecogrow.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.raj.api.ecogrow.exceptions.AlreadyExistsException;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.models.Category;
import project.raj.api.ecogrow.response.ApiResponse;
import project.raj.api.ecogrow.services.category.ICategoryService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategory() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(
                    new ApiResponse("Found " + categories.size(), categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error: ", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory (@RequestBody Category category) {
        try {
            Category responseCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Success!",responseCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT)
                    .body((new ApiResponse(e.getMessage(),null)));
        }

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Found: ", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Category Not Found", null));
        }
    }


    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found: ", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Category Not Found", null));
        }
    }

    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Found: ", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Category Not Found", null));
        }
    }

    @PutMapping("/{categoryId}/update")
    public ResponseEntity<ApiResponse> updateCategoryById(@PathVariable Long categoryId, @RequestBody Category category) {
        try {
            categoryService.updateCategory(category, categoryId);
            return ResponseEntity.ok(new ApiResponse("Update Success: ", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Category Not Found", null));
        }
    }
}
