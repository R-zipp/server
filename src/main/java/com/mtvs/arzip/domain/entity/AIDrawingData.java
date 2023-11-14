package com.mtvs.arzip.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mtvs.arzip.domain.enum_class.DrawingType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @JsonProperty("URL") // JSON 키를 "URL"로 명시적으로 매핑
    private String fbxFile; // 3D 도면 이미지 링크

    @NotNull
    private String houseSize;  // 집 평수

    private int wallPaperNo;  // 벽지 번호

//     @JoinColumn(name = "user_no")
//     @ManyToOne(fetch = FetchType.LAZY)
//     private User user;

    // AI fbx 파일 url 추가
    public void updateFbxFile(String fbxFile) {
        this.fbxFile = fbxFile;
    }
}
