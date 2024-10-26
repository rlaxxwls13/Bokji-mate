package mover.bokji_mate.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Policy {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long views;
    private Long scrapCount;

    @OneToMany(mappedBy = "policy")
    private List<Scrap> scraps;

}
