package donnews.ru.donnews.Network;

import donnews.ru.donnews.Models.SignUpRespones;
import rx.Observable;

/**
 * Created by antonnikitin on 20.04.17.
 */

public interface SendCommentInterface {
    void onCompleted();
    void onError(String message);
    void onSendComment(SignUpRespones result);
    Observable<SignUpRespones> addComment();
}
