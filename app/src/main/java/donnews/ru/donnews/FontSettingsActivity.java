package donnews.ru.donnews;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by antonnikitin on 20.04.17.
 */

public class FontSettingsActivity extends AppCompatActivity {
    @Bind(R.id.toolBar)
    Toolbar mToolbar;
    @Bind(R.id.seekBar)
    SeekBar mSeekBar;
    @Bind(R.id.textNews)
    TextView mTextNewsText;
    @Bind(R.id.textSize) TextView mTextSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_settings);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Typeface typefaceLead = Typeface.createFromAsset(getAssets(), "PT_Sans-Caption-Web-Regular.ttf");
        mTextNewsText.setTypeface(typefaceLead);
        mTextSize.setTypeface(typefaceLead);
        SharedPreferences mSharedPreferences = getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSharedPreferences.getInt("textSize", 0) == 0) {
            mSeekBar.setProgress(2);
        } else {
            if (mSharedPreferences.getInt("textSize", 0) == 16) {
                mTextSize.setText("16");
                mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                mSeekBar.setProgress(0);
            } else if (mSharedPreferences.getInt("textSize", 0) == 17) {
                mTextSize.setText("17");
                mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                mSeekBar.setProgress(1);
            } else if (mSharedPreferences.getInt("textSize", 0) == 18) {
                mTextSize.setText("18");
                mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mSeekBar.setProgress(2);
            } else if (mSharedPreferences.getInt("textSize", 0) == 19) {
                mTextSize.setText("19");
                mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                mSeekBar.setProgress(3);
            } else if (mSharedPreferences.getInt("textSize", 0) == 20) {
                mTextSize.setText("20");
                mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                mSeekBar.setProgress(4);
            }
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SharedPreferences mSharedPreferences = getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                if (i == 0) {
                    mTextSize.setText("16");
                    mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    editor.putInt("textSize", 16);
                    editor.apply();
                } else if (i == 1) {
                    mTextSize.setText("17");
                    mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    editor.putInt("textSize", 17);
                    editor.apply();
                } else if (i == 2) {
                    mTextSize.setText("18");
                    mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    editor.putInt("textSize", 18);
                    editor.apply();
                } else if (i == 3) {
                    mTextSize.setText("19");
                    mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                    editor.putInt("textSize", 19);
                    editor.apply();
                } else if (i == 4) {
                    mTextSize.setText("20");
                    mTextNewsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    editor.putInt("textSize", 20);
                    editor.apply();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
}
