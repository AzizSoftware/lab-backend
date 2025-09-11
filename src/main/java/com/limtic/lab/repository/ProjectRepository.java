package com.limtic.lab.repository;

import com.limtic.lab.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ProjectRepository extends MongoRepository<Project, String> {
    Optional<Project> findByProjectName(String projectName);
}
