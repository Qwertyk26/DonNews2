package donnews.ru.donnews.Presenters;

import donnews.ru.donnews.Models.RequestResponse;
import donnews.ru.donnews.Network.NewsInterface;
import rx.Observer;

/**
 * Created by antonnikitin on 27.03.17.
 */

public class NewsMixPresenter extends BasePresenter implements Observer<RequestResponse> {

    private NewsInterface mNewsInterface;

    public NewsMixPresenter(NewsInterface newsInterface) {
        mNewsInterface = newsInterface;
    }

    @Override
    public void onCompleted() {
        mNewsInterface.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mNewsInterface.onError(e.getMessage());
    }

    @Override
    public void onNext(RequestResponse newsItem) {
        mNewsInterface.onNews(newsItem);
    }

    public void getMixNews() {
        unSubscribeAll();
        subscribe(mNewsInterface.getNews(), NewsMixPresenter.this);
    }
}
