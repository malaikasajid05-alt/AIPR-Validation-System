package com.aiprteam.backend.util;

import com.aiprteam.backend.exception.GithubException;

/**
 * Splits GitHub {@code owner/repo} full names into path segments for REST API calls.
 */
public final class GithubRepoPathUtil {

    public record RepoPath(String owner, String repo) {}

    public static RepoPath parseRepoFullName(String repoFullName) {
        if (repoFullName == null || repoFullName.isBlank()) {
            throw new GithubException("Repository full name is required");
        }
        String trimmed = repoFullName.trim();
        int slash = trimmed.indexOf('/');
        if (slash <= 0 || slash == trimmed.length() - 1) {
            throw new GithubException("Invalid repository full name: " + repoFullName);
        }
        String owner = trimmed.substring(0, slash).trim();
        String repo = trimmed.substring(slash + 1).trim();
        if (owner.isEmpty() || repo.isEmpty()) {
            throw new GithubException("Invalid repository full name: " + repoFullName);
        }
        return new RepoPath(owner, repo);
    }

    private GithubRepoPathUtil() {}
}
