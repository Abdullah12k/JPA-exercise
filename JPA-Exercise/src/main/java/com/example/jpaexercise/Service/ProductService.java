package com.example.jpaexercise.Service;

import com.example.jpaexercise.Model.Merchant;
import com.example.jpaexercise.Model.MerchantStock;
import com.example.jpaexercise.Model.Product;
import com.example.jpaexercise.Repository.MerchantRepository;
import com.example.jpaexercise.Repository.MerchantStockRepository;
import com.example.jpaexercise.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantStockRepository merchantStockRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void addProduct(Product product){
        productRepository.save(product);
    }

    public Boolean updateProduct(Integer id, Product product){
        Product oldProduct = productRepository.getById(id);
        if (oldProduct == null){
            return false;
        }
        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setCategoryId(product.getCategoryId());
        oldProduct.setSalesCount(product.getSalesCount());
        productRepository.save(oldProduct);
        return true;
    }

    public Boolean deleteProduct(Integer id){
        Product delete = productRepository.getById(id);
        if (delete == null){
            return false;
        }
        productRepository.delete(delete);
        return true;
    }

    // extra
    public List<Product> getRecommendations(Integer categoryId) {
        List<Product> recommendations = new ArrayList<>();
        List<Product> allProducts = productRepository.findAll();
        List<MerchantStock> allStocks = merchantStockRepository.findAll();

        for (Product p : allProducts) {
            if (p.getCategoryId().equals(categoryId) && p.getSalesCount() > 5) {
                boolean isAvailable = false;
                for (MerchantStock ms : allStocks) {
                    if (ms.getProductId().equals(p.getId()) && ms.getStock() > 0) {
                        isAvailable = true;
                        break;
                    }
                }
                if (isAvailable) {
                    recommendations.add(p);
                }
            }
            if (recommendations.size() == 3) {
                break;
            }
        }

        return recommendations;
    }
    // extra
    public int increasePriceByMerchant(Integer merchantId, double percentage) {
        if (percentage > 15 || percentage <= 0) {
            return 3;
        }

        List<Merchant> allMerchants = merchantRepository.findAll();
        boolean merchantExists = false;
        for (Merchant m : allMerchants) {
            if (m.getId().equals(merchantId)) {
                merchantExists = true;
                break;
            }
        }
        if (!merchantExists) {
            return 0;
        }

        List<MerchantStock> allStocks = merchantStockRepository.findAll();
        List<Product> allProducts = productRepository.findAll();
        int updateCount = 0;

        for (MerchantStock ms : allStocks) {
            if (ms.getMerchantId().equals(merchantId)) {
                for (Product p : allProducts) {
                    if (p.getId().equals(ms.getProductId())) {
                        if (p.getSalesCount() > 50) {
                            double newPrice = p.getPrice() + (p.getPrice() * percentage / 100);
                            p.setPrice((int) newPrice);
                            productRepository.save(p);
                            updateCount++;
                        }
                    }
                }
            }
        }

        if (updateCount == 0) {
            return 1;
        }

        return 2;
    }
    // extra
    public int decreasePriceByMerchant(Integer merchantId, double percentage) {
        if (percentage <= 0 || percentage > 100) {
            return 3;
        }

        List<Merchant> allMerchants = merchantRepository.findAll();
        boolean merchantExists = false;
        for (Merchant m : allMerchants) {
            if (m.getId().equals(merchantId)) {
                merchantExists = true;
                break;
            }
        }
        if (!merchantExists) {
            return 0;
        }

        List<MerchantStock> allStocks = merchantStockRepository.findAll();
        List<Product> allProducts = productRepository.findAll();
        int updateCount = 0;

        for (MerchantStock ms : allStocks) {
            if (ms.getMerchantId().equals(merchantId)) {
                for (Product p : allProducts) {
                    if (p.getId().equals(ms.getProductId())) {
                        if (p.getSalesCount() <= 15) {
                            double newPrice = p.getPrice() - (p.getPrice() * percentage / 100);
                            p.setPrice((int) newPrice);
                            productRepository.save(p);
                            updateCount++;
                        }
                    }
                }
            }
        }

        if (updateCount == 0) {
            return 1;
        }

        return 2;
    }
}
