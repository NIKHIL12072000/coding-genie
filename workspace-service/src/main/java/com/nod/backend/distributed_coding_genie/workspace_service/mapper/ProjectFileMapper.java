package com.nod.backend.distributed_coding_genie.workspace_service.mapper;

import com.nod.backend.distributed_coding_genie.common_lib.dto.FileNode;
import com.nod.backend.distributed_coding_genie.workspace_service.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}
