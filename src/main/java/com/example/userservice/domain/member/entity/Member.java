package com.example.userservice.domain.member.entity;

import com.example.userservice.domain.auth.MemberRole;
import com.example.userservice.domain.auth.ProviderType;
import com.example.userservice.domain.member.dto.request.UpdateMemberRequestDto;
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
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW(),user_id = CONCAT(user_id, NOW()),state=False WHERE member_id = ?") // delete 된 유저를 몇일동안 보관할 것인지 에 대해 의논이 필요함
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
        private String phone;
        @Column(nullable = false)
        private String nickname;
        private Boolean state;
        @Column(nullable = false)
        private String profile;
        @Enumerated(EnumType.STRING)
        private MemberRole memberRole;
        @Enumerated(EnumType.STRING)
        private ProviderType providerType;

        private String providerId;

        @Builder
        public Member(Long id, String name, String userId, String password, String phone, String nickname,boolean state,String profile,ProviderType providerType,String providerId,MemberRole memberRole) {
                this.id = id;
                this.name = name;
                this.userId = userId;
                this.password = password;
                this.phone = phone;
                this.nickname = nickname;
                this.state=state;
                this.profile=profile;
                this.memberRole = memberRole;
                this.providerType=providerType;
                this.providerId=providerId;
        }

        public void updateMember(UpdateMemberRequestDto updateMemberRequesstDto, String profileName) {
                this.name=updateMemberRequesstDto.getName();
                this.profile=profileName;
                this.phone=updateMemberRequesstDto.getPhoneNumber();
        }

}
