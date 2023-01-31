package hoon.community.global.security.guard;

import hoon.community.domain.role.entity.RoleType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class Guard {
    public final boolean check(Long id) {
        log.info("GUARD - CHECK call");
        log.info("GUARD - AuthHelper ={}", AuthHelper.isAuthenticated());
        return AuthHelper.isAuthenticated() && (hasRole(getRoleTypes()) || isResourceOwner(id));
    }

    abstract protected List<RoleType> getRoleTypes();
    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<RoleType> roleTypes) {
        log.info("GUARD - hasRole call");
        return roleTypes.stream().allMatch(roleType -> AuthHelper.extractMemberRoles().contains(roleType));
    }
}
