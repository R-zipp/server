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

    private double x;  // x 좌표 값
    private double y;  // y 좌표 값
    private double z;  // z 좌표 값
    private double rotation;  // 회전 값

    @Column(name = "size_x")
    private double sizeX;  // 크기 x 좌표 값

    @Column(name = "size_y")
    private double sizeY;  // 크기 y 좌표 값

    @JoinColumn(name = "arSpaceData_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private ARSpaceData arSpaceData;   // AR 공간 no

    @JoinColumn(name = "wallPaperInfo_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private WallPaperInfo wallPaperInfo;  // 벽지 no

}
