package wakeb.tech.drb.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;


import org.greenrobot.eventbus.EventBus;

import wakeb.tech.drb.Home.EventBusMssg_home;
import wakeb.tech.drb.R;

public class EndTripDialog extends Dialog {

    private static Context c;
    public Dialog d;
    private  RadioButton EndTrip_Public;
    private RadioButton EndTrip_Followers;
    private RadioButton EndTrip_Private;
    private TextInputEditText EndTrip_Desc ;
    private Switch EndTrip_posted ;
    private Button EndTrip_End ;

    String privacy = "public" ;
    String  Posted = "1" ;

    public EndTripDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    public EndTripDialog() {
        super(c);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_trip_dialog);


        EndTrip_Public = (RadioButton) findViewById(R.id.EndTrip_Public);
        EndTrip_Followers = (RadioButton) findViewById(R.id.EndTrip_Followers);
        EndTrip_Private = (RadioButton) findViewById(R.id.EndTrip_Private);
        EndTrip_Desc = (TextInputEditText) findViewById(R.id.EndTrip_Desc);
        EndTrip_posted = (Switch) findViewById(R.id.EndTrip_posted);
        EndTrip_End = (Button) findViewById(R.id.EndTrip_End);

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

        EndTrip_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusMssg_home(EndTrip_Desc.getText().toString() , Posted ,privacy ));
                dismiss();
            }
        });
        EndTrip_posted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EndTrip_posted.isChecked())
                {
                    Posted = "1";
                }
                else
                {
                    Posted = "0";
                }
            }
        });

    }
}