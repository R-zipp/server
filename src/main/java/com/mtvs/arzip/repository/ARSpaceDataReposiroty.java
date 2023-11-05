package com.mtvs.arzip.repository;

import com.mtvs.arzip.domain.entity.ARSpaceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ARSpaceDataReposiroty extends JpaRepository<ARSpaceData, Long> {
}
