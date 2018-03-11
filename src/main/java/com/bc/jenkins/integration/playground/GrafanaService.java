package com.bc.jenkins.integration.playground;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GrafanaService {
    @Autowired
    private ApplicationConfig config;

    public void updateMessage(String message) {
        WebClient client = WebClient
            .builder()
            .baseUrl("http://" + config.getGrafanaHost())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.getGrafanaAuthToken())
            .build();
        ObjectNode node = client
            .get()
            .uri("/api/dashboards/uid/" + config.getGrafanaDashboardUid())
            .retrieve()
            .bodyToMono(ObjectNode.class)
            .block();
        if (!node.has("dashboard")) {
            throw new RuntimeException("Invalid response: " + node);
        }

        node.set("overwrite", BooleanNode.getTrue());
        node.remove("meta");
        ObjectNode dashboard = (ObjectNode) node.get("dashboard");
        dashboard.remove("version");
        if (!dashboard.has("panels")) {
            throw new RuntimeException("Invalid dashboard: " + dashboard);
        }

        ArrayNode panels = (ArrayNode) dashboard.get("panels");
        for (int i = 0; i < panels.size(); i++) {
            ObjectNode panel = (ObjectNode) panels.get(i);
            if (!"text".equals(panel.get("type").asText())) {
                continue;
            }

            panel.set("content", new TextNode("# Lorem ipsum\n" + message));
            break;
        }
        ClientResponse response = client
            .post()
            .uri("/api/dashboards/db")
            .body(BodyInserters.fromObject(node))
            .exchange()
            .block();
        System.out.println("Request complete with: " + response.statusCode().value());
    }
}
