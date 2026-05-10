package com.example.jpaexercise.Controller;

import com.example.jpaexercise.Api.ApiResponse;
import com.example.jpaexercise.Model.Category;
import com.example.jpaexercise.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.status(200).body(categoryService.getAllCategory());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody @Valid Category category, Errors errors) {
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        categoryService.addCategory(category);
        return ResponseEntity.status(200).body(new ApiResponse("Category added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody @Valid Category category, Errors errors) {
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        Boolean updated = categoryService.updateCategory(id, category);
        if (!updated) {
            return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Category updated successfully"));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        Boolean deleted = categoryService.deleteCategory(id);
        if (!deleted) {
            return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully"));
    }
}
