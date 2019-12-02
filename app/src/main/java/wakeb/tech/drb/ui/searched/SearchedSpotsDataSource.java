package wakeb.tech.drb.ui.searched;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class SearchedSpotsDataSource extends PageKeyedDataSource<Long, SpotModel> {


    ApiServices myAPI;
    Retrofit retrofit;


    DataManager dataManager;
    String type , search_id;

    public SearchedSpotsDataSource(DataManager dataManager, String type , String search_id ) {

        this.dataManager = dataManager;
        this.type = type;
        this.search_id = search_id;

    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, SpotModel> callback) {
        Log.i("printed gson => ", "test");

        // doctorsDiseasesListViewModel.showProgress();

        // doctorsDiseasesListViewModel.showDialog();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", dataManager.getID());
        parms.put("id", search_id);
        parms.put("type", type);
        parms.put("page", "1");
        myAPI.searched_spots(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                        Log.w("printed gson => ", new GsonBuilder().setPrettyPrinting().create().toJson(apiResponse));

                        if (apiResponse.getStatus()) {

                            callback.onResult(apiResponse.getData().getSpotModels(), null, (long) 2);

                        } else {

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("printed gson => ", e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, SpotModel> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, SpotModel> callback) {

        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", dataManager.getID());
        parms.put("id", search_id);
        parms.put("type", type);
        parms.put("page", String.valueOf(params.key));
        myAPI.get_profile_spots(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                        Log.i("printed gson => ", new GsonBuilder().setPrettyPrinting().create().toJson(apiResponse));

                        if (apiResponse.getStatus()) {

                            callback.onResult(apiResponse.getData().getSpotModels(), params.key + 1);
                        } else {

                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void invalidate() {
        super.invalidate();
    }
}
