package wakeb.tech.drb.SuggestionPlaces;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import wakeb.tech.drb.Activities.NewResource;
import wakeb.tech.drb.Activities.TripComments;
import wakeb.tech.drb.Adapters.CommentsAdapter;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.Suggest;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class SuggestedPlaces extends BaseActivity {


    @OnClick(R.id.add_new_suggest)
    void add_new_suggest() {

       startActivity(new Intent(SuggestedPlaces.this , AddNewPlace.class));
    }
    ArrayList<Suggest> suggests;
    @BindView(R.id.empty_list)
    RelativeLayout empty_list;

    @BindView(R.id.suggested_places)
    RecyclerView suggested_places;


    @BindView(R.id.suggests_NestedScrollView)
    NestedScrollView suggests_NestedScrollView;

    @BindView(R.id.suggests_SwipeRefreshLayout)
    SwipeRefreshLayout suggests_SwipeRefreshLayout;


    SuggestionAdapter suggestionAdapter;
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;
    int page_number = 1;
    boolean isLast = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_places);



        suggested_places.setLayoutManager(new LinearLayoutManager(this));
        suggests = new ArrayList<>();
        suggestionAdapter = new SuggestionAdapter(SuggestedPlaces.this ,suggests, dataManager);
        suggested_places.setAdapter(suggestionAdapter);

        suggests_SwipeRefreshLayout.setRefreshing(true);
        suggests_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                suggests.clear();
                isLast = false;
                page_number = 1;
                list_suggest();
            }
        });


        suggests_NestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == 0) {

                }
                if (scrollY > oldScrollY) {

                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if (!isLast)
                    {   list_suggest();

                    }




                }
            }
        });



        list_suggest();
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

    void list_suggest() {

        CommonUtilities.showStaticDialog(this, "");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        parms.put("page", String.valueOf(page_number));

        myAPI.list_suggest(parms)
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

                            page_number++;
                            if (apiResponse.getData().getSuggests().getMeta().getNextPageUrl().equals("")) {
                                isLast = true;
                            }
                            if( apiResponse.getData().getSuggests().getSuggests().size()==0)
                            {
                                empty_list.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                empty_list.setVisibility(View.GONE);

                            }
                            suggests.addAll(apiResponse.getData().getSuggests().getSuggests());
                        } else {
                            Toast.makeText(SuggestedPlaces.this, apiResponse.getMsg() , Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SuggestedPlaces.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("RETROFIT_ERROR" , e.getMessage());
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {
                        suggestionAdapter.notifyDataSetChanged();
                        try {
                            suggests_SwipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }

                        try {

                        } catch (Exception e) {
                        }
                    }
                });
    }
}
