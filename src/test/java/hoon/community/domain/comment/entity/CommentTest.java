package hoon.community.domain.comment.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Ref;
import java.util.List;
import java.util.Optional;

import static hoon.community.global.factory.dto.CommentFactory.createComment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void deleteTest() {
        //given
        Comment comment = createComment(null);
        boolean beforeDeleted = comment.isDeleted();

        //when
        comment.delete();

        //then
        boolean afterDeleted = comment.isDeleted();
        assertThat(beforeDeleted).isFalse();
        assertThat(afterDeleted).isTrue();
    }

    @Test
    void findDeletableCommentWhenExistsTest() {
        //given

        //root 1
        //1->2
        //2(del) -> 3(del) -> 5
        //2(del) -> 4
        Comment comment1 = createComment(null);
        Comment comment2 = createComment(comment1);
        Comment comment3 = createComment(comment2);
        Comment comment4 = createComment(comment2);
        Comment comment5 = createComment(comment3);
        comment2.delete();
        comment3.delete();
        ReflectionTestUtils.setField(comment1, "children", List.of(comment2));
        ReflectionTestUtils.setField(comment2, "children", List.of(comment3, comment4));
        ReflectionTestUtils.setField(comment3, "children", List.of(comment5));
        ReflectionTestUtils.setField(comment4, "children", List.of());
        ReflectionTestUtils.setField(comment5, "children", List.of());

        //when
        Optional<Comment> deletableComment = comment5.findDeletableComment();

        //then
        assertThat(deletableComment).containsSame(comment3);
    }

    @Test
    void findDeletableCommentWhenNotExistTest() {
        //given

        //root 1
        //1 -> 2 -> 3
        Comment comment1 = createComment(null);
        Comment comment2 = createComment(comment1);
        Comment comment3 = createComment(comment2);
        ReflectionTestUtils.setField(comment1, "children", List.of(comment2));
        ReflectionTestUtils.setField(comment2, "children", List.of(comment3));
        ReflectionTestUtils.setField(comment3, "children", List.of());

        //when
        Optional<Comment> deletableComment = comment2.findDeletableComment();

        //then
        assertThat(deletableComment).isEmpty();
    }

}