package com.github.tablesheep233.permission.control.field.fortest.service;

import com.github.tablesheep233.permission.control.field.fortest.vo.ReportVO;
import com.github.tablesheep233.permission.control.field.jackson.annotation.JsonFieldControl;
import com.github.tablesheep233.permission.control.field.metadata.RootMetadata;
import com.github.tablesheep233.permission.control.field.metadata.event.MetadataPrepareEvent;
import com.github.tablesheep233.permission.control.field.metadata.storage.MetadataStorage;
import com.github.tablesheep233.permission.control.field.policy.ControlPolicyLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class MockControlPolicyService implements ControlPolicyLoader {

    private static final String DEFAULT_USER = "DEFAULT";

    private final Map<String, Map<String, Boolean>> flatUserPolicy = new HashMap<>();

    public void configUserPolicy(String key, Map<String, Boolean> flatPolicy) {
        flatUserPolicy.put(DEFAULT_USER + ":" + key, flatPolicy);
    }

    public Map<String, Boolean> loadPolicy(String key) {
        return flatUserPolicy.get(DEFAULT_USER + ":" + key);
    }

    @EventListener
    public void mockPolicy(MetadataPrepareEvent event) {
        MetadataStorage storage = event.getStorage();
        RootMetadata.MetaDataContent content = storage.load(ReportVO.class.getAnnotation(JsonFieldControl.class).name());
        Map<String, Boolean> flatPolicy = new HashMap<>();
        mockPolicy(content.getFields(), flatPolicy);
        for (String filterKey : flatPolicy.keySet()) {
            log.info("filter : " + filterKey);
        }
        configUserPolicy(content.getKey(), flatPolicy);
    }

    private void mockPolicy(List<RootMetadata.MetaDataContent> fields, Map<String, Boolean> flatPolicy) {
        for (RootMetadata.MetaDataContent field : fields) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                flatPolicy.put(field.getFullKey(), false);
            } else {
                if (field.isMulti()) {
                    mockPolicy(field.getFields(), flatPolicy);
                }
            }
        }
    }

    @Override
    public Map<String, Boolean> loadFlatPolicy(String key) {
        return loadPolicy(key);
    }
}
