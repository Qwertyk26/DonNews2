package donnews.ru.donnews.Presenters;

import donnews.ru.donnews.Models.SignUpRespones;
import donnews.ru.donnews.Network.SocauthInterface;
import rx.Observer;

/**
 * Created by antonnikitin on 20.04.17.
 */

public class SocAuthPresenter extends BasePresenter implements Observer<SignUpRespones> {
    SocauthInterface mSocauthInterface;

    public SocAuthPresenter(SocauthInterface socauthInterface) {
        mSocauthInterface = socauthInterface;
    }
    @Override
    public void onCompleted() {
        mSocauthInterface.onCompletedSocAuth();
    }

    @Override
    public void onError(Throwable e) {
        mSocauthInterface.onErrorSocAuth(e.getMessage());
    }

    @Override
    public void onNext(SignUpRespones signUpRespones) {
        mSocauthInterface.onSocauth(signUpRespones);
    }
    public void socAuth() {
        unSubscribeAll();
        subscribe(mSocauthInterface.socAuth(), SocAuthPresenter.this);
    }
}
