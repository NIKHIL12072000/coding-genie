package com.nod.backend.distributed_coding_genie.intelligence_service.security;

import com.nod.backend.distributed_coding_genie.common_lib.enums.ProjectPermission;
import com.nod.backend.distributed_coding_genie.common_lib.security.AuthUtil;
import com.nod.backend.distributed_coding_genie.intelligence_service.client.WorkspaceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

@Component("security")
@RequiredArgsConstructor
@Slf4j
public class SecurityExpressions {

    private final AuthUtil authUtil;
    private final WorkspaceClient workspaceClient;

    private boolean hasPermission(Long projectId, ProjectPermission projectPermission) {
        try {
            org.springframework.web.context.request.RequestAttributes attributes = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            log.info("[DEBUG] RequestAttributes present: {}", attributes != null);
            if (attributes instanceof org.springframework.web.context.request.ServletRequestAttributes servletAttributes) {
                jakarta.servlet.http.HttpServletRequest request = servletAttributes.getRequest();
                log.info("[DEBUG] Authorization header: {}", request.getHeader("Authorization"));
            }
            log.info("[DEBUG] Current Authentication: {}", org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication());
            
            boolean result = workspaceClient.checkPermission(projectId, projectPermission);
            log.info("[DEBUG] checkPermission result: {}", result);
            return result;
        } catch (FeignException.Unauthorized e) {
            log.warn("Token expired or invalid during permission check for project: {}", projectId);
            throw new CredentialsExpiredException("JWT token is expired or invalid");
        } catch (FeignException e) {
            log.error("Workspace-service failed during permission check: {}", e.getMessage());
            return false;
        }
    }

    public boolean canViewProject(Long projectId) {
        return hasPermission(projectId, ProjectPermission.VIEW);
    }

    public boolean canEditProject(Long projectId) {
        return hasPermission(projectId, ProjectPermission.EDIT);
    }

    public boolean canDeleteProject(Long projectId) {
        return hasPermission(projectId, ProjectPermission.DELETE);
    }

    public boolean canViewMembers(Long projectId) {
        return hasPermission(projectId, ProjectPermission.VIEW_MEMBERS);
    }

    public boolean canManageMembers(Long projectId) {
        return hasPermission(projectId, ProjectPermission.MANAGE_MEMBERS);
    }
}
