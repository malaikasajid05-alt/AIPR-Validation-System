import { API_BASE_URL } from '@/utils/constants';
import { getWebhookKey, regenerateWebhookKey } from '@/utils/storage';

export function buildWebhookUrl(projectId: number) {
  const key = getWebhookKey(projectId);
  return `${API_BASE_URL.replace(/\/api$/, '')}/api/v1/webhooks/wh_${projectId}_${key}`;
}

export function buildBackendWebhookUrl() {
  return `${API_BASE_URL}/github/webhook`;
}

export function regenerateProjectWebhook(projectId: number) {
  regenerateWebhookKey(projectId);
  return buildWebhookUrl(projectId);
}
