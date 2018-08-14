package donnews.ru.donnews.Network;

import donnews.ru.donnews.Models.RequestResponse;
import rx.Observable;

/**
 * Created by antonnikitin on 27.03.17.
 */

public interface NewsInterface {
    void onCompleted();
    void onError(String message);
    void onNews(RequestResponse news);
    Observable<RequestResponse> getNews();
}
