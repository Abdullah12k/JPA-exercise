package com.example.jpaexercise.Service;

import com.example.jpaexercise.Model.MerchantStock;
import com.example.jpaexercise.Model.Product;
import com.example.jpaexercise.Model.PurchaseHistory;
import com.example.jpaexercise.Model.User;
import com.example.jpaexercise.Repository.MerchantStockRepository;
import com.example.jpaexercise.Repository.ProductRepository;
import com.example.jpaexercise.Repository.PurchaseHistoryRepository;
import com.example.jpaexercise.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MerchantStockRepository merchantStockRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public Boolean updateUser(Integer id, User user){
        User oldUser = userRepository.getById(id);
        if (oldUser == null){
            return false;
        }
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        oldUser.setBalance(user.getBalance());
        oldUser.setRole(user.getRole());
        userRepository.save(oldUser);
        return true;
    }

    public Boolean deleteUser(Integer id){
        User delete = userRepository.getById(id);
        if (delete == null){
            return false;
        }
        userRepository.delete(delete);
        return true;
    }

    public int buyProduct(Integer userId, Integer productId, Integer merchantId) {
        List<Product> allProducts = productRepository.findAll();
        Product product = null;
        for (Product p : allProducts) {
            if (p.getId().equals(productId)) {
                product = p;
                break;
            }
        }

        List<MerchantStock> allStocks = merchantStockRepository.findAll();
        MerchantStock merchantStock = null;
        for (MerchantStock ms : allStocks) {
            if (ms.getMerchantId().equals(merchantId) && ms.getProductId().equals(productId)) {
                merchantStock = ms;
                break;
            }
        }

        List<User> allUsers = userRepository.findAll();
        User user = null;
        for (User u : allUsers) {
            if (u.getId().equals(userId)) {
                user = u;
                break;
            }
        }
        if (product == null) {
            return 1;
        }

        if (merchantStock == null) {
            return 2;
        }

        if (user == null) {
            return 3;
        }

        if (merchantStock.getStock() <= 0) {
            return 4;
        }

        if (user.getBalance() < product.getPrice()) {
            return 5;
        }

        user.setBalance(user.getBalance() - product.getPrice());
        merchantStock.setStock(merchantStock.getStock() - 1);
        product.setSalesCount(product.getSalesCount() + 1);

        userRepository.save(user);
        merchantStockRepository.save(merchantStock);
        productRepository.save(product);

        PurchaseHistory history = new PurchaseHistory();
        history.setUserId(userId);
        history.setProductId(productId);
        history.setMerchantId(merchantId);
        history.setPricePaid(product.getPrice());
        history.setStatus("COMPLETED");
        purchaseHistoryRepository.save(history);

        return 0;
    }

    // extra
    public int transfer(Integer fromUserId, Integer toUserId, double amount) {
        if (amount <= 0) {
            return 3;
        }

        List<User> allUsers = userRepository.findAll();
        User user1 = null;
        User user2 = null;

        for (User u : allUsers) {
            if (u.getId().equals(fromUserId)) {
                user1 = u;
            }
            if (u.getId().equals(toUserId)) {
                user2 = u;
            }
        }

        if (user1 == null) {
            return 1;
        }
        if (user2 == null) {
            return 2;
        }

        if (user1.getBalance() < amount) {
            return 4;
        }

        user1.setBalance(user1.getBalance() - amount);
        user2.setBalance(user2.getBalance() + amount);

        userRepository.save(user1);
        userRepository.save(user2);

        return 0;
    }
    // extra
    public int buyProductAsGift(Integer user1Id, Integer user2Id, Integer productId, Integer merchantId) {
        List<Product> allProducts = productRepository.findAll();
        Product product = null;
        for (Product p : allProducts) {
            if (p.getId().equals(productId)) {
                product = p;
                break;
            }
        }
        if (product == null) {
            return 1;
        }

        List<MerchantStock> allStocks = merchantStockRepository.findAll();
        MerchantStock merchantStock = null;
        for (MerchantStock ms : allStocks) {
            if (ms.getMerchantId().equals(merchantId) && ms.getProductId().equals(productId)) {
                merchantStock = ms;
                break;
            }
        }
        if (merchantStock == null) {
            return 2;
        }

        List<User> allUsers = userRepository.findAll();
        User user1 = null;
        User user2 = null;
        for (User u : allUsers) {
            if (u.getId().equals(user1Id)) {
                user1 = u;
            }
            if (u.getId().equals(user2Id)) {
                user2 = u;
            }
        }

        if (user1 == null) {
            return 3;
        }
        if (user2 == null) {
            return 4;
        }

        if (merchantStock.getStock() <= 0) {
            return 5;
        }

        if (user1.getBalance() < product.getPrice()) {
            return 6;
        }

        user1.setBalance(user1.getBalance() - product.getPrice());
        merchantStock.setStock(merchantStock.getStock() - 1);
        product.setSalesCount(product.getSalesCount() + 1);

        userRepository.save(user1);
        merchantStockRepository.save(merchantStock);
        productRepository.save(product);

        PurchaseHistory history = new PurchaseHistory();
        history.setUserId(user2Id);
        history.setProductId(productId);
        history.setMerchantId(merchantId);
        history.setPricePaid(product.getPrice());
        history.setStatus("COMPLETED");
        purchaseHistoryRepository.save(history);

        return 0;
    }
    // extra
    public int exchangeProduct(Integer userId, Integer oldProductId, Integer newProductId, Integer merchantId) {
        User user = null;
        for (User u : userRepository.findAll()) {
            if (u.getId().equals(userId)) {
                user = u;
                break;
            }
        }
        if (user == null) return 1;

        Product oldProduct = null;
        Product newProduct = null;
        for (Product p : productRepository.findAll()) {
            if (p.getId().equals(oldProductId)) oldProduct = p;
            if (p.getId().equals(newProductId)) newProduct = p;
        }
        if (oldProduct == null) return 2;
        if (newProduct == null) return 3;

        MerchantStock newProductStock = null;
        MerchantStock oldProductStock = null;
        for (MerchantStock ms : merchantStockRepository.findAll()) {
            if (ms.getMerchantId().equals(merchantId)) {
                if (ms.getProductId().equals(newProductId)) newProductStock = ms;
                if (ms.getProductId().equals(oldProductId)) oldProductStock = ms;
            }
        }
        if (newProductStock == null) return 4;

        PurchaseHistory oldHistoryRecord = null;
        for (PurchaseHistory h : purchaseHistoryRepository.findAll()) {
            if (h.getUserId().equals(userId) && h.getProductId().equals(oldProductId) && h.getMerchantId().equals(merchantId) && h.getStatus().equals("COMPLETED")) {
                oldHistoryRecord = h;
                break;
            }
        }
        if (oldHistoryRecord == null) return 5;

        if (newProductStock.getStock() <= 0) return 6;

        double priceDifference = newProduct.getPrice() - oldProduct.getPrice();
        if (priceDifference > 0 && user.getBalance() < priceDifference) return 7;

        user.setBalance(user.getBalance() - priceDifference);
        newProductStock.setStock(newProductStock.getStock() - 1);

        if (oldProductStock != null) {
            oldProductStock.setStock(oldProductStock.getStock() + 1);
            merchantStockRepository.save(oldProductStock);
        }

        newProduct.setSalesCount(newProduct.getSalesCount() + 1);
        oldProduct.setSalesCount(oldProduct.getSalesCount() - 1);

        oldHistoryRecord.setStatus("EXCHANGED");
        purchaseHistoryRepository.save(oldHistoryRecord);

        PurchaseHistory newHistory = new PurchaseHistory();
        newHistory.setUserId(userId);
        newHistory.setProductId(newProductId);
        newHistory.setMerchantId(merchantId);
        newHistory.setPricePaid(newProduct.getPrice());
        newHistory.setStatus("COMPLETED");
        purchaseHistoryRepository.save(newHistory);

        userRepository.save(user);
        merchantStockRepository.save(newProductStock);
        productRepository.save(oldProduct);
        productRepository.save(newProduct);

        return 0;
    }
    // extra
    public int refund(Integer userId, Integer productId, Integer merchantId) {
        List<User> allUsers = userRepository.findAll();
        User user = null;
        for (User u : allUsers) {
            if (u.getId().equals(userId)) {
                user = u;
                break;
            }
        }
        if (user == null) {
            return 1;
        }

        List<Product> allProducts = productRepository.findAll();
        Product product = null;
        for (Product p : allProducts) {
            if (p.getId().equals(productId)) {
                product = p;
                break;
            }
        }
        if (product == null) {
            return 2;
        }

        List<MerchantStock> allStocks = merchantStockRepository.findAll();
        MerchantStock merchantStock = null;
        for (MerchantStock ms : allStocks) {
            if (ms.getMerchantId().equals(merchantId) && ms.getProductId().equals(productId)) {
                merchantStock = ms;
                break;
            }
        }
        if (merchantStock == null) {
            return 3;
        }

        List<PurchaseHistory> allHistory = purchaseHistoryRepository.findAll();
        PurchaseHistory record = null;
        for (PurchaseHistory h : allHistory) {
            if (h.getUserId().equals(userId) && h.getProductId().equals(productId) && h.getMerchantId().equals(merchantId) && h.getStatus().equals("COMPLETED")) {
                record = h;
                break;
            }
        }
        if (record == null) {
            return 4;
        }

        user.setBalance(user.getBalance() + record.getPricePaid());
        merchantStock.setStock(merchantStock.getStock() + 1);
        product.setSalesCount(product.getSalesCount() - 1);

        record.setStatus("REFUNDED");

        userRepository.save(user);
        merchantStockRepository.save(merchantStock);
        productRepository.save(product);
        purchaseHistoryRepository.save(record);

        return 0;
    }

}

