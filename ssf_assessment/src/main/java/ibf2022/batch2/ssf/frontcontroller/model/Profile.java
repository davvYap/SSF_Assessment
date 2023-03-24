package ibf2022.batch2.ssf.frontcontroller.model;

import java.io.Serializable;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Profile implements Serializable {
    @Size(min = 2, message = "Username must be at least 2 characters")
    private String username;

    @Size(min = 2, message = "Password must be at least 2 characters")
    private String password;

    private boolean authenticated = false;

    private int captchaAnswer;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public int getCaptchaAnswer() {
        return captchaAnswer;
    }

    public void setCaptchaAnswer(int captchaAnswer) {
        this.captchaAnswer = captchaAnswer;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("username", this.getUsername())
                .add("password", this.getPassword())
                .build();
    }

}
