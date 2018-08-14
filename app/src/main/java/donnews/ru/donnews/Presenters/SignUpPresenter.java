package donnews.ru.donnews.Presenters;

import donnews.ru.donnews.Models.SignUpRespones;
import donnews.ru.donnews.Network.SignUpInterface;
import rx.Observer;

/**
 * Created by antonnikitin on 12.04.17.
 */

public class SignUpPresenter extends BasePresenter implements Observer<SignUpRespones> {

    private SignUpInterface mSignUpInterface;

    public SignUpPresenter(SignUpInterface signUpInterface) {
        mSignUpInterface = signUpInterface;
    }
    @Override
    public void onCompleted() {
        mSignUpInterface.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mSignUpInterface.onError(e.getMessage());
    }

    @Override
    public void onNext(SignUpRespones signUpRespones) {
        mSignUpInterface.onSignUp(signUpRespones);
    }
    public void signUp() {
        unSubscribeAll();
        subscribe(mSignUpInterface.signUp(), SignUpPresenter.this);
    }
}
