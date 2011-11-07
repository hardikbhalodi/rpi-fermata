package remote.client;

import remote.client.R.menu;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.Context;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.SubMenu;
import android.view.View;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;


public class FermataActivity extends Activity {

	// Layout view
	private TextView mTitle;

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_IP_CONNECT = 3;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_IP = 6;
	public static final int MESSAGE_FILTER_LIST = 7;

	// Key names received from the BluetoothCommandService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String FILTER_LIST = "filter_list";
	public static final String TOAST = "toast";
	
	private int xfilter;
	private int yfilter;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for Bluetooth Command Service
	private ConnectionService mCommandService = null;

	private Menu optionsMenu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		xfilter = 0;
		yfilter = 1;
		
		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView textView = (TextView)findViewById(R.id.textView);
		// this is the view on which you will listen for touch events
		final View touchView = findViewById(R.id.touchView);
		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText("Xfilter: " + xfilter +  " Yfilter: " + yfilter);
		mTitle = (TextView) findViewById(R.id.title_right_text);
		touchView.setOnTouchListener(new View.OnTouchListener() {
			
			Display display = getWindowManager().getDefaultDisplay(); 
			int width = display.getWidth();
			int height = display.getHeight();
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String xcoord= String.valueOf((int)((double)(event.getX()/width) * 255));
				String ycoord= String.valueOf((int)((double)(event.getY()/height) * 255));
				String fullText = xfilter + "," + xcoord + ";" + yfilter + "," + ycoord;
				textView.setText(fullText);
				mCommandService.write(fullText);
				return true;
			}
		});
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		// If BT is not on, request that it be enabled.
		// setupCommand() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		// otherwise set up the command service
		else {
			if (mCommandService==null)
				setupCommand();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if (mCommandService != null) {
			if (mCommandService.getState() == ConnectionService.STATE_NONE) {
				mCommandService.start();
			}
		}
	}

	private void setupCommand() {
		// Initialize the BluetoothChatService to perform bluetooth connections
		mCommandService = new ConnectionService(this, mHandler);
		String message = "guh";
		byte[] send = new byte[4];
		send=message.getBytes();
		System.out.println("guh shouuld print "+send);
		mCommandService.write(send);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mCommandService != null)
			mCommandService.stop();
	}

	private void ensureDiscoverable() {
		if (mBluetoothAdapter.getScanMode() !=
				BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	// The Handler that gets information back from the ConnectionService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case ConnectionService.STATE_CONNECTED:
					mTitle.setText(R.string.title_connected_to);
					mTitle.append(mConnectedDeviceName);
					break;
				case ConnectionService.STATE_CONNECTING:
					mTitle.setText(R.string.title_connecting);
					break;
				case ConnectionService.STATE_LISTEN:
				case ConnectionService.STATE_NONE:
					mTitle.setText(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				Log.v("FermataDebug", "Setting the device name to: " + msg.getData().getString(DEVICE_NAME));
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(), "Connected to "
						+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
						Toast.LENGTH_SHORT).show();
				break;

			case MESSAGE_FILTER_LIST:
				String name;
				int uid, axis, default_value;
				SubMenu filtersMenu = optionsMenu.addSubMenu(3, 0, 0, "Change Filters");
				String filters[] =(msg.getData().getString(FILTER_LIST)).split(";");
				for(int i = 0; i < filters.length; i++)
				{
					String specs[] = filters[i].split(",");
					name=specs[0];
					uid = Integer.parseInt(specs[1]);
					axis = Integer.parseInt(specs[2]);
					filtersMenu.add(axis, uid, 0, name);
				}
				break;

			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras()
						.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				// Attempt to connect to the device
				mCommandService.connect(device);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupCommand();
			} else {
				// User did not enable Bluetooth or an error occured
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				finish();
			}
			break;

		case REQUEST_IP_CONNECT:
			//If the result from the IP connection activity is okay
			if(resultCode == Activity.RESULT_OK) {

				//Parse out the IP and port and pass it along to the connect thread to make 
				//and setup the connection
				Bundle extras = data.getExtras();
				if(extras != null)
				{
					String datastring = extras.getString(IPConnectActivity.IP_ADDRESS);
					String[] ipport = datastring.split(":");
					if(ipport.length == 2)
					{
						int portInt = Integer.parseInt(ipport[1]);
						mCommandService.connect(ipport[0], portInt);
					}
				}
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		optionsMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ipconnect:
			Intent ipIntent = new Intent(this, IPConnectActivity.class);
			startActivityForResult(ipIntent, REQUEST_IP_CONNECT);
			return true;
		case R.id.scan:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}

		switch (item.getGroupId()) {
		case 0:
			xfilter = item.getItemId();
			mTitle.setText("Xfilter: " + xfilter +  " Yfilter: " + yfilter);
			break;
			
		case 1:
			yfilter = item.getItemId();
			mTitle.setText("Xfilter: " + xfilter +  " Yfilter: " + yfilter);
			break;
			
		case 2:
			xfilter = item.getItemId();
			yfilter = item.getItemId();
			mTitle.setText("Xfilter: " + xfilter +  " Yfilter: " + yfilter);

			break;
		}
		return false;
	}

	/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			//mCommandService.write(BluetoothCommandService.VOL_UP);
			mCommandService.write(5);
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
			mCommandService.write(BluetoothCommandService.VOL_DOWN);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	 */
}
