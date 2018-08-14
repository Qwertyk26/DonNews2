package donnews.ru.donnews.Presenters;

import donnews.ru.donnews.Models.RequestResponse;
import donnews.ru.donnews.Network.LoadMoreInterface;
import rx.Observer;

/**
 * Created by antonnikitin on 02.04.17.
 */

public class LoadMorePresenter extends BasePresenter implements Observer<RequestResponse> {

    private LoadMoreInterface mLoadMoreInterface;

    public LoadMorePresenter(LoadMoreInterface loadMoreInterface) {
        mLoadMoreInterface = loadMoreInterface;
    }
    @Override
    public void onCompleted() {
        mLoadMoreInterface.onCompletedLoadMore();
    }

    @Override
    public void onError(Throwable e) {
        mLoadMoreInterface.onErrorLoadMore(e.getMessage());
    }

    @Override
    public void onNext(RequestResponse requestResponse) {
        mLoadMoreInterface.onLoadMore(requestResponse);
    }
    public void loadMore() {
        unSubscribeAll();
        subscribe(mLoadMoreInterface.loadMore(), LoadMorePresenter.this);
    }
}
