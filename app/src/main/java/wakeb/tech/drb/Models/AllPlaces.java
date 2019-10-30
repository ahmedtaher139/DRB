package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllPlaces {


        @SerializedName("stores")
        @Expose
        private List<NearPlaces> stores = null;
        @SerializedName("suggests")
        @Expose
        private List<Suggest> suggests = null;
        @SerializedName("risks")
        @Expose
        private List<Risk> risks = null;

        public List<NearPlaces> getStores() {
        return stores;
    }

        public void setStores(List<NearPlaces> stores) {
        this.stores = stores;
    }

        public List<Suggest> getSuggests() {
        return suggests;
    }

        public void setSuggests(List<Suggest> suggests) {
        this.suggests = suggests;
    }

        public List<Risk> getRisks() {
        return risks;
    }

        public void setRisks(List<Risk> risks) {
        this.risks = risks;
    }

    }
