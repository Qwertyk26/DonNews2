package donnews.ru.donnews.Network;

import donnews.ru.donnews.Models.AboutResonse;
import rx.Observable;

/**
 * Created by antonnikitin on 06.04.17.
 */

public interface AboutInterface {
    void onCompleted();
    void onError(String message);
    void onAbout(AboutResonse about);
    Observable<AboutResonse> getAbout();
}
