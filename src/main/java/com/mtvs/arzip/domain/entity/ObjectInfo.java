package com.mtvs.arzip.domain.entity;

import com.mtvs.arzip.domain.enum_class.ObjectType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ObjectInfo {
    // 오브젝트 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Enumerated(EnumType.STRING)
    private ObjectType objectType;  // 오브젝트 타입

    private String objectName;  // 오브젝트 이름
//    private String objectImage; // 오브젝트 이미지 링크


    public ObjectInfo(String objectName, ObjectType objectType) {
        this.objectName = objectName;
        this.objectType = objectType;
    }
}
