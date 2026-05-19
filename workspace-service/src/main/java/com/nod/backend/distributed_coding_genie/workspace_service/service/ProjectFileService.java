package com.nod.backend.distributed_coding_genie.workspace_service.service;


import com.nod.backend.distributed_coding_genie.common_lib.dto.FileTreeDto;

public interface ProjectFileService {
    FileTreeDto getFileTree(Long projectId);

    String getFileContent(Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
