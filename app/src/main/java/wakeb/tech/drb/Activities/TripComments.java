package wakeb.tech.drb.Activities;

import com.google.android.material.textfield.TextInputEditText;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

 import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import wakeb.tech.drb.Adapters.CommentsAdapter;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.Comment;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.PlaceSettingsListener;
import wakeb.tech.drb.Uitils.Refresh;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;


public class TripComments extends BaseActivity implements Refresh {


    @BindView(R.id.commentsTrips_SwipeRefreshLayout)
    SwipeRefreshLayout commentsTrips_SwipeRefreshLayout;

    @BindView(R.id.commentsTrips_NestedScrollView)
    NestedScrollView commentsTrips_NestedScrollView;

    @BindView(R.id.commentsTrips)
    RecyclerView commentsTrips;


    @BindView(R.id.commentsTrips_editText)
    TextInputEditText commentsTrips_editText;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @OnClick(R.id.send_comment)
    void send_comment() {
        if (TextUtils.isEmpty(commentsTrips_editText.getText().toString())) {
            Toast.makeText(this, getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
        } else {
            CommonUtilities.showStaticDialog(this,  "comments");
            Map<String, String> parms = new HashMap<>();
            parms.put("publishing_id", ItemID);
            parms.put("user_id", dataManager.getID());
            parms.put("body", commentsTrips_editText.getText().toString());
            myAPI.save_comment(parms)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ApiResponse apiResponse) {

                            CommonUtilities.hideDialog();

                            if (apiResponse.getStatus()) {

                                CommonUtilities.save_log(myAPI, dataManager.getID(), "COMMENT", "publsihing", ItemID);

                                get_comments();
                                commentsTrips_editText.setText("");
                            } else {
                                Toast.makeText(TripComments.this, apiResponse.getMsg() + "add", Toast.LENGTH_SHORT).show();
                            }
                            Log.w("printed gson => ", new GsonBuilder().setPrettyPrinting().create().toJson(apiResponse));


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(TripComments.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            Log.i("ERROR_RETROFIT", e.getMessage());
                            CommonUtilities.hideDialog();
                        }

                        @Override
                        public void onComplete() {

                            commentsAdapter.notifyDataSetChanged();
                        }
                    });
        }

    }




    CommentsAdapter commentsAdapter;
    ArrayList<Comment> comments;
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;
    String ItemID;

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
        setContentView(R.layout.activity_trip_comments);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                ItemID = null;
            } else {
                ItemID = extras.getString("ItemID");
            }
        } else {
            ItemID = (String) savedInstanceState.getSerializable("ItemID");

        }

        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        comments = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        commentsTrips.setLayoutManager(linearLayoutManager);
        commentsTrips_SwipeRefreshLayout.setRefreshing(true);
        commentsTrips_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_comments();
            }
        });
        get_comments();
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


    void get_comments() {

        CommonUtilities.showStaticDialog(this,  "comments");
        Map<String, String> parms = new HashMap<>();
        parms.put("publishing_id", ItemID);
        parms.put("user_id", dataManager.getID());
        myAPI.get_comments(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                        CommonUtilities.hideDialog();

                        if (apiResponse.getStatus()) {

                            commentsAdapter = new CommentsAdapter(TripComments.this, apiResponse.getData().getComments(), dataManager, myAPI);
                            commentsTrips.setAdapter(commentsAdapter);
                            commentsTrips.scrollToPosition(apiResponse.getData().getComments().size() - 1);

                        } else {
                            Toast.makeText(TripComments.this, apiResponse.getMsg() + "get", Toast.LENGTH_SHORT).show();
                        }

                        try {
                            commentsTrips_SwipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TripComments.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {

                        commentsAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void refresh() {
        get_comments();
    }
}
