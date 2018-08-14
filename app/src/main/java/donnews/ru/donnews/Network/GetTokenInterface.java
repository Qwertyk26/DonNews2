package donnews.ru.donnews.Network;

import donnews.ru.donnews.Models.SignUpRespones;
import rx.Observable;

/**
 * Created by antonnikitin on 20.04.17.
 */

public interface GetTokenInterface {
    void onCompleted();
    void onError(String message);
    void onToken(SignUpRespones news);
    Observable<SignUpRespones> getToken();
}
