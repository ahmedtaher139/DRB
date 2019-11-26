package wakeb.tech.drb.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import wakeb.tech.drb.Activities.TripComments;
import wakeb.tech.drb.Adapters.BlocksAdapter;
import wakeb.tech.drb.Adapters.CommentsAdapter;
import wakeb.tech.drb.Adapters.FollwingAdapter;
import wakeb.tech.drb.Adapters.LogsAdapter;
import wakeb.tech.drb.Adapters.NotificationsAdapter;
import wakeb.tech.drb.Adapters.UsersAdapter;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class FollowersList extends BaseActivity implements BlocksAdapter.Refreshing {


    @BindView(R.id.users_recycler)
    RecyclerView users_recycler;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.appBar_text)
    TextView appBar_text;

    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;


    @BindView(R.id.FOLLOWING_empty_list)
    RelativeLayout FOLLOWING_empty_list;

    @BindView(R.id.FOLLOWERS_empty_list)
    RelativeLayout FOLLOWERS_empty_list;

    @BindView(R.id.BLOCK_empty_list)
    RelativeLayout BLOCK_empty_list;

    @BindView(R.id.LOGS_empty_list)
    RelativeLayout LOGS_empty_list;

    @BindView(R.id.NOTIFICATIONS_empty_list)
    RelativeLayout NOTIFICATIONS_empty_list;

    String FLAG, ItemID , USER_FLAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_followres_list);
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        users_recycler.setLayoutManager(new LinearLayoutManager(this));

            FLAG = getIntent().getStringExtra("FLAG");
            ItemID = getIntent().getStringExtra("ItemID");



        if (dataManager.getID().equals(ItemID))
        {
            USER_FLAG = "ADMIN";
        }
        else
        {
            USER_FLAG = "USER";

        }

        if (FLAG.equals("FOLLOWING")) {
            get_followers(ItemID);
            appBar_text.setText(R.string.following);
        } else if (FLAG.equals("FOLLOWERS")) {
            get_following(ItemID);
            appBar_text.setText(R.string.followers);
        } else if (FLAG.equals("BLOCK")) {
            get_blocks();
            appBar_text.setText(R.string.blocks_list);
        } else if (FLAG.equals("LOGS")) {
            get_logs();
            appBar_text.setText(R.string.logs_list);
        } else if (FLAG.equals("NOTIFICATIONS")) {
            get_notifications();
            appBar_text.setText(R.string.notifications);
        }
    }



    @Override
    protected void init() {

    }



    void get_followers(String ID) {

        CommonUtilities.showStaticDialog(this, "getFollowers");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", ID);
        myAPI.followers_list(parms)
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

                            if (apiResponse.getData().getFollowers().size() == 0) {
                                FOLLOWING_empty_list.setVisibility(View.VISIBLE);
                            } else {
                                FOLLOWING_empty_list.setVisibility(View.GONE);

                            }


                            users_recycler.setAdapter(new FollwingAdapter(FollowersList.this, apiResponse.getData().getFollowers(), dataManager , myAPI , FollowersList.this , USER_FLAG));

                        } else {
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(FollowersList.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void get_following(String ID) {

        CommonUtilities.showStaticDialog(this, "getFollowing");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", ID);
        myAPI.follows_list(parms)
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
                            users_recycler.setAdapter(new UsersAdapter(FollowersList.this, apiResponse.getData().getFollows(), dataManager));
                            if (apiResponse.getData().getFollows().size() == 0) {
                                FOLLOWERS_empty_list.setVisibility(View.VISIBLE);
                            } else {
                                FOLLOWERS_empty_list.setVisibility(View.GONE);

                            }

                        } else {
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(FollowersList.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void get_blocks() {

        CommonUtilities.showStaticDialog(this, "getBlocks");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        myAPI.block_list(parms)
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
                            if (apiResponse.getData().getBlocks().size() == 0) {
                                BLOCK_empty_list.setVisibility(View.VISIBLE);
                            } else {
                                BLOCK_empty_list.setVisibility(View.GONE);

                            }


                            users_recycler.setAdapter(new BlocksAdapter(FollowersList.this, apiResponse.getData().getBlocks(), dataManager, myAPI , FollowersList.this));

                        } else {
                            Toast.makeText(FollowersList.this, apiResponse.getMsg() + "get", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(FollowersList.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


    void get_logs() {

        CommonUtilities.showStaticDialog(this, "getLogs");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        myAPI.get_logs(parms)
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

                            if (apiResponse.getData().getLogs().size() == 0) {
                                LOGS_empty_list.setVisibility(View.VISIBLE);
                            } else {
                                LOGS_empty_list.setVisibility(View.GONE);

                            }

                            users_recycler.setAdapter(new LogsAdapter(FollowersList.this, apiResponse.getData().getLogs()));

                        } else {
                            Toast.makeText(FollowersList.this, apiResponse.getMsg() + "get", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(FollowersList.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    void get_notifications() {

        CommonUtilities.showStaticDialog(this, "getNotifications");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        myAPI.get_notifications(parms)
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

                            if (apiResponse.getData().getNotifications().size() == 0) {
                                NOTIFICATIONS_empty_list.setVisibility(View.VISIBLE);
                            } else {
                                NOTIFICATIONS_empty_list.setVisibility(View.GONE);

                            }

                            users_recycler.setAdapter(new NotificationsAdapter(FollowersList.this, apiResponse.getData().getNotifications()));

                        } else {
                            Toast.makeText(FollowersList.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(FollowersList.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    @Override
    public void Refresh(String s) {

        switch (s)
        {
            case "FOLLOWING":
                get_followers(ItemID);
                break;

            case "BLOCK":

                get_blocks();
                break;
        }

    }
}
