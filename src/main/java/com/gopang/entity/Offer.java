package com.gopang.entity;

import com.gopang.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
public class Offer extends BaseEntity{

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

    private int memberCount;

    public void addManager(Account account) {
        this.managers.add(account);
    }

    public boolean isJoinable(UserAccount userAccount){
        Account account = userAccount.getAccount();
        /*공개o, 모집o, 멤버x, 관리자x 면 가입가능*/
        return this.published &&!this.members.contains(account) && !this.managers.contains(account);
    }

    public boolean isMember(UserAccount userAccount){
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount){
        return this.managers.contains(userAccount.getAccount());
    }


    public String getPath() {
        return URLEncoder.encode(this.path, StandardCharsets.UTF_8);
    }

    public String getImage() {
        return image != null ? image : "/img/default_banner.jpg";
    }

    public void publish() {
        if (!this.closed && !this.published) {
            this.published =true;
            this.publishedDateTime=LocalDateTime.now();
        }else {
            throw new RuntimeException("작업을 공개 할수 없는 상태입니다. 작업신청을 이미 공개했거나 종료했습니다.");
        }
    }

    public void close(){
        if (this.published && !this.closed){
            this.closed=true;
            this.closedDateTime = LocalDateTime.now();
        }else {
            throw new RuntimeException("작업신청을 종료 할수 없습니다. 작업신청을 공개하지 않았거나 이미 종료한 작업입니다.");
        }
    }

    public boolean isManagedBy(Account account) {
        return this.getManagers().contains(account);
    }

    public boolean isRemovable() {
        return !this.published; //TODO 공개한 게시글은 삭제 할수 없다.
    }

    public void addMember(Account account) {
        this.getMembers().add(account);
        this.memberCount++;
    }

    public void removeMember(Account account) {
        this.getMembers().remove(account);
        this.memberCount--;
    }
}
