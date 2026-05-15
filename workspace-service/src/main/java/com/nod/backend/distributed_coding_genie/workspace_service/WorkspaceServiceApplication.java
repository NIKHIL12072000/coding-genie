package com.nod.backend.distributed_coding_genie.workspace_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WorkspaceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkspaceServiceApplication.class, args);
		/*
		1. Changed common-lib imports in security config
		*/
	}

}
