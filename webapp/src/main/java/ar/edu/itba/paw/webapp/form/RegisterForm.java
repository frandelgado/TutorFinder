package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterForm {

    @NotNull
    @Size(min = 1, max = 128)
    @Pattern(regexp = "[a-zA-Z]+")
    private String name;

    @NotNull
    @Size(min = 1, max = 128)
    @Pattern(regexp = "[a-zA-Z]+")
    private String lastname;

    @NotNull
    @Size(min = 1, max = 512)
    @Pattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    private String email;

    @NotNull
    @Size(min = 1, max = 128)
    private String username;

    @NotNull
    @Size(min = 1, max = 64)
    private String password;

    @NotNull
    @Size(min = 1, max = 64)
    private String repeatPassword;

    @NotNull
    @Size(min = 1, max = 512)
    private String description;

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public String getDescription() {
        return description;
    }
}
