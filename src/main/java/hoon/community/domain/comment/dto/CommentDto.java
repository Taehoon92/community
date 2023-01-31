package hoon.community.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.member.dto.MemberDto;
import hoon.community.global.helper.NestedConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private boolean deleted;
    private MemberDto member;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Australia/Sydney")
    private LocalDateTime createdDate;
    private List<CommentDto> children;

    public static List<CommentDto> toDtoList(List<Comment> comments) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                comments,
                c -> new CommentDto(c.getId(), c.isDeleted() ? null : c.getContent(), c.isDeleted(), c.isDeleted() ? null : MemberDto.toDto(c.getMember()), c.getCreatedDate(), new ArrayList<>()),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }
}
