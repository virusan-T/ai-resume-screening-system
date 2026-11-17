package com.resumeai.backend.job.repository;

import com.resumeai.backend.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByStatus(String status);

    List<Job> findByRecruiterId(Long recruiterId);
}