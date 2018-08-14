package donnews.ru.donnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Models.SignUpRespones;
import donnews.ru.donnews.Network.GetTokenInterface;
import donnews.ru.donnews.Network.NetworkClient;
import donnews.ru.donnews.Network.SocauthInterface;
import donnews.ru.donnews.Presenters.GetTokenPresenter;
import donnews.ru.donnews.Presenters.SocAuthPresenter;
import rx.Observable;

/**
 * Created by antonnikitin on 20.04.17.
 */

public class SocAuthActivity extends AppCompatActivity implements GetTokenInterface, SocauthInterface {
    @Inject
    NetworkClient mNetworkClient;
    GetTokenPresenter mGetTokenPresenter;
    SocAuthPresenter mSocAuthPresenter;
    @Bind(R.id.webView) WebView mWebView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    String socauth;
    String mDnToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soc_auth);
        ((AppApplication) getApplicationContext()).getApiComponent().inject(this);
        ButterKnife.bind(this);
        socauth = getIntent().getStringExtra("socnetwork");
        mGetTokenPresenter = new GetTokenPresenter(this);
        mGetTokenPresenter.onCreate();
        mGetTokenPresenter.getToken();
        mSocAuthPresenter = new SocAuthPresenter(this);
        mSocAuthPresenter.onCreate();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("authorize_complete")) {
                    mSocAuthPresenter.socAuth();
                    mProgressBar.setVisibility(View.VISIBLE);
                    mWebView.stopLoading();
                }
                return false;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void onCompleted() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, "Во время загрузки произошла ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToken(SignUpRespones news) {
        mDnToken = news.getResult().getDn_token();
        if (socauth.contains("vk")) {
            mWebView.loadUrl("http://oauth.vk.com/authorize?client_id=5361673&redirect_uri=http://donnews.ru/socauth/vk/?dn_token=" + news.getResult().getDn_token() + "&response_type=code&scope=email\"");
        } else if (socauth.contains("fb")) {
            mWebView.loadUrl("https://www.facebook.com/dialog/oauth?client_id=1587149864941791&redirect_uri=http://donnews.ru/socauth/fb/?dn_token=" + news.getResult().getDn_token() + "&response_type=code&scope=email");
        } else if (socauth.contains("ok")) {
            mWebView.loadUrl("https://www.odnoklassniki.ru/oauth/authorize?client_id=1246575872&redirect_uri=http://donnews.ru/socauth/ok/?dn_token="+ news.getResult().getDn_token() +"&response_type=code&scope=email");
        } else if (socauth.contains("tw")) {
            mWebView.loadUrl("http://donnews.ru/socauth/tw/?dn_token=" + news.getResult().getDn_token());
        }
    }

    @Override
    public Observable<SignUpRespones> getToken() {
        return mNetworkClient.getToken();
    }

    @Override
    public void onCompletedSocAuth() {
        mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onErrorSocAuth(String message) {
        mProgressBar.setVisibility(View.GONE);
        finish();
    }

    @Override
    public void onSocauth(SignUpRespones result) {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("auth", true);
        editor.putString("username", result.getResult().getName());
        editor.putString("dn_token", mDnToken);
        editor.apply();
        NavigationView navigationView = (NavigationView) MainActivity.ma.findViewById(R.id.nav_view);
        navigationView.getHeaderView(0).setVisibility(View.VISIBLE);
        TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        username.setText(result.getResult().getName());
    }

    @Override
    public Observable<SignUpRespones> socAuth() {
        return mNetworkClient.socAuth(mDnToken);
    }
}
