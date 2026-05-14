package com.nod.backend.distributed_coding_genie.common_lib.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum ProjectRole {
    EDITOR(Set.of(ProjectPermission.VIEW, ProjectPermission.EDIT, ProjectPermission.DELETE,
            ProjectPermission.VIEW_MEMBERS)),
    VIEWER(Set.of(ProjectPermission.VIEW, ProjectPermission.VIEW_MEMBERS)),
    OWNER(Set.of(ProjectPermission.EDIT, ProjectPermission.VIEW, ProjectPermission.MANAGE_MEMBERS,
            ProjectPermission.VIEW_MEMBERS, ProjectPermission.DELETE));

    ProjectRole(ProjectPermission... permissions) {
        this.permissions = Set.of(permissions);
    }

    private final Set<ProjectPermission> permissions;
}
