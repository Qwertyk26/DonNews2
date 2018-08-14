package donnews.ru.donnews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class SignUpActivity extends AppCompatActivity implements SignUpInterface {
    @Inject
    NetworkClient mNetworkClient;
    SignUpPresenter mSignUpPresenter;
    @Bind(R.id.toolBar)
    Toolbar mToolbar;
    @Bind(R.id.email)
    EditText mEmailEditText;
    @Bind(R.id.login) EditText mLoginEditText;
    @Bind(R.id.password) EditText mPasswordEditText;
    @Bind(R.id.password2) EditText mPasswordEditText2;
    @Bind(R.id.signUpBtn)
    Button mSignUpBtn;
    ProgressDialog mProgressDialog;
    Helper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ((AppApplication) getApplicationContext()).getApiComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Пожалуйста подождите...");
        mSignUpPresenter = new SignUpPresenter(this);
        mSignUpPresenter.onCreate();
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                mSignUpPresenter.signUp();
            }
        });
        mHelper = new Helper();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSignUpPresenter.onDestroy();
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
        SharedPreferences mSharedPreferences = getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("auth", true);
        editor.putString("username", mLoginEditText.getText().toString());
        editor.putString("email", mEmailEditText.getText().toString());
        editor.putString("password", mPasswordEditText.getText().toString());
        editor.apply();
        mProgressDialog.dismiss();
        mHelper.showToast(this, "Для завершения регистрации активируйте аккаунт, интсрукция была выслана на указанный email");
    }

    @Override
    public void onError(String message) {
        mProgressDialog.dismiss();
        mHelper.showToast(this, "Во время загрузки произошла ошибка");
    }

    @Override
    public void onSignUp(SignUpRespones result) {
        mProgressDialog.dismiss();
        mHelper.showToast(this, result.getResult().getMessage());
    }

    @Override
    public Observable<SignUpRespones> signUp() {
        return mNetworkClient.signUpRequest(mEmailEditText.getText().toString(), mLoginEditText.getText().toString(), mPasswordEditText.getText().toString(), mPasswordEditText2.getText().toString());
    }
}
