package com.mtvs.arzip.domain.entity;

import com.mtvs.arzip.domain.enum_class.DrawingType;
import lombok.*;

import javax.persistence.*;

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

    @Enumerated(EnumType.STRING)
    private DrawingType drawingType;  // 사용자가 올린 이미지 타입

    private String userDrawingImage;  // 사용자가 보낸 도면 이미지 링크
    private String aiDrawingImage; // 3D 도면 이미지 링크

    // @JoinColumn(name = "user_no")
    //    @ManyToOne(fetch = FetchType.LAZY)
    //    private User user;  // 도면 이미지 user no 추가(인증/인가 진행 후)

    // AI fbx 파일 url 추가
    public void modify(String aiDrawingImage) {
        this.aiDrawingImage = aiDrawingImage;
    }
}
