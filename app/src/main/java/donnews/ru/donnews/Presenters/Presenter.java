package donnews.ru.donnews.Presenters;

/**
 * Created by antonnikitin on 27.03.17.
 */

public interface Presenter {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();
}
