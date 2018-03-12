package com.bc.jenkins.integration.playground;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JenkinsService {
    private WebClient client;

    @Autowired
    private ApplicationConfig config;

    @PostConstruct
    public void init() {
        client = WebClient
            .builder()
            .baseUrl("http://" + config.getJenkinsHost())
            .filter(ExchangeFilterFunctions
                .basicAuthentication(config.getJenkinsUser(), config.getJenkinsAuthToken()))
            .build();
    }

    public List<String> getBuilds() {
        ObjectNode node = client
            .get()
            .uri("/job/" + config.getJenkinsJobName() + "/api/json")
            .retrieve()
            .bodyToMono(ObjectNode.class)
            .block();
        ArrayNode builds = (ArrayNode) node.get("builds");
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(builds.iterator(), Spliterator.ORDERED), false)
            .map(jsonNode -> jsonNode.get("number").asText())
            .collect(Collectors.toList());
    }

    public ObjectNode getBuild(String buildNumber) {
        return client
            .get()
            .uri("/job/" + config.getJenkinsJobName() + "/" + buildNumber + "/api/json")
            .retrieve()
            .bodyToMono(ObjectNode.class)
            .block();
    }

    public void scheduleBuild(String name, String body) {
        ObjectNode crumb = getCrumb();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("filename", name);
        formData.add("file_body", body);
        formData.add(crumb.get("crumbRequestField").asText(), crumb.get("crumb").asText());
        ClientResponse response = client
            .post()
            .uri("/job/" + config.getJenkinsJobName() + "/buildWithParameters")
            .body(BodyInserters.fromFormData(formData))
            .exchange()
            .block();
        if (!response.statusCode().is2xxSuccessful()) {
            throw new RuntimeException("Invalid response: " + response.bodyToMono(String.class).block());
        }
    }

    private ObjectNode getCrumb() {
        return client
            .get()
            .uri("/crumbIssuer/api/json")
            .retrieve()
            .bodyToMono(ObjectNode.class)
            .block();
    }
}
