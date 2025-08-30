package com.ai.traning.output;

import java.util.List;

public record CareerStep(
        String title,
        String industry,
        List<String> requiredSkills,
        List<String> recommendedResources) {
}
