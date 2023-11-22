package com.mtvs.arzip.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ARSpaceData extends BaseEntity{
    // AR 공간 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @ColumnDefault(value = "0")
    private Integer views;      // 조회수

//    private Integer maxConnectionCount; // 최대 접속 인원 수

    private String title;  // 공간 이름

    private boolean isShared  = false;

    @JoinColumn(name = "user_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;  // AR 공간 생성자 이름

    @JoinColumn(name = "aiDrawingData_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private AIDrawingData aiDrawingData;  // AR 공간에 사용된 도면 No

    @OneToMany(mappedBy = "arSpaceData", fetch = FetchType.EAGER)
    private List<ARObjectPlacementData> placements = new ArrayList<>();

    public void share() {
        this.isShared = true;
    }

    public void addViews() {
        this.views++;
    }

    // toString 메서드를 오버라이드
    @Override
    public String toString() {
        return "ARSpaceData{" +
                "no=" + no +
                ", views=" + views +
                ", title='" + title + '\'' +
                ", isShared=" + isShared +
                ", user=" + user +
                ", aiDrawingData=" + aiDrawingData +
                '}';
    }


}
