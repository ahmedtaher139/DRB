package wakeb.tech.drb.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wakeb.tech.drb.Models.Publisher;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.Profile.UserProfile;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;


public class UsersSuggestionAdapter extends RecyclerView.Adapter<UsersSuggestionAdapter.ViewHolder>  {

    private Context context;
    private List<Publisher> my_data;
    DataManager dataManager ;
    ApiServices apiServices ;
    Refresh_suggestions refresh_suggestions;

  public  interface Refresh_suggestions
    {
        void refresh_suggestion ();
    }


    public UsersSuggestionAdapter(Context context, List<Publisher> my_data , DataManager dataManager , ApiServices apiServices ,  Refresh_suggestions refresh_suggestions) {
        this.context = context;
        this.my_data = my_data;
        this.dataManager = dataManager;
        this.apiServices = apiServices;
        this.refresh_suggestions=refresh_suggestions;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Publisher model = my_data.get(position);
        holder.Suggestions_item_userName.setText("@"+model.getUsername());
        holder.Suggestions_item_displayName.setText(model.getDisplayName());

        Glide.with(context)
                .load(model.getImage())
                .apply(new RequestOptions()
                        .placeholder(holder.Suggestions_item_image.getDrawable())
                )
                .into(holder.Suggestions_item_image);

        holder.Suggestions_item_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_follow(model.getId().toString());
            }
        });




        holder.Suggestions_item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (String.valueOf(model.getId()).equals(dataManager.getID())) {
                    Intent intent = new Intent(context, MyProfile.class);
                    intent .putExtra("User_Name" , model.getDisplayName());
                    intent .putExtra("User_Image" , model.getImage());

                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, UserProfile.class);
                    intent.putExtra("ItemID", String.valueOf(model.getId()));
                    intent .putExtra("User_Name" , model.getDisplayName());
                    intent .putExtra("User_Image" , model.getImage());
                    context.startActivity(intent );
                }


            }
        });



    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {


        View view;
        CircleImageView Suggestions_item_image;
        TextView Suggestions_item_userName, Suggestions_item_displayName;
        RelativeLayout Suggestions_item_follow;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            Suggestions_item_image = (CircleImageView) view.findViewById(R.id.Suggestions_item_image);
            Suggestions_item_userName = (TextView) view.findViewById(R.id.Suggestions_item_userName);
            Suggestions_item_displayName = (TextView) view.findViewById(R.id.Suggestions_item_displayName);
            Suggestions_item_follow = (RelativeLayout) view.findViewById(R.id.Suggestions_item_follow);

        }
    }


    void add_follow(String follower_id) {



        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", follower_id);
        parms.put("user_id", dataManager.getID());
        apiServices.follow_action(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            refresh_suggestions.refresh_suggestion();

                        } else {

                            Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


}