package com.example.userservice.domain.member.entity;

import com.example.userservice.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW(),user_id = CONCAT(user_id, NOW()),state=False WHERE member_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Member extends BaseTimeEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "MEMBER_ID")
        private Long id;
        @Column(nullable = false, length = 50 ,unique = true)
        private String name;
        @Column(nullable = false, unique = true)
        private String userId;
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Column(nullable = false)
        private String password;
        @Column(nullable = false)
        private String phone;
        @Column(nullable = false)
        private String nickname;
        private Boolean state;
        @Column(nullable = false)
        private String profile;
        @Builder
        public Member(Long id, String name, String userId, String password, String phone, String nickname,boolean state,String profile) {
                this.id = id;
                this.name = name;
                this.userId = userId;
                this.password = password;
                this.phone = phone;
                this.nickname = nickname;
                this.state=state;
                this.profile=profile;
        }

}
