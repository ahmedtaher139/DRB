package wakeb.tech.drb.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import wakeb.tech.drb.Models.Comment;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.PlaceSettingsListener;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiServices;


public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> implements PlaceSettingsListener {

    private Context context;
    private List<Comment> my_data;
    DataManager dataManager ;
    ApiServices apiServices ;


    public FollowingAdapter(Context context, List<Comment> my_data , DataManager dataManager , ApiServices apiServices) {
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
        holder.time.setText(model.getCreatedAt());

        if (Objects.equals(model.getPublisher().getId().toString(), dataManager.getID())) {
            holder.setting.setVisibility(View.VISIBLE);
        }

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    @Override
    public void onPopupMenuClick(View view, String commentID, final String commentText) {


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
        ImageView commentImage;
        TextView commentUser, commeent, time;
        ImageButton setting;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            time = (TextView) view.findViewById(R.id.comment_time);
            commentImage = (ImageView) view.findViewById(R.id.comment_image_profile);
            commentUser = (TextView) view.findViewById(R.id.comment_username);
            commeent = (TextView) view.findViewById(R.id.comment_comment);
            setting = (ImageButton) view.findViewById(R.id.comment_setting);

        }
    }


}