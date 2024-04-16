package sideproject.gugumo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * 연관관계를 위한 임시 작성
 */

@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long memberId;
}
