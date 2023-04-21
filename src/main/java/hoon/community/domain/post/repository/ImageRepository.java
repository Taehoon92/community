package hoon.community.domain.post.repository;

import hoon.community.domain.post.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
