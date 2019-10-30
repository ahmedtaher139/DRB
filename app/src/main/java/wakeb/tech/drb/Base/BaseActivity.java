package wakeb.tech.drb.Base;

import android.content.Context;
 import android.util.Log;
import android.view.MenuItem;


import androidx.appcompat.app.AppCompatActivity;

import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends LocaleAwareCompatActivity {




    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected abstract void setViewListeners();

    protected abstract void init ();

    abstract protected boolean isValidData();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
