package hoon.community.domain.post.dto;

import hoon.community.domain.post.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {

    private Long id;
    private String originName;
    private String uniqueName;

    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getId(), image.getOriginName(), image.getUniqueName());
    }
}
