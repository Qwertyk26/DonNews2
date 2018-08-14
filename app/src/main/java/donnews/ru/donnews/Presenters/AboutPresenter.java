package donnews.ru.donnews.Presenters;

import donnews.ru.donnews.Models.AboutResonse;
import donnews.ru.donnews.Network.AboutInterface;
import rx.Observer;

/**
 * Created by antonnikitin on 06.04.17.
 */

public class AboutPresenter extends BasePresenter implements Observer<AboutResonse> {

    private AboutInterface mAboutInterface;

    public AboutPresenter(AboutInterface aboutInterface) {
        mAboutInterface = aboutInterface;
    }

    @Override
    public void onCompleted() {
        mAboutInterface.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mAboutInterface.onError(e.getMessage());
    }

    @Override
    public void onNext(AboutResonse requestResponse) {
        mAboutInterface.onAbout(requestResponse);
    }
    public void getAbout() {
        unSubscribeAll();
        subscribe(mAboutInterface.getAbout(), AboutPresenter.this);
    }
}
