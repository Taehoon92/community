package hoon.community.domain.post.entity;

import hoon.community.domain.BaseTimeEntity;
import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.post.dto.PostUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int hits;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.hits = 0;
        this.comments = new ArrayList<>();
    }

    public void update(PostUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
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
}
