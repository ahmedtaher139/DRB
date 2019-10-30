package wakeb.tech.drb.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import wakeb.tech.drb.Adapters.HistoryAdapter;
import wakeb.tech.drb.R;

public class ChangePrivacy extends Dialog {


    private static Context c;
    public Dialog d;
    private RadioButton EndTrip_Public;
    private RadioButton EndTrip_Followers;
    private RadioButton EndTrip_Private;

    private Button EndTrip_End ;

    String privacy = "public" ;
    String  Posted = "1" ;
    String ID ;

    public ChangePrivacy(Activity a , String ID) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.ID = ID;
    }

    public ChangePrivacy() {
        super(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_privacy);

        EndTrip_Public = (RadioButton) findViewById(R.id.EndTrip_Public);
        EndTrip_Followers = (RadioButton) findViewById(R.id.EndTrip_Followers);
        EndTrip_Private = (RadioButton) findViewById(R.id.EndTrip_Private);

        EndTrip_End = (Button) findViewById(R.id.EndTrip_End);

        EndTrip_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c instanceof HistoryAdapter.HistoryAdapterCallback) {
                    ((HistoryAdapter.HistoryAdapterCallback) c).edit(ID ,privacy );
                }
                dismiss();
            }
        });


        EndTrip_Public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EndTrip_Public.setChecked(true);
                EndTrip_Followers.setChecked(false);
                EndTrip_Private.setChecked(false);
                privacy = "public";
            }
        });
        EndTrip_Followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTrip_Public.setChecked(false);
                EndTrip_Followers.setChecked(true);
                EndTrip_Private.setChecked(false);
                privacy = "flowers";

            }
        });

        EndTrip_Private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTrip_Public.setChecked(false);
                EndTrip_Followers.setChecked(false);
                EndTrip_Private.setChecked(true);
                privacy = "private";

            }
        });


    }
}
