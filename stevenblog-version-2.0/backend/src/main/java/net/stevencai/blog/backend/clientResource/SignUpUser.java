package net.stevencai.blog.backend.clientResource;

import lombok.Data;
import net.stevencai.blog.backend.validation.FieldMatch;
import net.stevencai.blog.backend.validation.ValidPassword;
import net.stevencai.blog.backend.validation.ValidUsername;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match."),
        @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match.")
})
public class SignUpUser {
    @NotNull
    @Size(min=6, max=30, message="username must be between 6 and 30 characters")
    @ValidUsername
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String confirmEmail;

    @NotNull
    @ValidPassword(min=8,max = 30)
    private String password;

    @NotNull
    private String confirmPassword;

}
