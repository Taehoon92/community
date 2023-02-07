package hoon.community.domain.post.repository;

import hoon.community.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    @Query("select p from Post p join fetch p.member where p.id = :id")
    Optional<Post> findByIdWithMember(Long id);

    @Modifying
    @Query("update Post p set p.hits = p.hits + 1 where p.id = :id")
    int updateHits(@Param("id") Long id);
}
