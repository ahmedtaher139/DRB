package wakeb.tech.drb.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;

public class FilterMap extends BaseActivity {


    @BindView(R.id.switch_risks)
    Switch switch_risks;

    @OnClick(R.id.back_button)
    void back_button() {

        onBackPressed();


    }

    @BindView(R.id.switch_places)
    Switch switch_places;


    @BindView(R.id.switch_stores)
    Switch switch_stores;


    @OnClick(R.id.switch_risks)
    void switch_risks() {

        if (dataManager.getTurnOnRisksMarkers()) {
            dataManager.turnOnRisksMarkers(false);
            switch_risks.setChecked(false);
        } else {
            dataManager.turnOnRisksMarkers(true);
            switch_risks.setChecked(true);
        }
        CommonUtilities.showStaticDialog(this, "more");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CommonUtilities.hideDialog();
            }
        }, 2000);

    }

    ;


    @OnClick(R.id.switch_places)
    void switch_places() {

        if (dataManager.getTurnOnPlacesMarkers()) {
            dataManager.turnOnPlacesMarkers(false);
            switch_places.setChecked(false);
        } else {
            dataManager.turnOnPlacesMarkers(true);
            switch_places.setChecked(true);
        }
        CommonUtilities.showStaticDialog(this, "more");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CommonUtilities.hideDialog();
            }
        }, 2000);

    }

    ;


    @OnClick(R.id.switch_stores)
    void switch_stores() {

        if (dataManager.getTurnOnStoresMarkers()) {
            dataManager.turnOnStoresMarkers(false);
            switch_stores.setChecked(false);
        } else {
            dataManager.turnOnStoresMarkers(true);
            switch_stores.setChecked(true);
        }
        CommonUtilities.showStaticDialog(this, "more");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CommonUtilities.hideDialog();
            }
        }, 2000);

    }

    DataManager dataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable;
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                drawable = getResources().getDrawable(R.drawable.background_svg, getTheme());
            } else {
                drawable = VectorDrawableCompat.create(getResources(), R.drawable.background_svg, getTheme());
            }
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(drawable);
        } else {
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.drawable.background_png);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_map);
        dataManager = ((MainApplication) getApplication()).getDataManager();


        if (dataManager.getTurnOnRisksMarkers()) {
            switch_risks.setChecked(true);
        } else {
            switch_risks.setChecked(false);
        }

        if (dataManager.getTurnOnPlacesMarkers()) {
            switch_places.setChecked(true);
        } else {
            switch_places.setChecked(false);
        }

        if (dataManager.getTurnOnStoresMarkers()) {
            switch_stores.setChecked(true);
        } else {
            switch_stores.setChecked(false);
        }


    }

    @Override
    protected void setViewListeners() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean isValidData() {
        return false;
    }
}
