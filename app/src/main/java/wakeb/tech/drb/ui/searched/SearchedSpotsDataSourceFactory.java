package wakeb.tech.drb.ui.searched;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.ui.profile.ProfileSpotsDataSource;


public class SearchedSpotsDataSourceFactory extends DataSource.Factory {
    SearchedSpotsDataSource doctorsDataSource;
    MutableLiveData<SearchedSpotsDataSource> mutableLiveData;
    DataManager dataManager;
    String type , search_id;





    public SearchedSpotsDataSourceFactory(DataManager dataManager, String type , String search_id) {
        this.dataManager = dataManager;
        this.type = type;
        this.search_id = search_id;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        doctorsDataSource = new SearchedSpotsDataSource(  dataManager  , type , search_id);
        mutableLiveData.postValue(doctorsDataSource);
        return doctorsDataSource;

    }

    public MutableLiveData<SearchedSpotsDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    public SearchedSpotsDataSource getDataSource() {
        return doctorsDataSource;
    }

}
