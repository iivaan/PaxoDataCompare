package com.paxovision.db.comparator.context;

import static java.lang.ThreadLocal.withInitial;
import com.paxovision.db.comparator.ComparisonConfig;
import com.paxovision.db.comparator.model.MapDiffResult;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("squid:S3066")
public enum TestContext {
    CONTEXT;
    private static final String PAYLOAD = "PAYLOAD";
    private static final String DIFFRESULT = "DIFFRESULT";
    private static final String CONFIG = "CONFIG";
    private final ThreadLocal<Map<String, Object>> testContexts = withInitial(HashMap::new);

    public <T> T get(String name) {
        return (T) testContexts.get().get(name);
    }

    public <T> T set(String name, T object) {
        testContexts.get().put(name, object);
        return object;
    }

    public MapDiffResult getDiffResult() {
        return get(DIFFRESULT);
    }

    public void setDiffResult(MapDiffResult diffResult) {
        set(DIFFRESULT, diffResult);
    }

    public ComparisonConfig getConfig() {
        return get(CONFIG);
    }

    public void setConfig(ComparisonConfig config) {
        set(CONFIG, config);
    }

    public Object getPayload() {
        return get(PAYLOAD);
    }

    public <T> T getPayload(Class<T> clazz) {
        return clazz.cast(get(PAYLOAD));
    }

    <T> void setPayload(T object) {
        set(PAYLOAD, object);
    }

    public void reset() {
        testContexts.get().clear();
    }



}
