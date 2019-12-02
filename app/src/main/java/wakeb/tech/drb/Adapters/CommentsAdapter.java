package wakeb.tech.drb.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wakeb.tech.drb.Models.Comment;
import wakeb.tech.drb.Models.StorePlace;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Stores.StoresInterface;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.PlaceSettingsListener;
import wakeb.tech.drb.Uitils.Refresh;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> implements PlaceSettingsListener {

    private Context context;
    private List<Comment> my_data;
    DataManager dataManager ;
    ApiServices apiServices ;


    public CommentsAdapter(Context context, List<Comment> my_data , DataManager dataManager , ApiServices apiServices) {
        this.context = context;
        this.my_data = my_data;
        this.dataManager = dataManager;
        this.apiServices = apiServices;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Comment model = my_data.get(position);

        holder.commeent.setText(model.getComment());
        holder.commentUser.setText(model.getPublisher().getDisplayName());

        holder.time.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(model.getCreatedAt()),
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS).toString());

        Glide.with(context)
                .load(model.getPublisher().getImage())
                .apply(new RequestOptions()
                        .placeholder(holder.commentImage.getDrawable())
                )
                .into(holder.commentImage);


        if (Objects.equals(model.getPublisher().getId().toString(), dataManager.getID())) {
            holder.setting.setVisibility(View.VISIBLE);
        }

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupMenuClick(v, String.valueOf(model.getId()), model.getComment());

            }
        });



    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    @Override
    public void onPopupMenuClick(View view, final String commentID, final String commentText) {


        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.comments_item_settings, popup.getMenu());
        Menu menu = popup.getMenu();
        menu.findItem(R.id.posts_setting_edit).setTitle(context.getString(R.string.edit_comment));
        menu.findItem(R.id.posts_setting_delete).setTitle(context.getString(R.string.delete_comment));
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //do your things in each of the following cases
                switch (item.getItemId()) {
                    case R.id.posts_setting_edit:
                        final AlertDialog.Builder editDialog = new AlertDialog.Builder(context);
                        editDialog.setTitle(context.getResources().getString(R.string.edit_comment));
                        // Create EditText box to input repeat number
                        final EditText input = new EditText(context);
                        input.setText(commentText);
                        editDialog.setView(input);
                        editDialog.setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {


                                        edit_comment(commentID , input.getText().toString());


                                    }
                                });
                        editDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do nothing
                            }
                        });
                        editDialog.show();
                        return true;

                    case R.id.posts_setting_delete:


                        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                        deleteDialog.setTitle(context.getResources().getString(R.string.delete_comment));
                        deleteDialog.setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        delete_comment(commentID);


                                    }
                                });
                        deleteDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                        deleteDialog.show();

                        return true;
                    default:
                        return false;
                }

            }
        });
        popup.show();


    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        View view;
        CircleImageView commentImage;
        TextView commentUser, commeent, time;
        ImageButton setting;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            time = (TextView) view.findViewById(R.id.comment_time);
            commentImage = (CircleImageView) view.findViewById(R.id.comment_image_profile);
            commentUser = (TextView) view.findViewById(R.id.comment_username);
            commeent = (TextView) view.findViewById(R.id.comment_comment);
            setting = (ImageButton) view.findViewById(R.id.comment_setting);

        }
    }


    void edit_comment(String comment_id , String body) {


        Map<String, String> parms = new HashMap<>();
        parms.put("comment_id", comment_id);
        parms.put("comment", body);
        parms.put("publisher_id", dataManager.getID());
        apiServices.update_comment(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {
                            if(context instanceof Refresh){
                                ((Refresh)context).refresh();
                            }

                        } else {

                            Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    void delete_comment(String comment_id ) {


        Map<String, String> parms = new HashMap<>();
        parms.put("comment_id", comment_id);
        parms.put("publisher_id", dataManager.getID());
        apiServices.delete_comment(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            if(context instanceof Refresh){
                                ((Refresh)context).refresh();
                            }
                        } else {

                            Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }
}