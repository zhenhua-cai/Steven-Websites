package net.stevencai.stevenweb.frontendResource;

import lombok.Data;
import net.stevencai.stevenweb.validation.FieldMatch;
import net.stevencai.stevenweb.validation.ValidEmail;
import net.stevencai.stevenweb.validation.ValidPassword;
import net.stevencai.stevenweb.validation.ValidUsername;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldMatch.List(
        @FieldMatch(first = "password", second = "confirmPassword", message="The password fields must match.")
)
public class UserResource {
    @NotNull
    @Size(min=1,max=30, message ="{username.size}")
    @ValidUsername
    @Column
    private String username;

    @NotNull
    @ValidPassword(min=6,max= 30)
    @Column
    private String password;

    @NotNull
    @Column
    private String confirmPassword;

    @NotNull
    @ValidEmail(message="{email.valid}")
    @Size(min=1, message = "{email.valid}")
    @Column
    private String email;

    public UserResource() {
    }

    @Override
    public String toString() {
        return "UserResource{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
