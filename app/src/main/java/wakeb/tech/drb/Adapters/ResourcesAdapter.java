package wakeb.tech.drb.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;
import wakeb.tech.drb.Activities.ViewRecourse;
import wakeb.tech.drb.Models.Resource;
import wakeb.tech.drb.R;


public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ViewHolder> {

    private Context context;
    private List<Resource> my_data;

    public ResourcesAdapter(Context context, List<Resource> my_data) {
        this.context = context;
        this.my_data = my_data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_item, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Resource model = my_data.get(position);

        if (model.getType().equals("image"))
        {
            Glide.with(context)
                    .load(model.getResource())
                    .apply(new RequestOptions()
                            .placeholder(holder.image.getDrawable())
                    )
                    .into(holder.image);

            holder.view_recourse_video.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
        }
        else
        {

            Glide.with(context)
                    .asBitmap()
                    .load(model.getResource())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.view_recourse_video.mThumbImageView);

            holder.view_recourse_video.startPlay(model.getResource(), MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            holder.view_recourse_video.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);


        }

        holder.date.setText(model.getCreatedAt());
        holder.address.setText(model.getAddress());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(context, ViewRecourse.class);
                intent.putExtra("TYPE", model.getType());
                intent.putExtra("ADDRESS",model.getAddress());
                intent.putExtra("URL",model.getResource());
                intent.putExtra("DESC", model.getDesc());
                context.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        View view;
        ImageView image ;
        TextView address, date;
        MxVideoPlayerWidget view_recourse_video;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            image = (ImageView) view.findViewById(R.id.image);
            address = (TextView) view.findViewById(R.id.address);
            date = (TextView) view.findViewById(R.id.date);

            view_recourse_video = (MxVideoPlayerWidget) view.findViewById(R.id.view_recourse_video);


        }
    }


}