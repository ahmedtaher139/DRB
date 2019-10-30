package wakeb.tech.drb.Activities;

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Registration.SplashScreen;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;

public class Settings extends BaseActivity {

    @BindView(R.id.changeLang_textView)
    TextView changeLang_textView;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.switch_notify)
    Switch switch_notify;

    @BindView(R.id.changeLang)
    RelativeLayout changeLang;

    @OnClick(R.id.switch_notify)
    void switch_notify(){

        if (dataManager.getTurnOnNotifications()) {
            dataManager.turnOnNotifications(false);
            switch_notify.setChecked(false);
        } else {
            dataManager.turnOnNotifications(true);
            switch_notify.setChecked(true);
        }
        CommonUtilities.showStaticDialog(this , "more");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CommonUtilities.hideDialog();
            }
        }, 2000);

    };


    @OnClick(R.id.changeLang)
    void changeLang(){


        LayoutInflater factory = LayoutInflater.from(Settings.this);
        final View languageDialogview = factory.inflate(R.layout.language_change_popup, null);
        if (languageDialog != null && languageDialog.isShowing()) {
            return;
        }

        languageDialog = new AlertDialog.Builder(Settings.this).create();
        languageDialog.setCancelable(false);
        languageDialog.setView(languageDialogview);
        languageDialog.show();
       /* languageDialog.getWindow()
                .findViewById(R.id.pop_up_language)
                .setBackgroundResource(android.R.color.transparent);*/
        languageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imvLogoChangeLanguage = (ImageView) languageDialogview.findViewById(R.id.imvLogoChangeLanguage);
        final RadioGroup radioGroup = (RadioGroup) languageDialogview.findViewById(R.id.myRadioGroup);
        final RadioButton rdbEnglish = (RadioButton) languageDialogview.findViewById(R.id.rdbEnglish);
        final RadioButton rdbArabic = (RadioButton) languageDialogview.findViewById(R.id.rdbArabic);
        Button dialogButton = (Button) languageDialogview.findViewById(R.id.btn_update);
        final String language = dataManager.getLang();

        if (language.equals("en")) {
            rdbArabic.setChecked(true);
        } else {
            rdbEnglish.setChecked(true);
        }
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rdbArabic.isChecked())
                {

                    updateLocale(new Locale("ar", "EG"));
                    dataManager.saveLang("ar");
                    startActivity(new Intent(Settings.this, SplashScreen.class));
                    finish();
                    languageDialog.dismiss();

                }
                else
                {

                    updateLocale(new Locale("en", "US"));
                    dataManager.saveLang("en");
                    startActivity(new Intent(Settings.this, SplashScreen.class));
                    finish();
                    languageDialog.dismiss();
                    startActivity(new Intent(Settings.this, SplashScreen.class));
                    finish();
                }

            }
        });

        languageDialogview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languageDialog != null)
                    languageDialog.dismiss();
            }
        });


    };

    DataManager dataManager;
    private AlertDialog languageDialog;

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
        }
        else
        {
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.drawable.background_png);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dataManager = ((MainApplication) getApplication()).getDataManager();


        if (dataManager.getTurnOnNotifications()) {
            switch_notify.setChecked(true);
        } else {
            switch_notify.setChecked(false);
        }


        final String language = dataManager.getLang();

        if (language.equals("en")) {
            changeLang_textView.setText("English");
        } else {
            changeLang_textView.setText("عربى");
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
