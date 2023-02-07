package hoon.community.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.member.dto.MemberDto;
import hoon.community.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private MemberDto member;
    private Integer hits;
    private Integer comments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Australia/Sydney")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Australia/Sydney")
    private LocalDateTime modifiedDate;

    public static PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                MemberDto.toDto(post.getMember()),
                post.getHits(),
                post.getComments().size(),
                post.getCreatedDate(),
                post.getModifiedDate()
        );
    }
}
