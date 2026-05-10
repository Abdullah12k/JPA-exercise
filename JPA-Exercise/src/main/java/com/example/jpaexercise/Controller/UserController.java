package com.example.jpaexercise.Controller;

import com.example.jpaexercise.Api.ApiResponse;
import com.example.jpaexercise.Model.User;
import com.example.jpaexercise.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user) {
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid User user) {
        Boolean updated = userService.updateUser(id, user);
        if (!updated) {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        Boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }

    @PostMapping("/buy/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> buyProduct(@PathVariable Integer userId,
                                        @PathVariable Integer productId,
                                        @PathVariable Integer merchantId) {
        int result = userService.buyProduct(userId, productId, merchantId);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        } else if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
        } else if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        } else if (result == 4) {
            return ResponseEntity.status(400).body(new ApiResponse("Product is out of stock"));
        } else if (result == 5) {
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Purchase successful"));
    }

    @PutMapping("/transfer/{fromUserId}/{toUserId}/{amount}")
    public ResponseEntity<?> transfer(@PathVariable Integer fromUserId, @PathVariable Integer toUserId, @PathVariable double amount) {
        int result = userService.transfer(fromUserId, toUserId, amount);
        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Sender not found"));
        } else if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Receiver not found"));
        } else if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Amount must be greater than 0"));
        } else if (result == 4) {
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Transfer successful"));
    }

    @PostMapping("/buy-gift/{user1Id}/{user2Id}/{productId}/{merchantId}")
    public ResponseEntity<?> buyProductAsGift(@PathVariable Integer user1Id, @PathVariable Integer user2Id, @PathVariable Integer productId, @PathVariable Integer merchantId) {
        int result = userService.buyProductAsGift(user1Id, user2Id, productId, merchantId);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        } else if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
        } else if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Buyer not found"));
        } else if (result == 4) {
            return ResponseEntity.status(400).body(new ApiResponse("Recipient not found"));
        } else if (result == 5) {
            return ResponseEntity.status(400).body(new ApiResponse("Product is out of stock"));
        } else if (result == 6) {
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Gift purchase successful"));
    }

    @PutMapping("/exchange/{userId}/{oldProductId}/{newProductId}/{merchantId}")
    public ResponseEntity<?> exchangeProduct(@PathVariable Integer userId, @PathVariable Integer oldProductId, @PathVariable Integer newProductId, @PathVariable Integer merchantId) {
        int result = userService.exchangeProduct(userId, oldProductId, newProductId, merchantId);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        } else if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Old product not found"));
        } else if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("New product not found"));
        } else if (result == 4) {
            return ResponseEntity.status(400).body(new ApiResponse("New product not in stock at this merchant"));
        } else if (result == 5) {
            return ResponseEntity.status(400).body(new ApiResponse("No purchase record found for old product"));
        } else if (result == 6) {
            return ResponseEntity.status(400).body(new ApiResponse("New product is out of stock"));
        } else if (result == 7) {
            return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance for price difference"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Exchange successful"));
    }

    @PutMapping("/refund/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> refund(@PathVariable Integer userId, @PathVariable Integer productId, @PathVariable Integer merchantId) {
        int result = userService.refund(userId, productId, merchantId);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        } else if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        } else if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
        } else if (result == 4) {
            return ResponseEntity.status(400).body(new ApiResponse("No completed purchase record found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Refund successful"));
    }
}