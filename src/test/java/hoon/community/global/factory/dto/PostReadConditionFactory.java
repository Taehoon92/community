package hoon.community.global.factory.dto;

import hoon.community.domain.post.dto.PostReadCondition;
import hoon.community.domain.post.entity.BoardType;

import java.util.List;

public class PostReadConditionFactory {
    public static PostReadCondition createPostReadCondition(Integer page, Integer size) {
        return new PostReadCondition(page, size, List.of(BoardType.FORUM), List.of(), List.of(), List.of(), List.of());
    }

    public static PostReadCondition createPostReadCondition(Integer page, Integer size, List<Long> memberIds) {
        return new PostReadCondition(page, size, List.of(BoardType.FORUM), memberIds, List.of(), List.of(), List.of());
    }
}
