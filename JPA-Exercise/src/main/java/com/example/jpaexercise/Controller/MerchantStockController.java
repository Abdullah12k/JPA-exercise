package com.example.jpaexercise.Controller;

import com.example.jpaexercise.Api.ApiResponse;
import com.example.jpaexercise.Model.MerchantStock;
import com.example.jpaexercise.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllMerchantStocks() {
        return ResponseEntity.status(200).body(merchantStockService.getAllMerchantStock());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStock(@RequestBody @Valid MerchantStock merchantStock) {
        merchantStockService.addMerchantStock(merchantStock);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant stock added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchantStock(@PathVariable Integer id, @RequestBody @Valid MerchantStock merchantStock) {
        Boolean updated = merchantStockService.updateMerchantStock(id, merchantStock);
        if (!updated) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Merchant stock updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable Integer id) {
        Boolean deleted = merchantStockService.deleteMerchantStock(id);
        if (!deleted) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Merchant stock deleted successfully"));
    }

    @PutMapping("/add-stock/{productId}/{merchantId}/{amount}")
    public ResponseEntity<?> addStock(@PathVariable Integer productId, @PathVariable Integer merchantId, @PathVariable int amount) {
        Boolean result = merchantStockService.addStock(productId, merchantId, amount);
        if (!result) {
            return ResponseEntity.status(400).body(new ApiResponse("No stock found for given product and merchant"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Stock updated successfully"));
    }
}
