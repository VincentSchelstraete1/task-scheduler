package com.vincents.taskscheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeEstimatorService {
    public final Map<String, Integer> keywordRules;



    public TimeEstimatorService() {
        keywordRules = new HashMap<>();
        keywordRules.put("essay", 180);
        keywordRules.put("project", 120);
        keywordRules.put("worksheet", 60);
        keywordRules.put("lab", 90);
    }

    public Integer estimateFromKeyWord(String title, String description) {

        String combined = (title + description).toLowerCase();
        for (String keyword : keywordRules.keySet()) {
            if (combined.contains(keyword)) {
                return keywordRules.get(keyword);
            }
        }
        return 60;
    }

    public List<Long> divideAcrossDays(int totalMinutes, Long days) {
        List<Long> chunks = new ArrayList<>();
        Long dailyAmount = totalMinutes / days;
        for (int i = 0; i < days; i++) {
            chunks.add(dailyAmount);
        }
        return chunks;
    }

}
