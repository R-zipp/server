package com.mtvs.arzip.repository;

import com.mtvs.arzip.domain.entity.ARObjectPlacementData;
import com.mtvs.arzip.domain.entity.ARSpaceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ARObjectPlacementDataRepository extends JpaRepository<ARObjectPlacementData, Long> {

    List<ARObjectPlacementData> findByArSpaceData (ARSpaceData arSpaceData);

}


