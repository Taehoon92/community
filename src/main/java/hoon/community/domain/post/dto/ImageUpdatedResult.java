package hoon.community.domain.post.dto;

import hoon.community.domain.post.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageUpdatedResult {
    private List<MultipartFile> addedImageFiles;
    private List<Image> addedImages;
    private List<Image> deletedImages;
}
