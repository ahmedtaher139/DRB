package wakeb.tech.drb.ui.spots;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.data.DataManager;

public class SpotViewModel extends AndroidViewModel {

    Executor executor;
    SpotsDataSourceFactory spotsDataSourceFactory;
    MutableLiveData<SpotsDataSource> spots_modelMutableLiveData;
    LiveData<PagedList<SpotModel>> listLiveData;


    FollowersSpotsDataSourceFactory followersSpotsDataSourceFactory;
    MutableLiveData<FollowersSpotsDataSource> followersSpots_modelMutableLiveData;
    LiveData<PagedList<SpotModel>> followersListLiveData;

    public final MutableLiveData<Integer> public_progress;
    public final MutableLiveData<Integer> followers_progress;

    DataManager dataManager;

    public SpotViewModel(@NonNull Application application) {
        super(application);
        dataManager = ((MainApplication) getApplication()).getDataManager();
        public_progress = new MutableLiveData<>();
        followers_progress = new MutableLiveData<>();
    }

    public void get_spots() {
        spotsDataSourceFactory = new SpotsDataSourceFactory(dataManager , this);
        spots_modelMutableLiveData = spotsDataSourceFactory.getMutableLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(5)
                .setPageSize(10)
                .setPrefetchDistance(4)
                .build();
        executor = Executors.newFixedThreadPool(5);
        listLiveData = (new LivePagedListBuilder<Long, SpotModel>(spotsDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();

    }

    public void get_followers_spots() {
        followersSpotsDataSourceFactory = new FollowersSpotsDataSourceFactory(dataManager , this);
        followersSpots_modelMutableLiveData = followersSpotsDataSourceFactory.getMutableLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(5)
                .setPageSize(10)
                .setPrefetchDistance(4)
                .build();
        executor = Executors.newFixedThreadPool(5);
        followersListLiveData = (new LivePagedListBuilder<Long, SpotModel>(followersSpotsDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();

    }


    public void show_public_progress() {
        public_progress.postValue(1);
    }

    public void hide_public_progress() {
        public_progress.postValue(0);
    }


    public void show_followers_progress() {
        followers_progress.postValue(1);
    }

    public void hide_followers_progress() {
        followers_progress.postValue(0);
    }

    public LiveData<PagedList<SpotModel>> getListLiveData() {
        return listLiveData;
    }

    public LiveData<PagedList<SpotModel>> getFollowersListLiveData() {
        return followersListLiveData;
    }

    public void refresh_public() {

        spotsDataSourceFactory.getMutableLiveData().getValue().invalidate();
    }

    public void refresh_followers() {

        followersSpotsDataSourceFactory.getMutableLiveData().getValue().invalidate();
    }


    public MutableLiveData<Integer> getPublic_progress() {
        return public_progress;
    }

    public MutableLiveData<Integer> getFollowers_progress() {
        return followers_progress;
    }
}
