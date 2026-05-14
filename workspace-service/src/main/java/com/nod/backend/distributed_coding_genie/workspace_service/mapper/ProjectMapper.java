package com.nod.backend.distributed_coding_genie.workspace_service.mapper;

import com.nod.backend.distributed_coding_genie.common_lib.enums.ProjectRole;
import com.nod.backend.distributed_coding_genie.workspace_service.dto.project.ProjectResponse;
import com.nod.backend.distributed_coding_genie.workspace_service.dto.project.ProjectSummaryResponse;
import com.nod.backend.distributed_coding_genie.workspace_service.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    ProjectSummaryResponse toProjectSummaryResponse(Project project, ProjectRole role);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projects);

}
