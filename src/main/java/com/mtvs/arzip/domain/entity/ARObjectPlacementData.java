package com.mtvs.arzip.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ARObjectPlacementData {
    // AR 공간에 배치된 오브젝트 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private Integer x;  // x 좌표 값
    private Integer y;  // y 좌표 값
    private Integer z;  // z 좌표 값
    private Integer rotation; // 오브젝트 회전 값

    @JoinColumn(name = "arSpaceData_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private ARSpaceData arSpaceData;   // AR 공간 no

    @JoinColumn(name = "objectInfo_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private ObjectInfo objectInfo;    // 오브젝트 no

}
