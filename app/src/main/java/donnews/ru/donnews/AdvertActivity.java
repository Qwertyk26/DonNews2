package donnews.ru.donnews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Models.AboutResonse;
import donnews.ru.donnews.Network.AboutInterface;
import donnews.ru.donnews.Network.NetworkClient;
import donnews.ru.donnews.Presenters.AboutPresenter;
import rx.Observable;

/**
 * Created by antonnikitin on 06.04.17.
 */

public class AdvertActivity extends AppCompatActivity implements AboutInterface {
    @Inject
    NetworkClient mNetworkClient;
    AboutPresenter mAboutPresenter;
    @Bind(R.id.toolBar)
    Toolbar mToolbar;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.webView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ((AppApplication) getApplicationContext()).getApiComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAboutPresenter = new AboutPresenter(this);
        mAboutPresenter.onCreate();
        mAboutPresenter.getAbout();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mAboutPresenter.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(String message) {
        mProgressBar.animate().alpha(0);
        Toast.makeText(this, "Во время загрузки произошла ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAbout(AboutResonse about) {
        String html = "<html><head><meta name='viewport' content='width=device-width' /><link rel='stylesheet' type='text/css' href='about.css' /></head><body>" + about.getResult().getAd() + "</body></html>";
        String mime = "text/html";
        String encoding = "utf-8";
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadDataWithBaseURL("file:///android_asset/", html, mime, encoding, null);
        mWebView.getSettings().setUseWideViewPort(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.animate().alpha(0);
            }
        });
    }

    @Override
    public Observable<AboutResonse> getAbout() {
        return mNetworkClient.getAdvert();
    }
}