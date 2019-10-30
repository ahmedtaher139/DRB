package wakeb.tech.drb.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import java.util.Locale;

import butterknife.BindView;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;

public class SelectLanguage extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.act_change_lang_btn_english)
    TextView btnEnglish;

    @BindView(R.id.act_change_lang_btn_arabic)
    TextView btnArabic;

    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        init();
        dataManager = ((MainApplication) getApplication()).getDataManager();
        setViewListeners();
    }

    @Override
    protected void setViewListeners() {
        btnArabic.setOnClickListener(this);
        btnEnglish.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }



    @Override
    protected boolean isValidData() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_change_lang_btn_english:
                dataManager.saveLang("en");
                dataManager.saveLangStatus(true);


                updateLocale(new Locale("en", "US"));
                startActivity(new Intent(SelectLanguage.this, WelcomeScreen.class));
                finish();
                break;
            case R.id.act_change_lang_btn_arabic:
                dataManager.saveLang("ar");
                dataManager.saveLangStatus(true);


                updateLocale(new Locale("ar", "EG"));
                startActivity(new Intent(SelectLanguage.this, WelcomeScreen.class));
                finish();
                break;
        }
    }
}
