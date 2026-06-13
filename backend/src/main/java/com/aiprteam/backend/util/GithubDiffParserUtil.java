package com.aiprteam.backend.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class GithubDiffParserUtil {

    public List<String> extractChangedFiles(String diff) {
        if (diff == null || diff.isBlank()) {
            return List.of();
        }

        Set<String> files = new LinkedHashSet<>();
        for (String line : diff.split("\\R")) {
            if (line.startsWith("+++ b/")) {
                files.add(line.substring("+++ b/".length()).trim());
            } else if (line.startsWith("diff --git ")) {
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    String path = parts[2];
                    if (path.startsWith("a/")) {
                        path = path.substring(2);
                    }
                    files.add(path);
                }
            }
        }
        return new ArrayList<>(files);
    }
}
