package com.gopang.entity;

import com.gopang.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraph(name="Offer.withAll", attributeNodes = {
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")})
@Entity
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Offer {

    @Id @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime updateDateTime;

    //참여 여부
    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean userBanner;

    public void addManager(Account account) {
        this.managers.add(account);
    }

    public boolean isJoinable(UserAccount userAccount){
        Account account = userAccount.getAccount();
        /*공개o, 모집o, 멤버x, 관리자x 면 가입가능*/
        return this.isPublished() && this.isRecruiting() && !this.members.contains(account) && !this.managers.contains(account);
    }

    public boolean isMember(UserAccount userAccount){
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount){
        return this.managers.contains(userAccount.getAccount());
    }


}
