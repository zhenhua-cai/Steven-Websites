package net.stevencai.blog.backend.clientResource;

import lombok.Data;
import net.stevencai.blog.backend.validation.FieldMatch;
import net.stevencai.blog.backend.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match."),
})
public class ResetPasswordObj {
    @NotNull
    @Email
    private String email;

    @NotNull
    @ValidPassword(min = 8, max = 30)
    private String password;

    @NotNull
    private String confirmPassword;
}
