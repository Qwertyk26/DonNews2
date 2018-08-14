package donnews.ru.donnews.Presenters;

import donnews.ru.donnews.Models.RequestResponse;
import donnews.ru.donnews.Network.SearchInterface;
import rx.Observer;

/**
 * Created by antonnikitin on 12.04.17.
 */

public class SearchPresenter extends BasePresenter implements Observer<RequestResponse> {
    private SearchInterface mSearchInterface;

    public SearchPresenter(SearchInterface searchInterface) {
        mSearchInterface = searchInterface;
    }
    @Override
    public void onCompleted() {
        mSearchInterface.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mSearchInterface.onError(e.getMessage());
    }

    @Override
    public void onNext(RequestResponse requestResponse) {
        mSearchInterface.onSearch(requestResponse);
    }
    public void searchQuery() {
        unSubscribeAll();
        subscribe(mSearchInterface.search(), SearchPresenter.this);
    }
}
