package com.limtic.lab.repository;

import com.limtic.lab.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

    // -------------------- DASHBOARD / UPCOMING --------------------
    List<Project> findByStartDateGreaterThanEqual(LocalDate date);

    // -------------------- SEARCH / FILTER --------------------
    Optional<Project> findByProjectName(String projectName);
    List<Project> findByProjectNameContainingIgnoreCase(String projectName);
    List<Project> findByStatus(String status);
    List<Project> findByBudgetBetween(double min, double max);
    List<Project> findByStartDateAfter(LocalDate start);
    List<Project> findByEndDateBefore(LocalDate end);
    List<Project> findByStartDateBetween(LocalDate start, LocalDate end);

    // -------------------- COUNT --------------------
    long count();
}
