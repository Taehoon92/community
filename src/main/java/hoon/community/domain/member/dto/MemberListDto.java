package hoon.community.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class MemberListDto {

    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<MemberDetailsDto> memberList;

    public static MemberListDto toDto(Page<MemberDetailsDto> page) {
        return new MemberListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
