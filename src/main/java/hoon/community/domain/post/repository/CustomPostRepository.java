package hoon.community.domain.post.repository;

import hoon.community.domain.post.dto.PostInfoDto;
import hoon.community.domain.post.dto.PostReadCondition;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {
    Page<PostInfoDto> findAllByCondition(PostReadCondition condition);
}
