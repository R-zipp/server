package com.mtvs.arzip.domain.entity;

import com.mtvs.arzip.domain.enum_class.ObjectType;
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
public class ObjectInfo {
    // 오브젝트 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private ObjectType objectType;  // 오브젝트 타입
    private String objectImage; // 오브젝트 이미지 링크

}
