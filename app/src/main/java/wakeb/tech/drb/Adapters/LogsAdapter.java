package wakeb.tech.drb.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wakeb.tech.drb.Models.LOG;
import wakeb.tech.drb.Models.StorePlace;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Stores.StoresInterface;


public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {

    private Context context;
    private List<LOG> my_data;

    public LogsAdapter(Context context, List<LOG> my_data) {
        this.context = context;
        this.my_data = my_data;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final LOG model = my_data.get(position);

        switch (model.getType())
        {
            case "FOLLOW":
                holder.text.setText(context.getString(R.string.log_follow) + " " + model.getPublisher().getDisplayName());
                break;

            case "UNFOLLOW":
                holder.text.setText(context.getString(R.string.log_unfollow) + " "+ model.getPublisher().getDisplayName());

                break;

            case "LIKE":
                holder.text.setText(context.getString(R.string.log_like) + " "+ model.getPublisher().getDisplayName());

                break;

            case "DISLIKE":
                holder.text.setText(context.getString(R.string.log_dislike)+ " " + model.getPublisher().getDisplayName());

                break;

            case "FAVORITE":
                holder.text.setText(context.getString(R.string.log_fav) + " "+ model.getPublisher().getDisplayName());

                break;

            case "UNFAVORITE":
                holder.text.setText(context.getString(R.string.log_unfav) + " "+ model.getPublisher().getDisplayName());

                break;

            case "SHARE":
                holder.text.setText(context.getString(R.string.log_share) + " "+ model.getPublisher().getDisplayName());

                break;

            case "COMMENT":
                holder.text.setText(context.getString(R.string.log_comment) + " "+ model.getPublisher().getDisplayName());

                break;


        }



        Glide.with(context)
                .load(model.getPublisher().getImage())
                .apply(new RequestOptions()
                        .placeholder(holder.user_profile_image.getDrawable())
                )
                .into(holder.user_profile_image);

        if(position == my_data.size()-1){holder.underline.setVisibility(View.GONE);}


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        View view;
        CircleImageView user_profile_image;
        TextView text;
        RelativeLayout underline;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            user_profile_image = (CircleImageView) view.findViewById(R.id.user_profile_image);
            text = (TextView) view.findViewById(R.id.text);
            underline = (RelativeLayout) view.findViewById(R.id.underline);


        }
    }


}