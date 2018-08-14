package donnews.ru.donnews.Network;

import donnews.ru.donnews.Models.RequestResponse;
import rx.Observable;

/**
 * Created by antonnikitin on 12.04.17.
 */

public interface SearchInterface {
    void onCompleted();
    void onError(String message);
    void onSearch(RequestResponse news);
    Observable<RequestResponse> search();
}
