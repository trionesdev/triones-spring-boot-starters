package com.moensun.spring.boot.propagate.scope;

import com.moensun.commons.context.actor.ActorConstants;
import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Scope;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import io.opentracing.util.ThreadLocalScopeManager;
import org.slf4j.MDC;

public class MSMDCScopeManager implements ScopeManager {
    private final ScopeManager wrappedScopeManager;
    private final String mdcTraceIdKey;
    private final String mdcSpanIdKey;
    private final String mdcSampledKey;
    private final String mdcTenantIdKey;
    private final String mdcActorIdKey;

    private MSMDCScopeManager(Builder builder) {
        this.wrappedScopeManager = builder.scopeManager;
        this.mdcTraceIdKey = builder.mdcTraceIdKey;
        this.mdcSpanIdKey = builder.mdcSpanIdKey;
        this.mdcSampledKey = builder.mdcSampledKey;
        this.mdcTenantIdKey = builder.mdcTenantIdKey;
        this.mdcActorIdKey = builder.mdcActorIdKey;
    }

    @Override
    public Scope activate(Span span) {
        return new MDCScope(wrappedScopeManager.activate(span), span);
    }

    @Override
    public Span activeSpan() {
        return wrappedScopeManager.activeSpan();
    }

    public static class Builder {
        private ScopeManager scopeManager = new ThreadLocalScopeManager();
        private String mdcTraceIdKey = "traceId";
        private String mdcSpanIdKey = "spanId";
        private String mdcSampledKey = "sampled";
        private String mdcTenantIdKey = ActorConstants.X_TENANT_ID;
        private String mdcActorIdKey = ActorConstants.X_ACTOR_ID;

        public Builder withScopeManager(ScopeManager scopeManager) {
            this.scopeManager = scopeManager;
            return this;
        }

        public Builder withMDCTraceIdKey(String mdcTraceIdKey) {
            this.mdcTraceIdKey = mdcTraceIdKey;
            return this;
        }

        public Builder withMDCSpanIdKey(String mdcSpanIdKey) {
            this.mdcSpanIdKey = mdcSpanIdKey;
            return this;
        }

        public Builder withMDCSampledKey(String mdcSampledKey) {
            this.mdcSampledKey = mdcSampledKey;
            return this;
        }

        public Builder withMDCTenantIdKey(String mdcTenantIdKey) {
            this.mdcTenantIdKey = mdcTenantIdKey;
            return this;
        }

        public Builder withMDCActorIdKey(String mdcActorIdKey) {
            this.mdcActorIdKey = mdcActorIdKey;
            return this;
        }

        public MSMDCScopeManager build() {
            return new MSMDCScopeManager(this);
        }

    }

    private class MDCScope implements Scope {
        private final Scope wrappedScope;
        private final String previousTraceId;
        private final String previousSpanId;
        private final String previousSampled;
        private final String previousTenantId;
        private final String previousActorId;

        /**
         * mdcScope.
         */
        MDCScope(Scope scope, Span span) {
            this.wrappedScope = scope;
            this.previousTraceId = MDC.get(mdcTraceIdKey);
            this.previousSpanId = MDC.get(mdcSpanIdKey);
            this.previousSampled = MDC.get(mdcSampledKey);
            this.previousTenantId = MDC.get(mdcTenantIdKey);
            this.previousActorId = MDC.get(mdcActorIdKey);

            if (span.context() instanceof JaegerSpanContext) {
                putContext((JaegerSpanContext) span.context());
            }
        }

        protected void putContext(JaegerSpanContext spanContext) {
            replace(mdcTraceIdKey, spanContext.toTraceId());
            replace(mdcSpanIdKey, spanContext.toSpanId());
            replace(mdcSampledKey, String.valueOf(spanContext.isSampled()));
            replace(mdcTenantIdKey, spanContext.getBaggageItem(ActorConstants.MDC_TENANT_ID));
            replace(mdcActorIdKey, spanContext.getBaggageItem(ActorConstants.MDC_ACTOR_ID));
        }

        private void replace(String key, String value) {
            if (value == null) {
                MDC.remove(key);
            } else {
                MDC.put(key, value);
            }
        }

        @Override
        public void close() {
            wrappedScope.close();
            replace(mdcTraceIdKey, previousTraceId);
            replace(mdcSpanIdKey, previousSpanId);
            replace(mdcSampledKey, previousSampled);
            replace(mdcTenantIdKey, previousTenantId);
            replace(mdcActorIdKey, previousActorId);
        }
    }
}
