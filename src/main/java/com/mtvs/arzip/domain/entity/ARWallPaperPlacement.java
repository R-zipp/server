package com.mtvs.arzip.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ARWallPaperPlacement {
    // AR 공간에 적용된 벽지 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @JoinColumn(name = "arSpaceData_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private ARSpaceData arSpaceData;   // AR 공간 no

    @JoinColumn(name = "wallPaperInfo_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private WallPaperInfo wallPaperInfo;  // 벽지 no

}
