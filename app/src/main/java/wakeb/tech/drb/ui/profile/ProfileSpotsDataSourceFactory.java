package wakeb.tech.drb.ui.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.ui.favorites.FavesSpotsDataSource;


public class ProfileSpotsDataSourceFactory extends DataSource.Factory {
    ProfileSpotsDataSource doctorsDataSource;
    MutableLiveData<ProfileSpotsDataSource> mutableLiveData;
    DataManager dataManager;
    String id;

    public ProfileSpotsDataSourceFactory(DataManager dataManager , String id) {
        this.dataManager = dataManager;
        this.id = id;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        doctorsDataSource = new ProfileSpotsDataSource(  dataManager  , id);
        mutableLiveData.postValue(doctorsDataSource);
        return doctorsDataSource;

    }

    public MutableLiveData<ProfileSpotsDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    public ProfileSpotsDataSource getDataSource() {
        return doctorsDataSource;
    }

}
