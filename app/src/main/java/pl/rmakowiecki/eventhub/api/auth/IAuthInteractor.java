package pl.rmakowiecki.eventhub.api.auth;

public interface IAuthInteractor {
    void loginUserWithEmail(String email, String password);

    void registerUserWithEmail(String email, String password);
}
