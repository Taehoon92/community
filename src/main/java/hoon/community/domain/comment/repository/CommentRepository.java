package hoon.community.domain.comment.repository;

import hoon.community.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c left join fetch c.parent where c.id = :id")
    Optional<Comment> findWithParentById(Long id);

    @Query("select c from Comment c join fetch c.member left join fetch c.parent where c.post.id = :postId order by c.parent.id asc nulls first, c.id asc")
    List<Comment> findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(@Param("postId") Long postId);
}
