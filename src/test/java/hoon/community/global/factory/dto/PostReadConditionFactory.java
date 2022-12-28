package hoon.community.global.factory.dto;

import hoon.community.domain.post.dto.PostReadCondition;

import java.util.List;

public class PostReadConditionFactory {
    public static PostReadCondition createPostReadCondition(Integer page, Integer size) {
        return new PostReadCondition(page, size, List.of());
    }

    public static PostReadCondition createPostReadCondition(Integer page, Integer size, List<Long> memberIds) {
        return new PostReadCondition(page, size, memberIds);
    }
}
