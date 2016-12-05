package pl.euler.bgs.restapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Metrics metrics = new Metrics();

    private final Genapi genapi = new Genapi();

    private final Swagger swagger = new Swagger();

    private final Cache cache = new Cache();

    public Metrics getMetrics() {
        return metrics;
    }

    public Genapi getGenapi() {
        return genapi;
    }

    public Cache getCache() {
        return cache;
    }

    public static class Genapi {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Metrics {
        private final Logs logs = new Logs();

        public Logs getLogs() {
            return logs;
        }

        public static  class Logs {

            private boolean summaryTracking = false;

            private long reportFrequency = 60;

            public long getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(int reportFrequency) {
                this.reportFrequency = reportFrequency;
            }

            public boolean isSummaryTracking() {
                return summaryTracking;
            }

            public void setSummaryTracking(boolean summaryTracking) {
                this.summaryTracking = summaryTracking;
            }
        }
    }

    public static class Swagger {
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Cache {
        private int expireAfter = 600;

        public int getExpireAfter() {
            return expireAfter;
        }

        public void setExpireAfter(int expireAfter) {
            this.expireAfter = expireAfter;
        }
    }

}
