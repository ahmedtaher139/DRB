package wakeb.tech.drb.imagepiker.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import wakeb.tech.drb.R;
import wakeb.tech.drb.imagepiker.features.ImagePicker;
import wakeb.tech.drb.imagepiker.model.Image;

import static wakeb.tech.drb.Uitils.CommonUtilities.isVideoFile;


public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Image> my_data;


    public ImagesAdapter(Context context, List<Image> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    public List<Image> getMy_data() {
        return my_data;
    }

    public void setMy_data(List<Image> my_data) {
        this.my_data = my_data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == my_data.size()) ? 1 : 0;
    }


    public interface Refreshing {
        void Refresh(String FLAG);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view_item, parent, false);

            return new ViewHolder(itemView);

        } else {


            return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_image_item, parent, false));

        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        switch (holder.getItemViewType()) {
            case 0:
                final ViewHolder viewHolder = (ViewHolder) holder;
                final Image model = my_data.get(position);

                Glide.with(context)
                        .load(model.getPath())
                        .into(viewHolder.imageView);



                if (isVideoFile(model.getPath())) {
                    viewHolder.fileTypeIndicator.setText("Video");
                    viewHolder.fileTypeIndicator.setVisibility(View.VISIBLE);

                } else {
                    viewHolder.fileTypeIndicator.setVisibility(View.GONE);

                }

                viewHolder.remove_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        my_data.remove(position);
                        notifyDataSetChanged();

                    }
                });
                break;

            case 1:
                final ViewHolder2 viewHolder2 = (ViewHolder2) holder;

                viewHolder2.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImagePicker.create((Activity) context)
                                .toolbarImageTitle(context.getString(R.string.tap_to_select)) // image selection title
                                .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                                .includeVideo(false) // Show video on image picker
                                .multi() // multi mode (default mode)
                                .enableLog(false) // disabling log
                                .start(); // start image picker activity with request code

                    }
                });

                break;
        }


    }

    @Override
    public int getItemCount() {
        return my_data.size() + 1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView imageView;
        private View view;
        private TextView fileTypeIndicator;
        private RelativeLayout remove_item;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            imageView = itemView.findViewById(R.id.image_view);
            fileTypeIndicator = itemView.findViewById(R.id.ef_item_file_type_indicator);
            remove_item = itemView.findViewById(R.id.remove_item);
        }
    }


    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private View view;

        ViewHolder2(View itemView) {
            super(itemView);
            view = itemView;
        }
    }
}