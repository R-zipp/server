package com.mtvs.arzip.repository;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiDrawingRepository extends JpaRepository<AIDrawingData, Long> {


}
