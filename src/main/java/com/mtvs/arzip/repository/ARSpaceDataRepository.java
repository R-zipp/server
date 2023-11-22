package com.mtvs.arzip.repository;

import com.mtvs.arzip.domain.entity.ARSpaceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ARSpaceDataRepository extends JpaRepository<ARSpaceData, Long> {

    Optional<ARSpaceData> findByNo(Long no);


}
