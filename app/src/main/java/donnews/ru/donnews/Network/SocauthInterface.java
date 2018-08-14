package donnews.ru.donnews.Network;

import donnews.ru.donnews.Models.SignUpRespones;
import rx.Observable;

/**
 * Created by antonnikitin on 20.04.17.
 */

public interface SocauthInterface {
    void onCompletedSocAuth();
    void onErrorSocAuth(String message);
    void onSocauth(SignUpRespones result);
    Observable<SignUpRespones> socAuth();
}
