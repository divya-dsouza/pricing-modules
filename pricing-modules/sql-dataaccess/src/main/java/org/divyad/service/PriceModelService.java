package org.divyad.service;

import lombok.extern.slf4j.Slf4j;
import org.divyad.domain.PricingModel;
import org.divyad.repository.PricingModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PriceModelService {

    @Autowired
    PricingModelRepository pricingModelRepository;

    public String dumpPricingInfo(List<PricingModel> pricingModels) {
        try {
            pricingModelRepository.saveAll(pricingModels);
            return "File Uploaded successfully";
        } catch (Exception e){
            log.error("Error in persisting pricing info", e);
            return "Technical Exception in Upload";
        }
    }

    public List<PricingModel> find(String field, String value) {
        List<PricingModel> pricingModels = null;
        switch (field) {
            case "STOREID":
                pricingModels = pricingModelRepository.findByStoreId(value);
                break;
            case "SKU":
                pricingModels = pricingModelRepository.findBySku(value);
                break;
            case "NAME":
                pricingModels = pricingModelRepository.findByProduct(value);
                break;
            case "PRICE":
                pricingModels = pricingModelRepository.findByPrice(value);
                break;
            case "DATE":
                pricingModels = pricingModelRepository.findByDate(value);
                break;
        }

        return pricingModels;
    }
}
