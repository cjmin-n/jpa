package com.ohgiraffers.section05.access.subsection01;

import jakarta.persistence.*;

@Entity(name = "member_section05_subsection01")
@Table(name = "tbl_member_section05_subsection01")
@Access(AccessType.FIELD)
public class Member {

    /*
    * 필드 접근은 기본 값이므로 해당 설정은 제거해도 동일하게 작동한다.
    * 또한 필드 레벨과 프로퍼티 레벨 모두 선언하면
    * 프로퍼티 레벨을 우선으로 사용한다.
    * */

    @Id
    @Column(name = "memberNo")
    private int memberNo;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_pwd")
    private String memberPwd;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone")
    private String phone;

    public Member() {
    }

    public Member(int memberNo, String memberId, String memberPwd, String nickname, String phone) {
        this.memberNo = memberNo;
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.nickname = nickname;
        this.phone = phone;
    }

    public int getMemberNo() {
        System.out.println("getMemberNo 로 access 되는지 확인");
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberId() {
        System.out.println("getMemberId 로 access 되는지 확인");
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPwd() {
        return memberPwd;
    }

    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberNo=" + memberNo +
                ", memberId='" + memberId + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
