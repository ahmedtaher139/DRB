package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestsPlaces {


        @SerializedName("suggests")
        @Expose
        private List<Suggest> suggests = null;
        @SerializedName("meta")
        @Expose
        private Meta meta;

        public List<Suggest> getSuggests() {
        return suggests;
    }

        public void setSuggests(List<Suggest> suggests) {
        this.suggests = suggests;
    }

        public Meta getMeta() {
        return meta;
    }

        public void setMeta(Meta meta) {
        this.meta = meta;
    }

    }
