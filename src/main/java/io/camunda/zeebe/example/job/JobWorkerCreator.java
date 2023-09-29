package io.camunda.zeebe.example.job;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.worker.JobWorker;
import java.time.Duration;
import java.util.Scanner;

public final class JobWorkerCreator {
    public static void main(final String[] args) {

        final String jobType = "orchestrate-something";

        try (final ZeebeClient client = MyZeebeClientBuilder.createZbclient()) {

            System.out.println("Opening job worker.");

            try (final JobWorker workerRegistration =
                         client
                                 .newWorker()
                                 .jobType(jobType)
                                 .handler(new ExampleJobHandler())
                                 .timeout(Duration.ofSeconds(10))
                                 .open()) {
                System.out.println("Job worker opened and receiving jobs.");

                // run until System.in receives exit command
                waitUntilSystemInput("exit");
            }
        }
    }

    private static void waitUntilSystemInput(final String exitCode) {
        try (final Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                final String nextLine = scanner.nextLine();
                if (nextLine.contains(exitCode)) {
                    return;
                }
            }
        }
    }

    private static class ExampleJobHandler implements JobHandler {
        @Override
        public void handle(final JobClient client, final ActivatedJob job) {
            // here: business logic that is executed with every job
            System.out.println(job);
            client.newCompleteCommand(job.getKey()).send().join();
        }
    }
}