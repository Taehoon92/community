package hoon.community.domain.post.entity;

import hoon.community.domain.BaseTimeEntity;
import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.post.dto.ImageUpdatedResult;
import hoon.community.domain.post.dto.PostUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Table(name = "POST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int hits;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

    private boolean existImages;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Builder
    public Post(String title, String content, BoardType boardType, Member member, List<Image> images) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.member = member;
        this.hits = 0;
        this.comments = new ArrayList<>();
        this.images = new ArrayList<>();
        this.existImages = false;
        addImages(images);
    }

    public ImageUpdatedResult update(PostUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        ImageUpdatedResult result = findImageUpdatedResult(request.getAddedImages(), request.getDeletedImages());
        addImages(result.getAddedImages());
        deleteImages(result.getDeletedImages());
        return result;
    }

    public void increaseHits() {
        this.hits ++;
    }
    public void decreaseHits() {
        this.hits --;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    private void addImages(List<Image> added) {
        added.stream().forEach(i -> {
            images.add(i);
            i.initPost(this);
            this.existImages = true;
        });
    }

    private void deleteImages(List<Image> deleted) {
        deleted.stream().forEach(i -> this.images.remove(i));
    }

    private ImageUpdatedResult findImageUpdatedResult(List<MultipartFile> addedImageFiles, List<Long> deletedImageIds) {
        List<Image> addedImages = convertImageFilesToImages(addedImageFiles);
        List<Image> deletedImages = convertImageIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addedImageFiles, addedImages, deletedImages);
    }

    private List<Image> convertImageIdsToImages(List<Long> imageIds) {
        return imageIds.stream()
                .map(id -> convertImageIdToImage(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(Collectors.toList());
    }

    private Optional<Image> convertImageIdToImage(Long id) {
        return this.images.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    private List<Image> convertImageFilesToImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream().map(imageFile -> new Image(imageFile.getOriginalFilename())).collect(Collectors.toList());
    }
}
