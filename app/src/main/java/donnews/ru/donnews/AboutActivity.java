package donnews.ru.donnews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

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

public class AboutActivity extends AppCompatActivity implements AboutInterface {
    @Inject
    NetworkClient mNetworkClient;
    AboutPresenter mAboutPresenter;
    @Bind(R.id.toolBar)
    Toolbar mToolbar;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.webView)
    WebView mWebView;
    @Bind(R.id.adView)
    AdView adView;

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
        adView.setBlockId("R-M-240930-2");
        adView.setAdSize(AdSize.BANNER_300x250);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdEventListener(new AdEventListener() {
            @Override
            public void onAdFailedToLoad(AdRequestError adRequestError) {
                Log.d("Ads error", adRequestError.getDescription());
            }

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdOpened() {

            }
        });
        adView.loadAd(adRequest);
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
    public void onDestroy() {
        super.onDestroy();
        mAboutPresenter.onDestroy();
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public void onCompleted() {
        mProgressBar.animate().alpha(0);
    }

    @Override
    public void onError(String message) {
        mProgressBar.animate().alpha(0);
        Toast.makeText(this, "Во время загрузки произошла ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAbout(AboutResonse about) {
        String html = "<html><head><link rel='stylesheet' type='text/css' href='about.css' /></head><body>" + about.getResult().getAbout() + "</body></html>";
        String mime = "text/html";
        String encoding = "utf-8";
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadDataWithBaseURL("file:///android_asset/", html, mime, encoding, null);
    }

    @Override
    public Observable<AboutResonse> getAbout() {
        return mNetworkClient.getAbout();
    }
}
