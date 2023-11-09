package com.mtvs.arzip.repository;

import com.mtvs.arzip.domain.entity.FlooringInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlooringInfoRepository extends JpaRepository<FlooringInfo, Long> {

}
