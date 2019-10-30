package wakeb.tech.drb.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
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
import retrofit2.Retrofit;
import wakeb.tech.drb.Models.Publisher;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.Profile.UserProfile;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.Data;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context context;
    private List<Publisher> my_data;
    DataManager dataManager;


    public UsersAdapter(Context context, List<Publisher> my_data , DataManager dataManager) {
        this.context = context;
        this.my_data = my_data;
        this.dataManager = dataManager;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Publisher model = my_data.get(position);
        holder.user_name.setText("@" + model.getUsername());
        holder.display_name.setText(model.getDisplayName());
        if(position == my_data.size()-1){holder.underline.setVisibility(View.GONE);}


         Glide.with(context)
                .load(model.getImage())
                .apply(new RequestOptions()
                        .placeholder(holder.user_profile_image.getDrawable())
                )
                .into(holder.user_profile_image);

        holder.view.setOnClickListener(new View.OnClickListener() {
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
        CircleImageView user_profile_image;
        TextView user_name , display_name;
        RelativeLayout underline;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            user_profile_image = (CircleImageView) view.findViewById(R.id.user_profile_image);
            user_name = (TextView) view.findViewById(R.id.user_name);
            display_name = (TextView) view.findViewById(R.id.display_name);
            underline = (RelativeLayout) view.findViewById(R.id.underline);


        }
    }


}