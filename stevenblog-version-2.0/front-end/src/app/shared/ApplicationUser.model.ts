export class AttemptLoginUser {
  username: '';
  password: '';
  rememberMe: '';
}

export class AuthedUser {
  username: string;
  roles: string[];
  token: string;
}
