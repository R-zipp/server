package com.mtvs.arzip.domain.entity;

import com.mtvs.arzip.domain.enum_class.DrawingType;
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
public class AIDrawingData {
    // AI 3D 도면 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private DrawingType drawingType;  // 3D 도면 타입
    private String userDrawingImage;  // 사용자가 보낸 도면 이미지 링크
    private String threeDrawingImage; // 3D 도면 이미지 링크
}
