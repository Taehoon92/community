package hoon.community.global.security.guard;

import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.comment.repository.CommentRepository;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentGuard {

    private final AuthHelper authHelper;
    private final CommentRepository commentRepository;

    public boolean check(Long id) {
        return authHelper.isAuthenticated() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResourceOwner(id);
    }

    private boolean isResourceOwner(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> {throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);});
        Long memberId = authHelper.extractMemberId();
        return comment.getMember().getId().equals(memberId);
    }

    private boolean hasAdminRole() {
        return authHelper.extractMemberRoles().contains(RoleType.ROLE_ADMIN);
    }

}
