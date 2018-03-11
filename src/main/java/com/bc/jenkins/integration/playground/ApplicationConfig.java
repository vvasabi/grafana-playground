package com.bc.jenkins.integration.playground;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ApplicationConfig {
    private GrafanaConfig grafana;

    public GrafanaConfig getGrafana() {
        return grafana;
    }

    public void setGrafana(GrafanaConfig grafana) {
        this.grafana = grafana;
    }

    public String getGrafanaHost() {
        return grafana.host;
    }

    public String getGrafanaAuthToken() {
        return grafana.authToken;
    }

    public String getGrafanaDashboardUid() {
        return grafana.dashboardUid;
    }

    private static class GrafanaConfig {
        private String host;
        private String authToken;
        private String dashboardUid;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public String getDashboardUid() {
            return dashboardUid;
        }

        public void setDashboardUid(String dashboardUid) {
            this.dashboardUid = dashboardUid;
        }
    }
}
