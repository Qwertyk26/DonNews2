package donnews.ru.donnews.Network;

import donnews.ru.donnews.Models.SignUpRespones;
import rx.Observable;

/**
 * Created by antonnikitin on 12.04.17.
 */

public interface SignUpInterface {
    void onCompleted();
    void onError(String message);
    void onSignUp(SignUpRespones result);
    Observable<SignUpRespones> signUp();
}
