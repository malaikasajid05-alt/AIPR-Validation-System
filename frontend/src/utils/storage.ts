import { WEBHOOK_STORAGE_KEY } from './constants';

type WebhookMap = Record<string, string>;

export function readWebhookKeys(): WebhookMap {
  const raw = localStorage.getItem(WEBHOOK_STORAGE_KEY);
  if (!raw) {
    return {};
  }
  try {
    return JSON.parse(raw) as WebhookMap;
  } catch {
    return {};
  }
}

export function writeWebhookKeys(map: WebhookMap) {
  localStorage.setItem(WEBHOOK_STORAGE_KEY, JSON.stringify(map));
}

export function getWebhookKey(projectId: number) {
  const map = readWebhookKeys();
  if (!map[String(projectId)]) {
    map[String(projectId)] = crypto.randomUUID().replace(/-/g, '').slice(0, 12);
    writeWebhookKeys(map);
  }
  return map[String(projectId)];
}

export function regenerateWebhookKey(projectId: number) {
  const map = readWebhookKeys();
  map[String(projectId)] = crypto.randomUUID().replace(/-/g, '').slice(0, 12);
  writeWebhookKeys(map);
  return map[String(projectId)];
}
