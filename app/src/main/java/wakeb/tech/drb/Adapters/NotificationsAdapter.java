package wakeb.tech.drb.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wakeb.tech.drb.Models.LOG;
import wakeb.tech.drb.Models.Notifications;
import wakeb.tech.drb.R;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context context;
    private List<Notifications> my_data;

    public NotificationsAdapter(Context context, List<Notifications> my_data) {
        this.context = context;
        this.my_data = my_data;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Notifications model = my_data.get(position);

        holder.date.setText(DateUtils.getRelativeTimeSpanString(model.getCreatedAt(),
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS).toString());
        holder.desc.setText(model.getTitleAr());
        Glide.with(context)
                .load(model.getImage())
                .apply(new RequestOptions()
                        .placeholder(holder.user_profile_image.getDrawable())
                )
                .into(holder.user_profile_image);


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        View view;
        TextView  desc , date;
        CircleImageView user_profile_image;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;


            user_profile_image = (CircleImageView) view.findViewById(R.id.user_profile_image);
            desc = (TextView) view.findViewById(R.id.desc);
            date = (TextView) view.findViewById(R.id.date);


        }
    }


}