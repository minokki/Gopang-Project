package com.gopang.entity;

import com.gopang.constant.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "email",unique = true, length = 255)
    private String email;

    @Column(unique = true, length = 255)
    private String nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob
    @Basic(fetch = FetchType.LAZY)  //lob = 텍스트 타입
    private String profileImage;

    private LocalDateTime emailCheckTokenGeneratedAt;

    @Enumerated(EnumType.STRING)
    private Role role;  // 사용자의 권한 정보 (USER 또는 ADMIN)

    @Column(nullable = false)
    private String userType;


    public void generateEmailCheckToken() {
        this.emailCheckToken= UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }


    public void completeSignUp() {
        this.emailVerified=true;
        this.joinedAt=LocalDateTime.now();
    }
    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusMinutes(5));
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean isManageOf(Offer offer) {
        return offer.getManagers().contains(this);
    }
}