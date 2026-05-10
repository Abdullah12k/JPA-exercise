package com.example.jpaexercise.Controller;

import com.example.jpaexercise.Api.ApiResponse;
import com.example.jpaexercise.Model.Product;
import com.example.jpaexercise.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.status(200).body(productService.getAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        productService.addProduct(product);
        return ResponseEntity.status(200).body(new ApiResponse("Product added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        Boolean updated = productService.updateProduct(id, product);
        if (!updated) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        Boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully"));
    }

    @GetMapping("/recommendations/{categoryId}")
    public ResponseEntity<?> getRecommendations(@PathVariable Integer categoryId) {
        List<Product> recommendations = productService.getRecommendations(categoryId);
        if (recommendations.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No recommendations found for this category"));
        }
        return ResponseEntity.status(200).body(recommendations);
    }

    @PutMapping("/increase-price/{merchantId}/{percentage}")
    public ResponseEntity<?> increasePriceByMerchant(@PathVariable Integer merchantId, @PathVariable double percentage) {
        int result = productService.increasePriceByMerchant(merchantId, percentage);
        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
        } else if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("No eligible products found (salesCount must be > 50)"));
        } else if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Percentage must be between 0 and 15"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Prices increased successfully"));
    }

    @PutMapping("/decrease-price/{merchantId}/{percentage}")
    public ResponseEntity<?> decreasePriceByMerchant(@PathVariable Integer merchantId, @PathVariable double percentage) {
        int result = productService.decreasePriceByMerchant(merchantId, percentage);
        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
        } else if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("No eligible products found (salesCount must be <= 15)"));
        } else if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Percentage must be between 0 and 100"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Prices decreased successfully"));
    }
}
