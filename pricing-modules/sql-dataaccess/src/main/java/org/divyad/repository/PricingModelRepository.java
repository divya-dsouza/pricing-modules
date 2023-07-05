package org.divyad.repository;

import org.divyad.domain.PricingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PricingModelRepository extends JpaRepository<PricingModel, Long> {

    List<PricingModel> findByStoreId(Long value);

    List<PricingModel> findBySku(String value);

    List<PricingModel> findByProduct(String value);

    List<PricingModel> findByPrice(Double value);

    List<PricingModel> findByDate(Timestamp value);
}
