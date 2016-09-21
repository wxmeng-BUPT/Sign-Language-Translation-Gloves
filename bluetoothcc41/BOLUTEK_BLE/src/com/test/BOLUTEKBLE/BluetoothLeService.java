/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.BOLUTEKBLE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 用于管理连接的服务和数据与服务器通信托管在关贸总协定
 * 给蓝牙LE装置。
 */
@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothLeService extends Service {
//    private final static String TAG = BluetoothLeService.class.getSimpleName();
  private final static String TAG ="a";
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
//    public static final UUID SERVIE_UUID = UUID
//			.fromString("0000FFF0-0000-1000-8000-00805f9b34fb");
    public static final UUID SERVIE_UUID = UUID
		.fromString("00001801-0000-1000-8000-00805f9b34fb");
    
//	public static final UUID RED_LIGHT_CONTROL_UUID = UUID
//			.fromString("0000FFF4-0000-1000-8000-00805f9b34fb");
    
    public final static String ACTION_DATA_RSSI =
            "com.example.bluetooth.le.ACTION_DATA_RSSI";
    
    public final static String ACTION_RSSI =
            "com.example.bluetooth.le.ACTION_RSSI";
    
    
//    public static final UUID RED_LIGHT_CONTROL_UUID = UUID
//			.fromString("0000FFF4-0000-1000-8000-00805f9b34fb");
//	public static final UUID RED_LIGHT_CONTROL_UUID_TWO = UUID
//			.fromString("0000FFF1-0000-1000-8000-00805f9b34fb");
    
    public static final UUID RED_LIGHT_CONTROL_UUID = UUID
			.fromString("bef8d6c9-9c21-4c9e-b632-bd58c1009f9f");
	public static final UUID RED_LIGHT_CONTROL_UUID_TWO = UUID
			.fromString("bef8d6c9-9c21-4c9e-b632-bd58c1009f9f");
    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    
    public static final UUID ACC_MEASUREMENT_CHARAC = UUID.fromString("F000AA51-0451-4000-B000-000000000000");
    public static final UUID ACC_MEASUREMENT_CHARAC2 = UUID.fromString("F000AA52-0451-4000-B000-000000000000");

    // 连接的变化和服务发现。
    @SuppressLint("NewApi")
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @SuppressLint("NewApi")
		@Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) 
            	{
            	 Log.i(TAG, "onConnectionStateChange");
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.e(TAG, "服务器的连接.");
                // 试图发现服务连接成功后。
                Log.e(TAG, "启动服务发现:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.e(TAG, "服务器断开.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
            	 Log.i(TAG, "onServicesDiscovered");
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
            	 Log.e(TAG, "onservicesdiscovered收到: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
        	 Log.i(TAG, "onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }
        /**
         * 返回数据。
         */
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
        	 Log.i(TAG, "onCharacteristicChanged");
        	/**
        	 * 正常型号蓝牙接收
        	 */
           broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        	/**
        	 * bolutek ------>>> DM-710-A 
        	 * 格式UInt8
        	 */
            /*byte[] data = characteristic.getValue();
            //鏁版嵁
            String str2 = new String(data);
            Log.e(TAG, "返回读出的值:"+characteristic.getValue().toString());
            Log.e(TAG, "返回读出的值data:"+Arrays.toString(data));
            Log.e(TAG, "返回读出的值data2:"+str2);
            
            Intent intent = new Intent(ACTION_DATA_AVAILABLE);
            intent.putExtra(EXTRA_DATA, String.valueOf(str2));
            sendBroadcast(intent);*/
        }
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        	broadcastUpdate(ACTION_RSSI,rssi+"");
        	 Log.e("a", "返回读出的值:"+rssi);
        };
    };
    
    //更新广播
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    //更新广播
    private void broadcastUpdate(final String action,final String rssi) {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ACTION_DATA_RSSI, rssi);
        sendBroadcast(intent);
    }

    //更新广播
	private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        //这是心率测量资料特殊处理。数据分析
        //按型材规格：
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "格式UInt16");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "格式UInt8--");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("接收的数据: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
        	//其他所有的配置文件，将数据格式化为十六进制。
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data));// + "\n" + stringBuilder.toString());
                Log.i(TAG, "onCharacteristicRead:"+new String(data));
            }
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
    	// 后使用一个给定的设备，你应该确保bluetoothgatt（）调用。
    	// 等资源的打扫干净。在这个特殊的例子中，（）是
    	// 调用时，用户界面是从服务断开。
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * 蓝牙转换接头。
     * 返回是否成功
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
            	  Log.e(TAG, "bluetoothmanager无法初始化");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
        	   Log.e(TAG, "无法获得 ：mBluetoothAdapter");
            return false;
        }

        return true;
    }

    /**
     *连接上蓝牙LE装置关贸总协定的服务器。
     *
     * @param地址的设备地址的目的地设备。
     *
     * @返回返回true，如果连接启动成功。连接结果
     *通过异步的报道
     *
     *回调。
     */
	public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.e(TAG, "你没有初始化或未指定的地址。");
            return false;
        }

        //以前的连接装置。尝试重新连接。
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
        	   Log.e(TAG, "试图使用一个现有的mbluetoothgatt连接.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.e(TAG, "没有找到设备。无法连接。");
            return false;
        }
        // 我们要直接连接到设备，所以我们设置自动连接
        // 参数错误。
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.e(TAG, "试图创建一个新的连接。");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        System.out.println("device.getBondState=="+device.getBondState());
        return true;
    }

	  /**
	    *断开一个现有连接或取消挂起的连接。断开的结果
	    *通过异步的报道
	    *
	    *回调。
	    */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
        	  Log.e(TAG, "你没有初始化");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     *使用给定的BLE装置后，应用程序必须调用这个方法来确保资源
     *正确释放。
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     *要求在一个给定的{“代码bluetoothgattcharacteristic读取}。报告阅读的结果是
     *异步通过
     *回调。
     *
     * @param特征读取。
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
        	  Log.e(TAG, "你没有初始化");
            return;
        }
        Log.e("a", "正在读");
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     *启用或禁用通知给特性。
     *
     * @param特征的行为。
     * @param启用如果为真，启用通知。否则为false。
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "你没有初始化");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        
        // 这是特定的心脏率测量。
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }


    /**
    *检索的连接设备支持的关贸总协定的服务列表。这应该是
    *调用只有在{”代码bluetoothgatt # discoverservices() }成功完成。
    *
    *”返回的代码列表支持的服务“{ }。
    */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }
    
    /**
     * 发送数据
     * @param characteristic
     * @param bb
     * @return
     */
    public Boolean write(BluetoothGattCharacteristic characteristic ,String bb){
    	if(mBluetoothGatt==null){
    		Log.e(TAG, "mBluetoothGatt==空");
			return false;
    	}
    	if(characteristic==null){
    		Log.e(TAG, "characteristic==空");
			return false;
    	}
    	 // 这是特定的心脏率测量。

        	Log.e("a", "进来了。、。。。");

        	characteristic.setValue(bb);

            return   mBluetoothGatt.writeCharacteristic(characteristic);

    	
    }
    
    public boolean readrssi(){
    	if(mBluetoothGatt==null){
    		return false;
    	}
    	return mBluetoothGatt.readRemoteRssi();
    }
    public Boolean writeLlsAlertLevel(int iAlertLevel, byte[] bb) {

		// Log.i("iDevice", iDevice);
    	if(mBluetoothGatt==null){
    		Log.e(TAG, "mBluetoothGatt==空");
			return false;
    	}
    	
		BluetoothGattService linkLossService = mBluetoothGatt
				.getService(SERVIE_UUID);
		if (linkLossService == null) {
			Log.e(TAG, "服务没有发现！");
			return false;
		}
	
		// enableBattNoti(iDevice);
		BluetoothGattCharacteristic alertLevel = null;
		switch (iAlertLevel) {
		case 1: // red
			alertLevel = linkLossService.getCharacteristic(RED_LIGHT_CONTROL_UUID);
			break;
		case 2:
			alertLevel = linkLossService.getCharacteristic(RED_LIGHT_CONTROL_UUID_TWO);
			break;
		}
		if (alertLevel == null) {
			Log.e(TAG, "特征没有发现！");
			return false;
		}
		boolean status = false;
		int storedLevel = alertLevel.getWriteType();
		Log.e(TAG, "storedLevel() - storedLevel=" + storedLevel);

		alertLevel.setValue(bb);

		alertLevel.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
		status = mBluetoothGatt.writeCharacteristic(alertLevel);
		Log.e(TAG, "writeLlsAlertLevel() - status=" + status);
		return status;
	}
    
    private void showMessage(String msg) {
		Log.e(TAG, msg);
	}
}
