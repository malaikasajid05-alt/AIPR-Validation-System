/**
 * Normalize to https://github.com/owner/repo
 * (strips .git, tree paths, adds https if missing).
 */
export function normalizeGithubRepoUrl(raw: string): string {
  let input = raw.trim();

  if (!input) {
    return '';
  }

  if (!/^https?:\/\//i.test(input)) {
    input = `https://${input}`;
  }

  try {
    const url = new URL(input);

    const host = url.hostname
        .replace(/^www\./i, '')
        .toLowerCase();

    if (host !== 'github.com') {
      throw new Error('not github');
    }

    const segments = url.pathname
        .split('/')
        .filter(Boolean);

    if (segments.length < 2) {
      throw new Error('short path');
    }

    let owner = segments[0];
    let repo = segments[1];

    if (repo.endsWith('.git')) {
      repo = repo.slice(0, -4);
    }

    owner = decodeURIComponent(owner);
    repo = decodeURIComponent(repo);

    return `https://github.com/${owner.toLowerCase()}/${repo.toLowerCase()}`;
  } catch {
    return '';
  }
}

export function validateGithubRepoUrl(value: string): string {
  if (!value.trim()) {
    return 'GitHub repository URL is required.';
  }

  const normalized = normalizeGithubRepoUrl(value);

  if (!normalized) {
    return 'Enter a valid GitHub repository URL (for example https://github.com/org/repository).';
  }

  return '';
}

/* =========================
   General Validators
========================= */

export function validateRequired(value: string): string {
  if (!value || !value.trim()) {
    return 'This field is required.';
  }

  return '';
}

export function validateEmail(email: string): string {
  if (!email.trim()) {
    return 'Email is required.';
  }

  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!regex.test(email)) {
    return 'Enter a valid email address.';
  }

  return '';
}

export function validatePassword(password: string): string {
  if (!password.trim()) {
    return 'Password is required.';
  }

  if (password.length < 6) {
    return 'Password must be at least 6 characters.';
  }

  return '';
}

export function validateConfirmPassword(
    password: string,
    confirmPassword: string
): string {
  if (!confirmPassword.trim()) {
    return 'Please confirm your password.';
  }

  if (password !== confirmPassword) {
    return 'Passwords do not match.';
  }

  return '';
}