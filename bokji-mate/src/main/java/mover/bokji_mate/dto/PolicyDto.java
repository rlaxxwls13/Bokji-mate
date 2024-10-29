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
    private String category;
    private String department;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long views;
    private Long scrapCount;

    static public PolicyDto toDto(Policy policy) {
        return PolicyDto.builder()
                .id(policy.getId())
                .title(policy.getTitle())
                .category(policy.getCategory())
                .department(policy.getDepartment())
                .content(policy.getContent())
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
                .category(category)
                .department(department)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .views(views)
                .scrapCount(scrapCount)
                .build();
    }
}
