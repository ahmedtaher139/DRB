package wakeb.tech.drb.ui.favorites;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

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

public class FavesSpotsDataSource extends PageKeyedDataSource<Long, SpotModel> {


    ApiServices myAPI;
    Retrofit retrofit;


    DataManager dataManager;
    FaveViewModel viewModel;

    public FavesSpotsDataSource(DataManager dataManager, FaveViewModel viewModel) {

        this.dataManager = dataManager;
        this.viewModel = viewModel;

    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, SpotModel> callback) {

        // doctorsDiseasesListViewModel.showProgress();

        viewModel.show_fave_progress();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        Map<String, String> parms = new HashMap<>();
        parms.put("page", "1");
        parms.put("publisher_id", dataManager.getID());
        myAPI.favourite_spots(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                        //  Log.w("printed gson => ", new GsonBuilder().setPrettyPrinting().create().toJson(apiResponse));

                        if (apiResponse.getStatus()) {

                            callback.onResult(apiResponse.getData().getSpotModels(), null, (long) 2);
                            viewModel.hide_fave_progress();

                        } else {

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        viewModel.hide_fave_progress();

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
        parms.put("page", String.valueOf(params.key));
        parms.put("publisher_id", dataManager.getID());
        myAPI.favourite_spots(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                        //  Log.w("printed gson => ", new GsonBuilder().setPrettyPrinting().create().toJson(apiResponse));

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
