package mover.bokji_mate.domain;

import jakarta.persistence.*;

@Entity
public class Scrap {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;
}
