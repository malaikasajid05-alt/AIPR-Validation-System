package com.aiprteam.backend.util;

import com.aiprteam.backend.exception.GithubException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Parses and normalizes GitHub repository URLs (https/http, optional www, optional .git, extra path segments).
 */
public final class GithubRepoUrlUtil {

    public static String extractFullName(String raw) {
        String normalized = normalize(raw);
        String prefix = "https://github.com/";
        if (!normalized.regionMatches(true, 0, prefix, 0, prefix.length())) {
            throw new GithubException("Invalid GitHub repository URL");
        }
        String tail = normalized.substring(prefix.length());
        String[] parts = tail.split("/", 3);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new GithubException("Invalid GitHub repository URL");
        }
        return parts[0].toLowerCase() + "/" + parts[1].toLowerCase();
    }

    public static String normalize(String raw) {
        if (raw == null) {
            return "";
        }
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        String candidate = trimmed;
        if (!candidate.toLowerCase().startsWith("http://") && !candidate.toLowerCase().startsWith("https://")) {
            candidate = "https://" + candidate;
        }
        try {
            URI uri = URI.create(candidate);
            String host = uri.getHost();
            if (host == null) {
                throw new GithubException("Invalid GitHub repository URL");
            }
            String hostLower = host.toLowerCase();
            if (hostLower.startsWith("www.")) {
                hostLower = hostLower.substring(4);
            }
            if (!"github.com".equals(hostLower)) {
                throw new GithubException("Only github.com repository URLs are supported");
            }
            String path = uri.getPath();
            if (path == null || path.isBlank()) {
                throw new GithubException("Invalid GitHub repository URL");
            }
            List<String> segments =
                    Arrays.stream(path.split("/")).filter(segment -> !segment.isBlank()).toList();
            if (segments.size() < 2) {
                throw new GithubException("Invalid GitHub repository URL");
            }
            String owner = segments.get(0);
            String repo = segments.get(1);
            if (owner.isBlank() || repo.isBlank()) {
                throw new GithubException("Invalid GitHub repository URL");
            }
            if (repo.endsWith(".git")) {
                repo = repo.substring(0, repo.length() - 4);
            }
            if (owner.isBlank() || repo.isBlank()) {
                throw new GithubException("Invalid GitHub repository URL");
            }
            return "https://github.com/" + owner.toLowerCase() + "/" + repo.toLowerCase();
        } catch (GithubException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new GithubException("Invalid GitHub repository URL", ex);
        }
    }

    private GithubRepoUrlUtil() {}
}
