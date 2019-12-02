package wakeb.tech.drb.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class TextsView extends BaseActivity {


    @BindView(R.id.text)
    TextView text;


    @BindView(R.id.what_is_drb_layout)
    RelativeLayout what_is_drb_layout;

    @BindView(R.id.followUs_layout)
    LinearLayout followUs_layout;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.face_icon)
    void face_icon() {
        try {
            this.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + FACEBOOK));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + FACEBOOK));
            startActivity(intent);
        }
    }

    @OnClick(R.id.twitter_icon)
    void twitter_icon() {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=" + TWITTER));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + TWITTER));
        }
        this.startActivity(intent);
    }

    @OnClick(R.id.instagram_icon)
    void instagram_icon() {
        Uri uri = Uri.parse("http://instagram.com/_u/" + INSTAGRAM);


        Intent i = new Intent(Intent.ACTION_VIEW, uri);

        i.setPackage("com.instagram.android");

        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + INSTAGRAM)));
        }
    }

    String FLAG, FACEBOOK, TWITTER, INSTAGRAM;

    ApiServices myAPI;
    Retrofit retrofit;
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
        setContentView(R.layout.activity_texts_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.about_us));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        init();

            FLAG = getIntent().getStringExtra("FLAG");



        switch (FLAG) {
            case "TERMS":

                break;

            case "ABOUT":

                break;


        }

        get_settings();

    }



    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
    }



    void get_settings() {

        CommonUtilities.showStaticDialog(this, "textView");
        Map<String, String> parms = new HashMap<>();
        myAPI.get_settings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        switch (FLAG) {
                            case "TERMS":

                                if (dataManager.getLang().equals("en")) {
                                    text.setText(apiResponse.getData().getSettings().get(0).getTermsEn());
                                } else {
                                    text.setText(apiResponse.getData().getSettings().get(0).getTermsAr());
                                }

                                break;
                            case "ABOUT":
                                if (dataManager.getLang().equals("en")) {
                                    text.setText(apiResponse.getData().getSettings().get(0).getAboutEn());

                                } else {
                                    text.setText(apiResponse.getData().getSettings().get(0).getAboutAr());

                                }
                                what_is_drb_layout.setVisibility(View.VISIBLE);
                                followUs_layout.setVisibility(View.VISIBLE);
                                FACEBOOK = apiResponse.getData().getSettings().get(0).getFacebook();
                                TWITTER = apiResponse.getData().getSettings().get(0).getTwitter();
                                INSTAGRAM = apiResponse.getData().getSettings().get(0).getLinked();
                                break;


                        }


                        if (apiResponse.getStatus()) {

                        } else {
                            Toast.makeText(TextsView.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TextsView.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                        Log.i("ERROR_RETROFIT", e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                        CommonUtilities.hideDialog();
                    }
                });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
