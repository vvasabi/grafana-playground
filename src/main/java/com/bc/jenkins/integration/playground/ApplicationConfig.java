package com.bc.jenkins.integration.playground;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ApplicationConfig {
    private GrafanaConfig grafana;
    private JenkinsConfig jenkins;

    public GrafanaConfig getGrafana() {
        return grafana;
    }

    public void setGrafana(GrafanaConfig grafana) {
        this.grafana = grafana;
    }

    public JenkinsConfig getJenkins() {
        return jenkins;
    }

    public void setJenkins(JenkinsConfig jenkins) {
        this.jenkins = jenkins;
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

    public String getJenkinsHost() {
        return jenkins.host;
    }

    public String getJenkinsUser() {
        return jenkins.user;
    }

    public String getJenkinsAuthToken() {
        return jenkins.authToken;
    }

    public String getJenkinsJobName() {
        return jenkins.jobName;
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

    private static class JenkinsConfig {
        private String host;
        private String user;
        private String authToken;
        private String jobName;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String jobName) {
            this.jobName = jobName;
        }
    }
}
