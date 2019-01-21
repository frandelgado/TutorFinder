package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResetPasswordForm {

    @NotNull
    @Size(min = 8, max = 64)
    private String password;

    @NotNull
    @Size(min = 8, max = 64)
    private String repeatPassword;

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public boolean checkRepeatPassword() {
        if (repeatPassword == null || password == null)
            return false;
        return repeatPassword.equals(password);
    }
}
