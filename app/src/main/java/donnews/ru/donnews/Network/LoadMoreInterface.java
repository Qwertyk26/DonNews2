package donnews.ru.donnews.Network;

import donnews.ru.donnews.Models.RequestResponse;
import rx.Observable;

/**
 * Created by antonnikitin on 02.04.17.
 */

public interface LoadMoreInterface {
    void onCompletedLoadMore();
    void onErrorLoadMore(String message);
    void onLoadMore(RequestResponse news);
    Observable<RequestResponse> loadMore();
}
