package com.example.pgbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@RestController
@RequestMapping("/jobs")
public class JobController {
    JobLauncher jobLauncher;
    Job importPersonJob;

    @Autowired
    public JobController(JobLauncher jobLauncher, Job importPersonJob) {
        this.jobLauncher = jobLauncher;
        this.importPersonJob = importPersonJob;
    }

    @PostMapping("/start")
    public void startJob () throws IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        String fakeFile = "Fake,Doe\n" +
                "Bake,Doe\n" +
                "Justin,Doe\n" +
                "Jane,Doe\n" +
                "John,Doe\n";

        File temp = File.createTempFile(Objects.requireNonNull("fake-file"), ".tmp");
        Files.copy(new ByteArrayInputStream(fakeFile.getBytes()), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        jobLauncher.run(importPersonJob,
                new JobParametersBuilder().addString("inputFilePath", temp.getAbsolutePath()
                ).toJobParameters());
    }
}
