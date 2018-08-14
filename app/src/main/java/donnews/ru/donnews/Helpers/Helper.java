package donnews.ru.donnews.Helpers;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by antonnikitin on 23.05.17.
 */

public class Helper {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;

    public static String getTimeAgo(long time, Context ctx) {

        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "только что";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "минуту назад";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " минут назад";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1 час назад";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String dateString = simpleDateFormat.format(new Date(time)).toString();
            return dateString;
        }
    }
    public void showToast(Context mContext, String error) {
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
    }
}
