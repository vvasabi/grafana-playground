package com.bc.jenkins.integration.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.stream.IntStream;

@SpringBootApplication
public class PlaygroundApplication implements CommandLineRunner {
    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private GrafanaService grafanaService;

    @Override
    public void run(String... args) {
        System.out.println("Hello world");
        System.out.println("Number of args: " + args.length);
        IntStream.range(0, args.length)
            .forEach(i -> System.out.println(i + ": " + args[i]));
        grafanaService.updateMessage("foo bar" + (args.length > 0 ? " " + args[0] : ""));
        SpringApplication.exit(appContext, () -> 0);
    }

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundApplication.class, args);
    }
}
