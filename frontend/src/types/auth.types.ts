export interface AuthResponse {
  token: string;
  email: string;
  fullName: string;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload {
  fullName: string;
  email: string;
  password: string;
}

export interface AuthUser {
  email: string;
  fullName: string;
}
