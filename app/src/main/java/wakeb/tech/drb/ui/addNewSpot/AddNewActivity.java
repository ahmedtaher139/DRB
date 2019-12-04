package wakeb.tech.drb.ui.addNewSpot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.DefaultExceptionHandler;

public class AddNewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
    }


    @Override
    protected void init() {

    }


}
