package remote.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class IPConnectActivity extends Activity {
	
	public static String IP_ADDRESS = "ip_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.ip_connect);

        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Initialize the button to perform device discovery
        Button ipButton = (Button) findViewById(R.id.button_IP_accept);
        ipButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	
                EditText ipfield = (EditText)findViewById(R.id.ipconnect);
                String ip = ipfield.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(IP_ADDRESS, ip);
                setResult(Activity.RESULT_OK, intent);
                finish();
           }
        });
        
	
    }
    
}
