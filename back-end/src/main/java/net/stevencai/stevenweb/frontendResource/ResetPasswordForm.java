package net.stevencai.stevenweb.frontendResource;

import lombok.Data;
import net.stevencai.stevenweb.validation.FieldMatch;
import net.stevencai.stevenweb.validation.ValidPassword;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldMatch.List(
        @FieldMatch(first = "password", second = "confirmPassword", message="The password fields must match.")
)
public class ResetPasswordForm {

    @NotNull
    @ValidPassword(min=6,max= 30)
    @Column
    private String password;

    @NotNull
    @Column
    private String confirmPassword;

    @NotNull
    @Size(min=1, message="{reset.password.token}")
    private String token;

}
