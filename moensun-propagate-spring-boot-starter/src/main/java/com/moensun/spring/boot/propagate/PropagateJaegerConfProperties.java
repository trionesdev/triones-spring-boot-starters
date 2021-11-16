package com.moensun.spring.boot.propagate;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("opentracing.jaeger")
public class PropagateJaegerConfProperties {
    private Reporter reporter;

    public Reporter getReporter() {
        return reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public static class Reporter{
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
