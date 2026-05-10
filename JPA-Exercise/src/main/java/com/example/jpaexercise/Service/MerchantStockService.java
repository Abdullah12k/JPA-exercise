package com.example.jpaexercise.Service;


import com.example.jpaexercise.Model.MerchantStock;
import com.example.jpaexercise.Repository.MerchantRepository;
import com.example.jpaexercise.Repository.MerchantStockRepository;
import com.example.jpaexercise.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantStockService {
    private final MerchantStockRepository merchantStockRepository;
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;

    public List<MerchantStock> getAllMerchantStock(){
        return merchantStockRepository.findAll();
    }

    public void addMerchantStock(MerchantStock merchantStock){
        merchantStockRepository.save(merchantStock);
    }

    public Boolean updateMerchantStock(Integer id, MerchantStock merchantStock){
        MerchantStock oldMerchantStock = merchantStockRepository.getById(id);
        if (oldMerchantStock == null){
            return false;
        }
        oldMerchantStock.setMerchantId(merchantStock.getMerchantId());
        oldMerchantStock.setStock(merchantStock.getStock());
        oldMerchantStock.setProductId(merchantStock.getProductId());
        merchantStockRepository.save(oldMerchantStock);
        return true;
    }

    public Boolean deleteMerchantStock(Integer id){
        MerchantStock delete = merchantStockRepository.getById(id);
        if (delete == null){
            return false;
        }
        merchantStockRepository.delete(delete);
        return true;
    }

    public Boolean addStock(Integer productId,Integer merchantId, int amount) {
        List<MerchantStock> merchantStocks=merchantStockRepository.findAll();
        for (MerchantStock stock:merchantStocks){
            if (stock.getProductId().equals(productId) && stock.getMerchantId().equals(merchantId)){
                stock.setStock(stock.getStock()+amount);
                merchantStockRepository.save(stock);
                return true;
            }
        }
        return false;
    }
}


