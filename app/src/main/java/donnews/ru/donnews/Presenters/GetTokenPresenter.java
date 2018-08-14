package donnews.ru.donnews.Presenters;

import donnews.ru.donnews.Models.SignUpRespones;
import donnews.ru.donnews.Network.GetTokenInterface;
import rx.Observer;

/**
 * Created by antonnikitin on 20.04.17.
 */

public class GetTokenPresenter extends BasePresenter implements Observer<SignUpRespones> {

    GetTokenInterface mGetTokenInterface;

    public GetTokenPresenter(GetTokenInterface getTokenInterface) {
        mGetTokenInterface = getTokenInterface;
    }
    @Override
    public void onCompleted() {
        mGetTokenInterface.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mGetTokenInterface.onError(e.getMessage());
    }

    @Override
    public void onNext(SignUpRespones signUpRespones) {
        mGetTokenInterface.onToken(signUpRespones);
    }
    public void getToken() {
        unSubscribeAll();
        subscribe(mGetTokenInterface.getToken(), GetTokenPresenter.this);
    }
}
