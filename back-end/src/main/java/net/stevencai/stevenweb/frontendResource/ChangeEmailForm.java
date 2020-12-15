package net.stevencai.stevenweb.frontendResource;

import lombok.Data;
import net.stevencai.stevenweb.validation.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@FieldMatch.List(
        @FieldMatch(first = "newEmail", second = "confirmEmail", message="The email fields must match.")
)
public class ChangeEmailForm {

    @Email
    private String newEmail;

    @Email
    private String confirmEmail;

    @Email
    private String oldEmail;

}
