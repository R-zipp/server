package com.mtvs.arzip.repository;

import com.mtvs.arzip.domain.entity.WallPaperInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WallPaperInfoRepository extends JpaRepository<WallPaperInfo, Long> {

}
