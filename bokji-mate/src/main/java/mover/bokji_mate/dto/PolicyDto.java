package mover.bokji_mate.dto;

import lombok.*;
import mover.bokji_mate.domain.Policy;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long views;
    private Long scrapCount;

    static public PolicyDto toDto(Policy policy) {
        return PolicyDto.builder()
                .id(policy.getId())
                .title(policy.getTitle())
                .content(policy.getContent())
                .category(policy.getCategory())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .views(policy.getViews())
                .scrapCount(policy.getScrapCount())
                .build();
    }

    public Policy toEntity() {
        return Policy.builder()
                .id(id)
                .title(title)
                .content(content)
                .category(category)
                .startDate(startDate)
                .endDate(endDate)
                .views(views)
                .scrapCount(scrapCount)
                .build();
    }
}
