package mover.bokji_mate.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Policy {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private String department;
    @ElementCollection
    private List<String> categories;
    private LocalDate startDate;
    private LocalDate endDate;
    @Builder.Default
    private Long views = 0L;
    @Builder.Default
    private Long scrapCount = 0L;

    @OneToMany(mappedBy = "policy")
    private List<Scrap> scraps;

}
