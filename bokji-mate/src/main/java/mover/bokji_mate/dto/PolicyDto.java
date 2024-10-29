package mover.bokji_mate.dto;

import lombok.*;
import mover.bokji_mate.domain.Policy;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDto {
    private Long id;
    private String title;
    private List<String> categories;
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
                .categories(policy.getCategories())
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
                .categories(categories)
                .department(department)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .views(views)
                .scrapCount(scrapCount)
                .build();
    }
}
