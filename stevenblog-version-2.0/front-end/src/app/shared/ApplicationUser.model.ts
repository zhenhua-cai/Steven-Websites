export class AttemptLoginUser {
  username: string;
  password: string;
}

export class AuthedUser {
  username: string;
  roles: number[];
}

export class SignUpUser {
  username = '';
  password = '';
  confirmPassword = '';
  email = '';
  confirmEmail = '';
}
