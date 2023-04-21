package hoon.community.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInfoDto {
    private Long id;
    private String title;
    private String username;
    private Integer hits;
    private Integer comments;
    private boolean existImages;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Australia/Sydney")
    private LocalDateTime createdDate;

}
