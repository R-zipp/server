package com.mtvs.arzip.repository;

import com.mtvs.arzip.domain.entity.FlooringPlacementData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlooringPlacementDataRepository extends JpaRepository<FlooringPlacementData, Long> {

}
