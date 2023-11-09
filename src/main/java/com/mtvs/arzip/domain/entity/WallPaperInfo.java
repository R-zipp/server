package com.mtvs.arzip.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WallPaperInfo {
    // 벽지 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String wallPaperType;  // 벽지 타입

    public WallPaperInfo(String wallPaperType) {
        this.wallPaperType = wallPaperType;
    }

}
