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

    private String email;
    private String password;

    private String name;        // 이름
    private String nickname;    // 닉네임

    @Enumerated(EnumType.STRING)
    private UserRole role;  // 권한

    private Long lastUploadFloorPlanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_no")
    private UserCharacter characterNo;  // 유저가 선택한 캐릭터 No

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateUser(String password) {
        this.password = password;
    }

    public void updateLastUploadFloorPlanId(Long lastUploadFloorPlanId) {
        this.lastUploadFloorPlanId = lastUploadFloorPlanId;
    }
}
