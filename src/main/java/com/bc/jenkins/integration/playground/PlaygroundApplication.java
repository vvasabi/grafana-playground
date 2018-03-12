package com.bc.jenkins.integration.playground;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.stream.IntStream;

@SpringBootApplication
public class PlaygroundApplication implements CommandLineRunner {
    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private GrafanaService grafanaService;

    @Autowired
    private JenkinsService jenkinsService;

    @Override
    public void run(String... args) throws InterruptedException {
        System.out.println("Hello world");
        System.out.println("Number of args: " + args.length);
        IntStream.range(0, args.length)
            .forEach(i -> System.out.println(i + ": " + args[i]));
//        grafanaService.updateMessage("foo bar" + (args.length >  ? " " + args[0] : ""));
        jenkinsService.scheduleBuild("test", args[0]);
        Thread.sleep(100);
        String lastBuild = jenkinsService.getBuilds().get(0);
        ObjectNode build = jenkinsService.getBuild(lastBuild);
        while (build.get("building").asBoolean()) {
            System.out.println("Build " + lastBuild + " is still building...");
            Thread.sleep(500);
            build = jenkinsService.getBuild(lastBuild);
        }
        ArrayNode actions = (ArrayNode) build.get("actions");
        for (JsonNode node : actions) {
            if (node.has("parameters")) {
                ArrayNode params = (ArrayNode) node.get("parameters");
                for (JsonNode param : params) {
                    System.out.println("Param " + param.get("name") + ": " + param.get("value"));
                }
                break;
            }
        }
        SpringApplication.exit(appContext, () -> 0);
    }

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundApplication.class, args);
    }
}
