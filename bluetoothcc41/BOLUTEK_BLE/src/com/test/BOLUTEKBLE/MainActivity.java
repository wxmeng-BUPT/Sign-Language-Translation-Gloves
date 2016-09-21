package com.test.BOLUTEKBLE;





import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	    private BluetoothAdapter mBluetoothAdapter;
	    private boolean mScanning;
	    private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 mHandler = new Handler();
		 
		 if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
	            Toast.makeText(this,"没有蓝牙", Toast.LENGTH_SHORT).show();
	            finish();
	        }
		 
		 // 初始化一个蓝牙适配器。对API 18级以上，可以参考 bluetoothmanager。
	        final BluetoothManager bluetoothManager =
	                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
	        mBluetoothAdapter = bluetoothManager.getAdapter();

	       //  检查是否支持蓝牙的设备。
	        if (mBluetoothAdapter == null) {
	            Toast.makeText(this,"设备不支持", Toast.LENGTH_SHORT).show();
	            finish();
	            return;
	        }
	        
	        
	        Button btstart=(Button) findViewById(R.id.btstart);
	        Button btstop=(Button) findViewById(R.id.btstop);
	        bar = (ProgressBar) findViewById(R.id.bar);
	        
	        
			bar.setVisibility(View.GONE);
	        
	        
	        
	        btlist = (ListView) findViewById(R.id.list);
	        listItem = new ArrayList<HashMap<String, Object>>();  
	        adapter = new SimpleAdapter(this,listItem,android.R.layout.simple_expandable_list_item_2,
	        new String[]{"name","andrass"},new int[]{android.R.id.text1,android.R.id.text2});
   	        btlist.setAdapter(adapter);
   	        
   	        
   	        btlist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					BluetoothDevice device=	(BluetoothDevice) listItem.get(arg2).get("device");
					Log.e("a", "点击的按钮"+arg2+device.getAddress()+"cacaca");
				
					Intent intent=new Intent(getApplicationContext(), BlueCont.class);
			
					intent.putExtra("andrass",device.getAddress());
					intent.putExtra("name",device.getName());
					
					
					  if (mScanning) {
				            mBluetoothAdapter.stopLeScan(mLeScanCallback);
				            mScanning = false;
				        }

					startActivity(intent);
				}
			});
   	        
	        btstart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					scanLeDevice(true);
					Log.e("a", "开始搜寻");
				}
			});
	        
	        btstop.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					scanLeDevice(false);
					Log.e("a", "停止");
				}
			});
	}
	@Override
	protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	
	//确保蓝牙是在设备上启用。如果当前没有启用蓝牙，
    //火意图显示一个对话框询问用户授予权限以使它。
	      if (!mBluetoothAdapter.isEnabled()) {
	          if (!mBluetoothAdapter.isEnabled()) {
	              Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	              startActivityForResult(enableBtIntent, 1);
	          }
	      }
	//      scanLeDevice(true);
	}
	
	
	 private void scanLeDevice(final boolean enable) {
	        if (enable) {
	            //停止后一个预定义的扫描周期扫描。
	            mHandler.postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                    mScanning = false;
	                    bar.setVisibility(View.GONE);
	                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
	                    invalidateOptionsMenu();
	                }
	            }, 10000);
	        	bar.setVisibility(View.VISIBLE);
	            mScanning = true;
	            mBluetoothAdapter.startLeScan(mLeScanCallback);
	        } else {
				bar.setVisibility(View.GONE);
	            mScanning = false;
	            mBluetoothAdapter.stopLeScan(mLeScanCallback);
	        }
	        
	        invalidateOptionsMenu();
	    }

	  // 扫描装置的回调。
	    private BluetoothAdapter.LeScanCallback mLeScanCallback =
	            new BluetoothAdapter.LeScanCallback() {

	        @Override
	        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
	            runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                	   HashMap<String, Object> map = new HashMap<String, Object>();  
	                	  	
	                       Log.e("a", "RSSI=:"+rssi+"");
	                       
	                       map.put("name", device.getName());
	                       map.put("andrass",device.getAddress());  
	                       map.put("device", device);
	                       listItem.add(map);
	                       adapter.notifyDataSetChanged();
	                    Log.e("a","发现蓝牙"+device.getAddress()+"状态"+device.getBondState()+"type"+device.getType()+device.describeContents());
	                }
	            });
	        }
	    };
		private ListView btlist;
		private ArrayList<HashMap<String, Object>> listItem;
		private SimpleAdapter adapter;
		private ProgressBar bar;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 将菜单；这将项目添加到动作条如果真的存在。
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
