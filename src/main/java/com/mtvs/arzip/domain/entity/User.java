package com.mtvs.arzip.domain.entity;

import com.mtvs.arzip.domain.enum_class.UserRole;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "User_TB")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String id;
    private String password;

    private String name;        // 이름
    private Integer age;        // 나이
    private String gender;      // 성별
    private Integer height;     // 키
    private Integer weight;     // 몸무게
    private String nickname;    // 닉네임

    @Enumerated(EnumType.STRING)
    private UserRole role;  // 권한

    private String provider;   // 소셜 로그인 타입
    private String providerId; // 소셜 로그인 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_no")
    private UserCharacter characterNo;  // 유저가 선택한 캐릭터 No
}
