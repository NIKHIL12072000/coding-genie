package com.nod.backend.distributed_coding_genie.common_lib.dto;

public record FileNode(
        String path) {

    @Override
    public String toString() {
        return path;
    }
}
