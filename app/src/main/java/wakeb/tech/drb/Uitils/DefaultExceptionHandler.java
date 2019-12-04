package wakeb.tech.drb.Uitils;

import android.app.Activity;
import android.widget.Toast;

public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {


    Activity activity;

    public DefaultExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {

        Toast.makeText(activity, "CONNECTION ERROR", Toast.LENGTH_SHORT).show();

       // System.exit(0);
    }
}
