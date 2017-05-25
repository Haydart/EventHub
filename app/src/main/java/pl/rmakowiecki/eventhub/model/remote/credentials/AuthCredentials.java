package pl.rmakowiecki.eventhub.model.remote.credentials;

public final class AuthCredentials implements Credentials {
    private final String email;
    private final String password;
    private final String repeatedPassword;

    public AuthCredentials(String email, String password, String repeatedPassword) {
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }
}
