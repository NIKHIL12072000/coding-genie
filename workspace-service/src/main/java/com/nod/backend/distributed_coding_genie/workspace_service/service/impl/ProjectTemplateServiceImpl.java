package com.nod.backend.distributed_coding_genie.workspace_service.service.impl;

import com.nod.backend.distributed_coding_genie.common_lib.error.ResourceNotFoundException;
import com.nod.backend.distributed_coding_genie.workspace_service.entity.Project;
import com.nod.backend.distributed_coding_genie.workspace_service.entity.ProjectFile;
import com.nod.backend.distributed_coding_genie.workspace_service.repository.ProjectFileRepository;
import com.nod.backend.distributed_coding_genie.workspace_service.repository.ProjectRepository;
import com.nod.backend.distributed_coding_genie.workspace_service.service.ProjectTemplateService;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectTemplateServiceImpl implements ProjectTemplateService {

    private final MinioClient minioClient;
    private final ProjectFileRepository projectFileRepository;
    private final ProjectRepository projectRepository;

    private static final String TEMPLATE_BUCKET = "starter-projects";
    private static final String TARGET_BUCKET = "projects";
    private static final String TEMPLATE_NAME = "react-vite-tailwind-daisyui-starter";


    @Override
    public void initializeProjectFromTemplate(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project", projectId.toString()));

        try {
            boolean starterBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(TEMPLATE_BUCKET).build());
            if (!starterBucketExists) {
                log.error("Starter projects bucket '{}' does not exist", TEMPLATE_BUCKET);
                throw new RuntimeException("Template storage not initialized");
            }

            boolean targetBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(TARGET_BUCKET).build());
            if (!targetBucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(TARGET_BUCKET).build());
                log.info("Created target bucket '{}'", TARGET_BUCKET);
            }

            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(TEMPLATE_BUCKET)
                            .prefix(TEMPLATE_NAME + "/")
                            .recursive(true)
                            .build()
            );

            List<ProjectFile> filesToSave = new ArrayList<>();

            boolean filesFound = false;
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir() || item.objectName().endsWith("/")) {
                    continue;
                }
                filesFound = true;
                String sourceKey = item.objectName();

                String cleanPath = sourceKey.replaceFirst(TEMPLATE_NAME + "/", "");
                String destKey = projectId + "/" + cleanPath;

                minioClient.copyObject(
                        CopyObjectArgs.builder()
                                .bucket(TARGET_BUCKET)
                                .object(destKey)
                                .source(
                                        CopySource.builder()
                                                .bucket(TEMPLATE_BUCKET)
                                                .object(sourceKey)
                                                .build()
                                )
                                .build()
                );

                ProjectFile pf = ProjectFile.builder()
                        .project(project)
                        .path(cleanPath)
                        .minioObjectKey(destKey)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build();

                filesToSave.add(pf);
            }

            if (!filesFound) {
                log.error("No template files found for template '{}' in bucket '{}'", TEMPLATE_NAME, TEMPLATE_BUCKET);
                throw new RuntimeException("Template files not found");
            }

            projectFileRepository.saveAll(filesToSave);

        } catch (Exception e) {
            log.error("Project initialization failed for project {}: {}", projectId, e.getMessage(), e);
            String msg = e.getMessage() != null && !e.getMessage().isEmpty() ? ": " + e.getMessage() : "";
            throw new RuntimeException("Failed to initialize project from template" + msg, e);
        }

    }
}





















