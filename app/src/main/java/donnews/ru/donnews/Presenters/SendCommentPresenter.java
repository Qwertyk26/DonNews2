package donnews.ru.donnews.Presenters;

import donnews.ru.donnews.Models.SignUpRespones;
import donnews.ru.donnews.Network.SendCommentInterface;
import rx.Observer;

/**
 * Created by antonnikitin on 20.04.17.
 */

public class SendCommentPresenter extends BasePresenter implements Observer<SignUpRespones> {
    private SendCommentInterface mSendCommentInterface;

    public SendCommentPresenter(SendCommentInterface sendCommentInterface) {
        mSendCommentInterface = sendCommentInterface;
    }

    @Override
    public void onCompleted() {
        mSendCommentInterface.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mSendCommentInterface.onError(e.getMessage());
    }

    @Override
    public void onNext(SignUpRespones signUpRespones) {
        mSendCommentInterface.onSendComment(signUpRespones);
    }
    public void addComment() {
        unSubscribeAll();
        subscribe(mSendCommentInterface.addComment(), SendCommentPresenter.this);
    }
}
