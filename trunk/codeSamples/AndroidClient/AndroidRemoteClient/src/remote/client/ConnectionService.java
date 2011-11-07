package remote.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class ConnectionService {
	// Debugging
	private static final String TAG = "ConnectionService";
	private static final boolean D = true;

	// Unique UUID for this application
	private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");

	// Member fields
	private final BluetoothAdapter mAdapter;
	private final Handler mHandler;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;
	//    private BluetoothDevice mSavedDevice;
	//    private int mConnectionLostCount;

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0;       // we're doing nothing
	public static final int STATE_LISTEN = 1;     // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3;  // now connected to a remote device
	public static final int MESSAGE_FILTER_LIST = 4;

	/**
	 * Constructor. Prepares a new BluetoothChat session.
	 * @param context  The UI Activity Context
	 * @param handler  A Handler to send messages back to the UI Activity
	 */
	public ConnectionService(Context context, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = STATE_NONE;
		//mConnectionLostCount = 0;
		mHandler = handler;
	}

	/**
	 * Set the current state of the chat connection
	 * @param state  An integer defining the current connection state
	 */
	private synchronized void setState(int state) {
		if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
		mState = state;

		// Give the new state to the Handler so the UI Activity can update
		mHandler.obtainMessage(FermataActivity.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}

	/**
	 * Return the current connection state. */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume() */
	public synchronized void start() {
		if (D) Log.d(TAG, "start");

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

		setState(STATE_LISTEN);
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 * @param device  The BluetoothDevice to connect
	 */
	public synchronized void connect(BluetoothDevice device) {
		if (D) Log.d(TAG, "connect to: " + device);

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * Start the ConnectedThread to begin managing the TCP/IP connection 
	 * @param ip The IP address of the server
	 * @param port The port of the server
	 */
	
	public synchronized void connect(String ip, int port) {

		Bundle bundle = new Bundle();

		try {
			setState(STATE_CONNECTING);
			mConnectedThread = new ConnectedThread(new Socket(ip, port));
			mConnectedThread.start();
			
			Message msg = mHandler.obtainMessage(FermataActivity.MESSAGE_DEVICE_NAME);
			bundle.putString(FermataActivity.DEVICE_NAME, ip);
			bundle.putString(FermataActivity.TOAST, "TCP Connection success.");
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			
			setState(STATE_CONNECTED);
			
		} catch (UnknownHostException e) {
			bundle.putString(FermataActivity.TOAST, "TCP Connection failed.");
			setState(STATE_LISTEN);
		} catch (IOException e) {
			bundle.putString(FermataActivity.TOAST, "TCP Connection failed.");
			setState(STATE_LISTEN);
		}
	}

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 * @param socket  The BluetoothSocket on which the connection was made
	 * @param device  The BluetoothDevice that has been connected
	 */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
		if (D) Log.d(TAG, "connected");

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		// Send the name of the connected device back to the UI Activity
		Message msg = mHandler.obtainMessage(FermataActivity.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(FermataActivity.DEVICE_NAME, device.getName());
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		// save connected device
		//mSavedDevice = device;
		// reset connection lost count
		//mConnectionLostCount = 0;

		setState(STATE_CONNECTED);
	}

	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		if (D) Log.d(TAG, "stop");
		if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
		if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

		setState(STATE_NONE);
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * @param out The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED) return;
			r = mConnectedThread;
		}
		// Perform the write unsynchronized
		r.write(out);
	}

	public void write(int out) {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED) return;
			r = mConnectedThread;
		}
		// Perform the write unsynchronized
		r.write(out);
	}

	public void write(String out) {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED) return;
			r = mConnectedThread;
		}
		// Perform the write unsynchronized
		r.write(out);
	}
	
	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		setState(STATE_LISTEN);

		// Send a failure message back to the Activity
		Message msg = mHandler.obtainMessage(FermataActivity.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(FermataActivity.TOAST, "Unable to connect device");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		//        mConnectionLostCount++;
		//        if (mConnectionLostCount < 3) {
		//        	// Send a reconnect message back to the Activity
		//	        Message msg = mHandler.obtainMessage(RemoteBluetooth.MESSAGE_TOAST);
		//	        Bundle bundle = new Bundle();
		//	        bundle.putString(RemoteBluetooth.TOAST, "Device connection was lost. Reconnecting...");
		//	        msg.setData(bundle);
		//	        mHandler.sendMessage(msg);
		//	        
		//        	connect(mSavedDevice);   	
		//        } else {

		setState(STATE_LISTEN);
		// Send a failure message back to the Activity
		Message msg = mHandler.obtainMessage(FermataActivity.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(FermataActivity.TOAST, "Device connection was lost.");
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		//        }
	}

	/**
	 * This thread runs while attempting to make an outgoing connection
	 * with a device. It runs straight through; the connection either
	 * succeeds or fails.
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			Method m;
			try {
				m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
				tmp = (BluetoothSocket) m.invoke(device, 1);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			mmSocket = tmp;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectThread");
			setName("ConnectThread");

			// Always cancel discovery because it will slow down a connection
			mAdapter.cancelDiscovery();

			// Make a connection to the BluetoothSocket
			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				mmSocket.connect();
			} catch (IOException e) {
				connectionFailed();
				// Close the socket
				try {
					mmSocket.close();
				} catch (IOException e2) {
					Log.e(TAG, "unable to close() socket during connection failure", e2);
				}
				// Start the service over to restart listening mode
				ConnectionService.this.start();
				return;
			}

			// Reset the ConnectThread because we're done
			synchronized (ConnectionService.this) {
				mConnectThread = null;
			}

			// Start the connected thread
			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	/**
	 * This thread runs during a connection with a remote device.
	 * It handles all incoming and outgoing transmissions.
	 */
	private class ConnectedThread extends Thread {
		private BluetoothSocket mmSocket;
		private Socket mmTCPSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		private DataOutputStream dataOut;
		private DataInputStream dataIn;

		public ConnectedThread(Socket socket)
		{
			Log.d(TAG, "create ConnectedThread");
			mmTCPSocket = socket;
			mmSocket = null;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the TCPSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
			dataOut = new DataOutputStream(mmOutStream);
			dataIn = new DataInputStream(mmInStream);
			
			Message msg = mHandler.obtainMessage(FermataActivity.MESSAGE_FILTER_LIST);
			Bundle bundle = new Bundle();
			try 
			{
				bundle.putString(FermataActivity.FILTER_LIST, dataIn.readUTF());
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			
		}

		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			mmTCPSocket = null;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;			

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
			dataOut = new DataOutputStream(mmOutStream);
			dataIn = new DataInputStream(mmInStream);
			
			Message msg = mHandler.obtainMessage(FermataActivity.MESSAGE_FILTER_LIST);
			Bundle bundle = new Bundle();
			try 
			{
				bundle.putString(FermataActivity.FILTER_LIST, dataIn.readUTF());
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			
			
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			byte[] buffer = new byte[1024];

			// Keep listening to the InputStream while connected
			while (true) {
				try {
					// Read from the InputStream
					int bytes = mmInStream.read(buffer);

					// Send the obtained bytes to the UI Activity
					mHandler.obtainMessage(FermataActivity.MESSAGE_READ, bytes, -1, buffer)
					.sendToTarget();
				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
					connectionLost();
					break;
				}
			}
		}

		/**
		 * Write to the connected OutStream.
		 * @param buffer  The bytes to write
		 */
		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);

				// Share the sent message back to the UI Activity
				//                mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1, buffer)
				//                        .sendToTarget();
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}

		public void write(int out) {
			try {
				mmOutStream.write(out);

				// Share the sent message back to the UI Activity
				//                mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1, buffer)
				//                        .sendToTarget();
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}
		
		public void write(String out) {
			
			try {
				dataOut.writeUTF(out);
				dataOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void cancel() {
			try {


				mmOutStream.write("stop".getBytes());

				if(mmSocket != null)
					mmSocket.close();
				if(mmTCPSocket != null)
					mmTCPSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}
}
