package donnews.ru.donnews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Helpers.Helper;
import donnews.ru.donnews.Models.SignUpRespones;
import donnews.ru.donnews.Network.NetworkClient;
import donnews.ru.donnews.Network.SignUpInterface;
import donnews.ru.donnews.Presenters.SignUpPresenter;
import rx.Observable;

/**
 * Created by antonnikitin on 12.04.17.
 */

public class AuthActivity extends AppCompatActivity implements SignUpInterface {
    @Inject
    NetworkClient mNetworkClient;
    SignUpPresenter mSignUpPresenter;
    @Bind(R.id.toolBar)
    Toolbar mToolbar;
    @Bind(R.id.login)
    EditText mLoginEditText;
    @Bind(R.id.password) EditText mPasswordEditText;
    @Bind(R.id.signUpText)
    TextView mSignUpTextView;
    @Bind(R.id.auth_btn)
    Button mAuthBtn;
    ProgressDialog mProgressDialog;
    @Bind(R.id.vk_auth)
    ImageView mVKAuthImage;
    @Bind(R.id.fb_auth)
    ImageView mFBAuth;
    @Bind(R.id.ok_auth)
    ImageView mOKAuth;
    @Bind(R.id.tw_auth)
    ImageView mTWAuth;
    Helper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ((AppApplication) getApplicationContext()).getApiComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mSignUpPresenter = new SignUpPresenter(this);
        mSignUpPresenter.onCreate();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Пожалуйста подождите...");
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(AuthActivity.this, SignUpActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        mAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                mSignUpPresenter.signUp();
            }
        });
        mVKAuthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(AuthActivity.this, SocAuthActivity.class);
                mIntent.putExtra("socnetwork", "vk");
                startActivityForResult(mIntent, 1);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        mFBAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(AuthActivity.this, SocAuthActivity.class);
                mIntent.putExtra("socnetwork", "fb");
                startActivityForResult(mIntent, 1);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        mOKAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(AuthActivity.this, SocAuthActivity.class);
                mIntent.putExtra("socnetwork", "ok");
                startActivityForResult(mIntent, 1);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        mTWAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(AuthActivity.this, SocAuthActivity.class);
                mIntent.putExtra("socnetwork", "tw");
                startActivityForResult(mIntent, 1);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        mHelper = new Helper();
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
        mProgressDialog.dismiss();
    }

    @Override
    public void onError(String message) {
        mProgressDialog.dismiss();
        mHelper.showToast(this, "Во время загрузки произошла ошибка");
        finish();
    }

    @Override
    public void onSignUp(SignUpRespones result) {
        if (result.getResult().isAuth()) {
            mProgressDialog.dismiss();
            SharedPreferences mSharedPreferences = getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("auth", true);
            editor.putString("username", mLoginEditText.getText().toString());
            editor.putString("email", "");
            editor.putString("password", mPasswordEditText.getText().toString());
            editor.putString("dn_token", result.getResult().getDn_token());
            editor.apply();
            NavigationView navigationView = (NavigationView) MainActivity.ma.findViewById(R.id.nav_view);
            navigationView.getHeaderView(0).setVisibility(View.VISIBLE);
            TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
            username.setText(mLoginEditText.getText().toString());
            finish();
        } else {
            mHelper.showToast(this, result.getResult().getMessage());
        }
    }

    @Override
    public Observable<SignUpRespones> signUp() {
        return mNetworkClient.loginRequest(mLoginEditText.getText().toString(), mPasswordEditText.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            finish();
        }
    }
}
