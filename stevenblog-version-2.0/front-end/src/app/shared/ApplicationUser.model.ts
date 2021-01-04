export class AttemptLoginUser {
  username: string;
  password: string;
}

export class AuthedUser {
  username: string;
  roles: string[];
}

export class SignUpUser {
  username = '';
  password = '';
  confirmPassword = '';
  email = '';
  confirmEmail = '';
}
