package com.mtvs.arzip.repository;

import com.mtvs.arzip.domain.entity.ObjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjectInfoRepository extends JpaRepository<ObjectInfo, Long> {

    Optional<ObjectInfo> findByNo(Long no);

}
