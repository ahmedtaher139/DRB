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
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import wakeb.tech.drb.Home.EventBusMssg_home;
import wakeb.tech.drb.R;

public class NewPlaceDialog extends Dialog {

    private static Context c;
    public Dialog d;
    TextInputEditText AddPlace_Desc;
    TextView AddPlace_address;
    Button AddPlace_Add;
    String address;

    public NewPlaceDialog(Activity a , String address) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.address = address;
    }

    public NewPlaceDialog() {
        super(c);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place_dialog);

        AddPlace_Desc = (TextInputEditText) findViewById(R.id.AddPlace_Desc) ;
        AddPlace_address = (TextView) findViewById(R.id.AddPlace_address) ;
        AddPlace_Add = (Button) findViewById(R.id.AddPlace_Add) ;
        AddPlace_address.setText(address);
        AddPlace_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(AddPlace_Desc.getText().toString());
                dismiss();
            }
        });

    }
}
