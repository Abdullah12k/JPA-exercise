package com.example.jpaexercise.Service;

import com.example.jpaexercise.Model.Category;
import com.example.jpaexercise.Model.Merchant;
import com.example.jpaexercise.Repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public List<Merchant> getAllMerchant(){
        return merchantRepository.findAll();
    }

    public void addMerchant(Merchant merchant){
        merchantRepository.save(merchant);
    }

    public Boolean updateMerchant(Integer id, Merchant merchant){
        Merchant oldMerchant = merchantRepository.getById(id);
        if (oldMerchant == null){
            return false;
        }
        oldMerchant.setName(merchant.getName());
        merchantRepository.save(oldMerchant);
        return true;
    }

    public Boolean deleteMerchant(Integer id){
        Merchant delete = merchantRepository.getById(id);
        if (delete == null){
            return false;
        }
        merchantRepository.delete(delete);
        return true;
    }
}

