package com.test.BOLUTEKBLE;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;






import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BlueCont extends Activity {

	private String xprev = "";
	private int prevplayedID = -1;
	private int prevrecogID = -1;
	private int kprev = 0;
	private String savestring[]=new String[1024];
	
	private int SignPrev = -1;
	private int SignCount = 0;
	private int BuffSign[]={-1,-1,-1};
	private boolean ledctrl = true;
	
	
	private double next[][] =new double [100][100];/*{
			{0.00043383 ,0.00872920 ,0.00009565 ,0.04742900 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.12062601 ,0.00000000 ,0.00000000 ,0.02983063 ,0.00014596 ,0.00020678 ,0.00491602 ,0.00000000 ,0.00000000 ,0.01034899 ,0.02357055 ,0.01322765 ,0.00650131 ,0.00000000 ,0.00422879 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00073588 ,0.02229745 ,0.00208804}, 
			{0.00074271 ,0.01822650 ,0.00103352 ,0.00570005 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.15836849 ,0.00000000 ,0.00000000 ,0.01113250 ,0.00019387 ,0.00056932 ,0.00150317 ,0.00000000 ,0.00000000 ,0.00075364 ,0.00152092 ,0.00195099 ,0.00009557 ,0.00000000 ,0.00028125 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00231006 ,0.00005734 ,0.00759915 ,0.00013516}, 
			{0.00008664 ,0.00196074 ,0.00010233 ,0.02614247 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.14123509 ,0.00000000 ,0.00000000 ,0.02796403 ,0.00018352 ,0.00014395 ,0.00033771 ,0.00000000 ,0.00000000 ,0.00474561 ,0.01406627 ,0.00454640 ,0.00249629 ,0.00000000 ,0.00530572 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00010984 ,0.03527756 ,0.00133104}, 
			{0.01056501 ,0.00531232 ,0.02720256 ,0.00042812 ,0.00008362 ,0.00001360 ,0.00002161 ,0.00002601 ,0.00690157 ,0.00003201 ,0.00004441 ,0.10517280 ,0.00056616 ,0.00075462 ,0.04198878 ,0.00000000 ,0.00014044 ,0.00352661 ,0.00190734 ,0.00270237 ,0.00199497 ,0.00000000 ,0.01261440 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00207579 ,0.00048854 ,0.04920563 ,0.00053775}, 
			{0.00040281 ,0.00000000 ,0.00006197 ,0.00319142 ,0.00467869 ,0.00111545 ,0.03386627 ,0.00628989 ,0.01347834 ,0.00000000 ,0.00092954 ,0.00151825 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.01570924 ,0.00046477 ,0.00105348 ,0.00009295 ,0.00000000 ,0.00000000 ,0.00006197 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00136333 ,0.00000000}, 
			{0.00000000 ,0.00000000 ,0.00029985 ,0.01019490 ,0.00749625 ,0.00119940 ,0.09805097 ,0.00512744 ,0.01679160 ,0.00659670 ,0.00509745 ,0.00119940 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00569715 ,0.00000000 ,0.00119940 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00089550 ,0.00000000 ,0.00119940 ,0.00000000}, 
			{0.00000000 ,0.00000000 ,0.00000000 ,0.00819050 ,0.02019048 ,0.01600000 ,0.04609524 ,0.03352381 ,0.01600000 ,0.02590476 ,0.00323810 ,0.00590476 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.05828571 ,0.00095238 ,6.00000000 ,0.00019048 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00228571 ,0.00038095}, 
			{0.00000000 ,0.00015161 ,0.00000000 ,0.00439660 ,0.00485143 ,0.04290479 ,0.01349303 ,0.00485143 ,0.01834445 ,0.01379624 ,0.03790176 ,0.00485143 ,0.00060643 ,0.00015161 ,0.00000000 ,0.00000000 ,0.03411158 ,0.00015161 ,0.00106125 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00090964 ,0.00000000 ,0.00394178 ,0.00015161}, 
			{0.00027341 ,0.00606373 ,0.00101742 ,0.02923097 ,0.00010903 ,0.00000019 ,0.00002218 ,0.00008238 ,0.00110428 ,0.00000280 ,0.00000801 ,0.06729578 ,0.00074867 ,0.00105973 ,0.00133538 ,0.00000000 ,0.00012990 ,0.00253247 ,0.00157282 ,0.00065604 ,0.00010176 ,0.00000000 ,0.00122393 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000876 ,0.00167887 ,0.00104445}, 
			{0.00000000 ,0.00000000 ,0.00008584 ,0.00343335 ,0.00806867 ,0.00309013 ,0.01339056 ,0.06137339 ,0.01038627 ,0.03776820 ,0.00326180 ,0.00094421 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.01278971 ,0.00060086 ,0.00094421 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00008584 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00068670 ,0.00000000 ,0.00120172 ,0.00008584}, 
			{0.00000000 ,0.00000000 ,0.00000000 ,0.00211787 ,0.00096817 ,0.00720077 ,0.00072613 ,0.00217839 ,0.00465932 ,0.00798741 ,0.00121021 ,0.00054460 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00750333 ,0.00024204 ,0.00030255 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00018153 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00114970 ,0.00012102}, 
			{0.00004907 ,0.00008502 ,0.00012065 ,0.01092298 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.01301556 ,0.00000000 ,0.00000000 ,0.00350003 ,0.00032912 ,0.00152060 ,0.00013909 ,0.00000000 ,0.00000313 ,0.00235138 ,0.00014753 ,0.00056948 ,0.00000719 ,0.00000000 ,0.00001813 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00058105 ,0.00000125 ,0.00043321 ,0.00198975}, 
			{0.00006464 ,0.00026501 ,0.00012281 ,0.00673505 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00562978 ,0.00000000 ,0.00000000 ,0.01777485 ,0.00489293 ,0.00420779 ,0.00029732 ,0.00000000 ,0.00000000 ,0.00014220 ,0.00029086 ,0.00018744 ,0.00000646 ,0.00000000 ,0.00003878 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00079502 ,0.00003878}, 
			{0.00002935 ,0.00007045 ,0.00005870 ,0.00132085 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00457307 ,0.00000000 ,0.00000000 ,0.04386979 ,0.00989756 ,0.01073116 ,0.00005870 ,0.00000000 ,0.00000000 ,0.00008806 ,0.00019372 ,0.00008806 ,0.00000587 ,0.00000000 ,0.00002348 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00027004 ,0.00000000 ,0.00010567 ,0.00001761}, 
			{0.00039685 ,0.00286816 ,0.00047999 ,0.02750670 ,0.00000078 ,0.00000000 ,0.00000000 ,0.00000000 ,0.09020380 ,0.00000000 ,0.00000000 ,0.02719768 ,0.00013411 ,0.00013568 ,0.00040940 ,0.00000000 ,0.00000000 ,0.00869858 ,0.02559459 ,0.00786410 ,0.00589866 ,0.00000000 ,0.00894407 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.02589105 ,0.00143369 ,0.02318132 ,0.00212622}, 
			{0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000}, 
			{0.00060861 ,0.00012172 ,0.00000000 ,0.00652433 ,0.01545877 ,0.00328651 ,0.00574531 ,0.02152056 ,0.01633518 ,0.00513669 ,0.00314044 ,0.00163108 ,0.00014607 ,0.00000000 ,0.00004869 ,0.00000000 ,0.01399810 ,0.00026779 ,0.00053558 ,0.00017041 ,0.00002434 ,0.00000000 ,0.00038951 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00099813 ,0.00000000 ,0.00136329 ,0.00024345}, 
			{0.00140309 ,0.00133396 ,0.00433536 ,0.01076126 ,0.00000640 ,0.00000192 ,0.00000064 ,0.00000128 ,0.11577569 ,0.00000064 ,0.00000704 ,0.01606059 ,0.00008257 ,0.00012034 ,0.00635741 ,0.00000000 ,0.00001024 ,0.00097614 ,0.00509643 ,0.00114129 ,0.00004737 ,0.00000000 ,0.00238691 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.01122981 ,0.00184859 ,0.00586902 ,0.00092942}, 
			{0.00000390 ,0.00626713 ,0.00001850 ,0.13307321 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00066118 ,0.00000000 ,0.00000000 ,0.00815622 ,0.00001655 ,0.00001753 ,0.00001071 ,0.00000000 ,0.00000000 ,0.00335948 ,0.00003311 ,0.00132432 ,0.00000097 ,0.00000000 ,0.01720149 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000}, 
			{0.00132304 ,0.00161721 ,0.00263772 ,0.00329018 ,0.00001534 ,0.00000000 ,0.00000279 ,0.00000418 ,0.05531971 ,0.00000000 ,0.00000000 ,0.01362220 ,0.00016869 ,0.00017706 ,0.00325951 ,0.00000000 ,0.00002231 ,0.00051583 ,0.00220136 ,0.00059112 ,0.00008086 ,0.00000000 ,0.00162279 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00420614 ,0.00016033 ,0.00197969 ,0.00038200}, 
			{0.01058435 ,0.00287057 ,0.02483019 ,0.02708198 ,0.00001861 ,0.00000930 ,0.00000465 ,0.00000000 ,0.07896622 ,0.00000930 ,0.00000930 ,0.01239416 ,0.00018145 ,0.00026519 ,0.05017679 ,0.00000000 ,0.00003722 ,0.00228436 ,0.01108216 ,0.00123290 ,0.00002791 ,0.00000000 ,0.00498279 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.01751652 ,0.00127477 ,0.00867219 ,0.00055830}, 
			{0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000}, 
			{0.00000458 ,0.06863396 ,0.00000916 ,0.00498960 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00011220 ,0.00000000 ,0.00000000 ,0.00050606 ,0.00000000 ,0.00000229 ,0.00002519 ,0.00000000 ,0.00000000 ,0.00002061 ,0.00000229 ,0.00000229 ,0.00000000 ,0.00000000 ,0.00003664 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000687 ,0.00000000 ,0.00064574 ,0.00017403}, 
			{0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000}, 
			{0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000}, 
			{0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000}, 
			{0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000}, 
			{0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000}, 
			{0.00058576 ,0.00802452 ,0.00075900 ,0.24497367 ,0.00000000 ,0.00000096 ,0.00000191 ,0.00000000 ,0.00380169 ,0.00000191 ,0.00000287 ,0.01231243 ,0.00008040 ,0.00005264 ,0.00208748 ,0.00000000 ,0.00000383 ,0.00466310 ,0.00040295 ,0.00496842 ,0.00010815 ,0.00000000 ,0.00660223 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00013304 ,0.00002489 ,0.03836627 ,0.00093702}, 
			{0.00062318 ,0.00062318 ,0.00133538 ,0.00188572 ,0.02648106 ,0.00000000 ,0.00000000 ,0.00000000 ,0.11400939 ,0.00000000 ,0.00000809 ,0.00717870 ,0.00004047 ,0.00004856 ,0.01040790 ,0.00000000 ,0.00002428 ,0.02522661 ,0.00889446 ,0.00045322 ,0.00021042 ,0.00000000 ,0.00255746 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.01230172 ,0.00002428 ,0.00099547 ,0.00087407}, 
			{0.00397365 ,0.00060919 ,0.01907616 ,0.00443611 ,0.00016621 ,0.00001043 ,0.00001287 ,0.00003790 ,0.01875975 ,0.00002573 ,0.00002921 ,0.04243753 ,0.00065509 ,0.00097464 ,0.02144999 ,0.00000000 ,0.00023888 ,0.00055634 ,0.00111894 ,0.00274414 ,0.00028095 ,0.00000000 ,0.00298406 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00207132 ,0.01806850 ,0.00208384 ,0.00015334}, 
			{0.00757558 ,0.00172022 ,0.02855610 ,0.00487238 ,0.00040170 ,0.00000236 ,0.00027883 ,0.00000000 ,0.05887496 ,0.00000709 ,0.00019612 ,0.02118610 ,0.00017249 ,0.00017013 ,0.01210297 ,0.00000000 ,0.00001654 ,0.00366728 ,0.00161389 ,0.00015595 ,0.00007561 ,0.00000000 ,0.00139413 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00000000 ,0.00140122 ,0.00024811 ,0.00192343 ,0.00265594},
};*/
	private double p[] = new double[100];
	private double path[] = new double [1024]; 
	private int SignBuffer[][] = new int[2][100];
	private String prevDATA = null;
	private boolean getDATA =false;
	private boolean strspace = true;
	private SoundPool soundPool;
	
	private HashMap<String, Integer> soundPoolMap = new HashMap<String,Integer>();
	private HashMap<Integer,Integer> mysoundHashMap = new HashMap<Integer,Integer>();
	private HashMap<Integer,String> charsoundhashmap = new HashMap<Integer, String>();
	private HashMap<Integer, Integer> ImageHashMap = new HashMap<Integer, Integer>();
	
	private String mDeviceName;
	private String mDeviceAddress;
	private TextView tvaddress;
	private TextView tvstate;
	private EditText tvdata;
	private TextView my_mainTextView;
	private ImageView my_ImageView_Red;
	private ImageView my_SignView;
	private ExpandableListView elist;
	private boolean result;
	private BluetoothLeService mBluetoothLeService;
	
    private BluetoothGattCharacteristic mNotifyCharacteristic;
	
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	// 代码管理服务生命周期。
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			Log.e("a", "初始化蓝牙服务");
			if (!mBluetoothLeService.initialize()) {
				Log.e("a", "无法初始化蓝牙");
				finish();
			}
			// 自动连接到装置上成功启动初始化。
			result = mBluetoothLeService.connect(mDeviceAddress);
			
			
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService.disconnect();
			mBluetoothLeService = null;
		}
	};

	// 处理各种事件的服务了。
	// action_gatt_connected连接到服务器：关贸总协定。
	// action_gatt_disconnected：从关贸总协定的服务器断开。
	// action_gatt_services_discovered：关贸总协定的服务发现。
	// action_data_available：从设备接收数据。这可能是由于阅读
	// 或通知操作。
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				result = true;
				Log.e("a", "来了广播1");
				tvstate.setText("连接");

			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				result = false;
				Log.e("a", "来了广播2");
				mBluetoothLeService.close();
				tvstate.setText("未连接");
				// clearUI();

			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				// 显示所有的支持服务的特点和用户界面。
				Log.e("a", "来了广播3");
				List<BluetoothGattService> supportedGattServices = mBluetoothLeService
						.getSupportedGattServices();
				 displayGattServices(mBluetoothLeService.getSupportedGattServices());
				for(int i=0;i<supportedGattServices.size();i++){
					Log.e("a","1:BluetoothGattService UUID=:"+supportedGattServices.get(i).getUuid());
					List<BluetoothGattCharacteristic> cs = supportedGattServices.get(i).getCharacteristics();
					for(int j=0;j<cs.size();j++){
						Log.e("a","2:   BluetoothGattCharacteristic UUID=:"+cs.get(j).getUuid());
							List<BluetoothGattDescriptor> ds = cs.get(j).getDescriptors();
							for(int f=0;f<ds.size();f++){
								Log.e("a","3:      BluetoothGattDescriptor UUID=:"+ds.get(f).getUuid());
								
								 byte[] value = ds.get(f).getValue();
								
								 Log.e("a","4:     			value=:"+Arrays.toString(value));
								 Log.e("a","5:     			value=:"+Arrays.toString( ds.get(f).getCharacteristic().getValue()));
							}
					}
				}
				
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				Log.e("a", "来了广播4--->data:"+intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
				i++;		
//my code
				//接收
				DATA = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
				tvdata.setText(DATA);
				String[] strArray = DATA.split("\\s+");
				int SignID = Integer.parseInt(strArray[0]);
				int SignID2 = Integer.parseInt(strArray[1]);
		
				if(SignID!=SignPrev &&!(SignCount==0 && SignID == prevrecogID)){
					my_ImageView_Red.setImageResource(R.drawable.red);
					SignBuffer[0][SignCount] = SignID;
					SignBuffer[1][SignCount] = SignID2;
					SignCount++;
					SignPrev = SignID;
				}else{

					    if(SignCount<=2 && SignCount!=0){
							if(SignCount == 1)playcharsound(SignBuffer[0][0]);
							else{
								p[0] = 0.67*0.67*next[SignBuffer[0][0]][SignBuffer[0][1]];
								p[1] = 0.67*0.33*next[SignBuffer[0][0]][SignBuffer[1][1]];
								p[2] = 0.33*0.67*next[SignBuffer[1][0]][SignBuffer[0][1]];
								p[3] = 0.33*0.33*next[SignBuffer[1][0]][SignBuffer[1][1]];
								double pmax = p[0];
								int pmaxi= 0;
								for(int i=0;i<4;i++){
									if(p[i]>pmax){
										pmax = p[i];
										pmaxi = i;
									}
								}
								if(p[pmaxi]==0)my_mainTextView.setText("无识别结果");
								else {
									if(pmaxi==0)playcharsound(SignBuffer[0][0]+100*SignBuffer[0][1]);
									if(pmaxi==1)playcharsound(SignBuffer[0][0]+100*SignBuffer[1][1]);
									if(pmaxi==2)playcharsound(SignBuffer[1][0]+100*SignBuffer[0][1]);
									if(pmaxi==3)playcharsound(SignBuffer[1][0]+100*SignBuffer[1][1]);
								}
							}
						}else{
							for(int i=0;i<SignCount;i++){
								if(i == 0){
									path[0] = 0.67;
									path[1] = 0.33;
								}else{
									int k=2*i;
									double p0 = 1000*0.67*path[k-2]*next[SignBuffer[0][i-1]][SignBuffer[0][i]];
									double p1 = 1000*0.33*path[k-1]*next[SignBuffer[1][i-1]][SignBuffer[0][i]];
									double p2 = 1000*0.67*path[k-2]*next[SignBuffer[0][i-1]][SignBuffer[1][i]];
									double p3 = 1000*0.33*path[k-1]*next[SignBuffer[1][i-1]][SignBuffer[1][i]];
									
									if(p0>p1){
										path[k]=p0;
										savestring[k]=savestring[k-2];
										if(charsoundhashmap.containsKey(SignBuffer[0][i]))savestring[k]+=charsoundhashmap.get(SignBuffer[0][i-1]);
									}
									else{
										path[k]=p1;
										savestring[k]=savestring[k-1];
										if(charsoundhashmap.containsKey(SignBuffer[1][i]))savestring[k]+=charsoundhashmap.get(SignBuffer[1][i-1]);
									}
									
									if(p2>p3){
										path[k+1]=p2;
										savestring[k+1]=savestring[k-2];
										if(charsoundhashmap.containsKey(SignBuffer[0][i]))savestring[k+1]+=charsoundhashmap.get(SignBuffer[0][i-1]);
									}
									else{
										path[k+1]=p3;
										savestring[k+1]=savestring[k-1];
										if(charsoundhashmap.containsKey(SignBuffer[0][i]))savestring[k+1]+=charsoundhashmap.get(SignBuffer[1][i-1]);
									}
									
									if(i == SignCount-1){
										if(0.67*path[k]>0.33*path[k+1]){
											my_mainTextView.setText(savestring[k]+charsoundhashmap.get(SignBuffer[0][i]));
										}
										else {
											my_mainTextView.setText(savestring[k+1]+charsoundhashmap.get(SignBuffer[1][i]));
										}
										my_SignView.setImageResource(R.drawable.a4);
									}
								}
							
							}
						}
						
						
						for(int i=0;i<SignCount;i++){
							SignBuffer[0][i] = -1;
							SignBuffer[1][i] = -1;
						}
						for(int i=0;i<(2*SignCount+2);i++){
							savestring[i]="[暂无音频]";
						}
						SignCount = 0;
						prevrecogID = SignID;
						my_ImageView_Red.setImageResource(R.drawable.gray);
				}
/*
				//手势缓冲区
				if (findnext(SignID)&&(SignID!=prevplayedID)){
					BuffSign[0] = SignID;
					my_ImageView_Red.setImageResource(R.drawable.red);
					if (findnext(SignID2)){
						BuffSign[1] = SignID2;
					}
				}

				//计数
				if(SignPrev==SignID){
					SignCount++;
					my_ImageView_Red.setImageResource(R.drawable.gray);
				}
				else{
					SignCount = 1;
				}
				SignPrev = SignID;
				
				//识别s
				if(2 == SignCount){
////认为发射概率为1的代码
//					if(BuffSign[0] == -1){
//					    playcharsound(SignID);
//					    SignCount = 0;
//					}
//					else {
//						if (next[BuffSign[0]][SignID]==1)playcharsound(BuffSign[0]+SignID*100);
//						else playcharsound(SignID);
//						BuffSign[0] = -1;
//						SignCount = 0;
//					}
					if(BuffSign[0]!=-1){
						p[0] = 67*67*next[BuffSign[0]][SignID];
						p[1] = 67*33*next[BuffSign[0]][SignID2];
					}
					else{
						p[0] = 0;
						p[1] = 0;
					}
					if(BuffSign[1]!=-1){
						p[2] = 33*67*next[BuffSign[1]][SignID];
						p[3] = 33*33*next[BuffSign[1]][SignID2];
					}
					else{
						p[2] = 0;
						p[3] = 0;
					}
					
					int pmax = p[0];
					int pmaxi= 0;
					for(int i=0;i<4;i++){
						if(p[i]>pmax){
							pmax = p[i];
							pmaxi = i;
						}
					}
					if(p[pmaxi]==0)
						playcharsound(SignID);
					else{
						if(pmaxi==0)playcharsound(BuffSign[0]+SignID*100);
						if(pmaxi==1)playcharsound(BuffSign[0]+SignID2*100);
						if(pmaxi==2)playcharsound(BuffSign[1]+SignID*100);
						if(pmaxi==3)playcharsound(BuffSign[1]+SignID2*100);
					}
					BuffSign[0] = -1;
					BuffSign[1] = -1;
					SignCount = 0;
				}
*/
			}else if(BluetoothLeService.ACTION_RSSI.equals(action)){
				Log.e("a", "来了广播5");
				tvrssi.setText("RSSI:"+intent
						.getStringExtra(BluetoothLeService.ACTION_DATA_RSSI));
			}
		}
	};
    private ExpandableListView mGattServicesList;
	private EditText et_send;
	private String DATA;
	private int i;
	private ArrayList<BluetoothGattCharacteristic> charas;
	
	//查找当前状态是否有可能的下一状态
	private boolean findnext(int SignID){
		for(int i=0;i<100;i++){
			if(next[SignID][i]==1)return true;
		}
		return false;
	}
	
	//用哈希表关联键值对  int - resid
    private void setSoundHashMap(){
    	
        for(int i=0;i<1024;i++){
        	savestring[i] = "[暂无音频]";
        }
    	
    	for(int i=0;i<100;i++){
    		SignBuffer[0][i]=-1;
    		SignBuffer[1][i]=-1;
    	}
       	//转移矩阵
    	for(int i=0;i<100;i++)
    		for(int j=0;j<100;j++){
    			next[i][j]=1;
    		}
    	next[5][5]=1;
    	next[0][1]=1;
    	next[15][21]=1;
    	next[20][21]=1;
    	next[23][24]=1;
    	next[25][26]=1;
    	next[23][16]=1;
    	next[5][27]=1;
    	
        mysoundHashMap.put(16,0x7f050000);
        mysoundHashMap.put(18,0x7f050001);
        mysoundHashMap.put(4,0x7f050002);
        mysoundHashMap.put(8,0x7f050003);
        mysoundHashMap.put(9,0x7f050004);
        mysoundHashMap.put(13,0x7f050005);
        mysoundHashMap.put(10,0x7f050006);
        mysoundHashMap.put(19,0x7f050007);
        mysoundHashMap.put(1,0x7f050008);
        mysoundHashMap.put(17,0x7f050009);
        mysoundHashMap.put(0,0x7f05000a);
        mysoundHashMap.put(20,0x7f05000b);
        mysoundHashMap.put(11,0x7f05000c);
        mysoundHashMap.put(6,0x7f05000d);
        mysoundHashMap.put(3,0x7f05000e);
        mysoundHashMap.put(12,0x7f05000f);
        mysoundHashMap.put(7,0x7f050010);
        mysoundHashMap.put(2,0x7f050011);
        mysoundHashMap.put(5,0x7f050012);
        mysoundHashMap.put(14,0x7f050013);
        mysoundHashMap.put(2115,0x7f050014);
        mysoundHashMap.put(100, 0x7f050015);
        mysoundHashMap.put(2120, 0x7f050016);
        mysoundHashMap.put(22, 0x7f050017);
        mysoundHashMap.put(2423, 0x7f050018);
        mysoundHashMap.put(2625, 0x7f050019);
        mysoundHashMap.put(1623, 0x7f05001a);
        mysoundHashMap.put(2705, 0x7f05001b);
        mysoundHashMap.put(28, 0x7f05001c);
        mysoundHashMap.put(29, 0x7f05001d);
        mysoundHashMap.put(30, 0x7f05001e);
        mysoundHashMap.put(31, 0x7f05001f);
        
    	charsoundhashmap.put(0, "你");//
    	charsoundhashmap.put(1, "好");//
    	charsoundhashmap.put(2, "他");//
    	charsoundhashmap.put(3, "是");//
    	charsoundhashmap.put(4, "B");//
    	charsoundhashmap.put(5, "U");//
    	charsoundhashmap.put(6, "P");//
    	charsoundhashmap.put(7, "T");//
    	charsoundhashmap.put(8, "的");//
    	charsoundhashmap.put(9, "E");
    	charsoundhashmap.put(10, "F");
    	charsoundhashmap.put(11, "一");
    	charsoundhashmap.put(12, "六");
    	charsoundhashmap.put(13, "八");
    	charsoundhashmap.put(14, "我");//
    	charsoundhashmap.put(2115, "学生");//
    	charsoundhashmap.put(16, "A");
    	charsoundhashmap.put(17, "来");//
    	charsoundhashmap.put(18, "也");//
    	charsoundhashmap.put(19, "去");//
    	charsoundhashmap.put(20, "现在");//
    	charsoundhashmap.put(100,"你好");//
    	charsoundhashmap.put(2120,"什么");
    	charsoundhashmap.put(22, "很");//
    	charsoundhashmap.put(2423, "不");//
    	charsoundhashmap.put(2625, "抱歉");//
    	charsoundhashmap.put(1623, "可以");//---------------------------
    	charsoundhashmap.put(2705, "或者");
    	charsoundhashmap.put(28, "就");//
    	charsoundhashmap.put(29, "这里");//
    	charsoundhashmap.put(30, "在");//
    	charsoundhashmap.put(31, "见");//
    	
    	ImageHashMap.put(0, 0x7f02007e);
    	ImageHashMap.put(1, 0x7f020066);
    	ImageHashMap.put(2, 0x7f020069);
    	ImageHashMap.put(3, 0x7f02007d);
    	ImageHashMap.put(4, 0x7f02005d);
    	ImageHashMap.put(5, 0x7f02007a);
    	ImageHashMap.put(6, 0x7f020073);
    	ImageHashMap.put(7, 0x7f020079);
    	ImageHashMap.put(8, 0x7f020061);
    	ImageHashMap.put(9, 0x7f020062);
    	ImageHashMap.put(10, 0x7f020064);
    	ImageHashMap.put(11, 0x7f020071);
    	ImageHashMap.put(12, 0x7f020076);
    	ImageHashMap.put(13, 0x7f020063);
    	ImageHashMap.put(14, 0x7f02006d);
    	ImageHashMap.put(2115, 0x7f020078);
    	ImageHashMap.put(16, 0x7f020000);
    	ImageHashMap.put(17, 0x7f020060);
    	ImageHashMap.put(18, 0x7f02005c);
    	ImageHashMap.put(19, 0x7f020065);
    	ImageHashMap.put(20, 0x7f020070);
    	ImageHashMap.put(100, 0x7f02006e);
    	ImageHashMap.put(2120, 0x7f02007c);
    	ImageHashMap.put(22, 0x7f02007b);
    	ImageHashMap.put(2423, 0x7f02006f);
    	ImageHashMap.put(2625, 0x7f020077);
    	ImageHashMap.put(1623, 0x7f02005f);
    	ImageHashMap.put(2705, 0x7f020072);
    	ImageHashMap.put(28, 0x7f02006c);
    	ImageHashMap.put(29, 0x7f02006a);
    	ImageHashMap.put(30, 0x7f02005e);
    	ImageHashMap.put(31, 0x7f020075);
    	
    }
    
    
    private void playcharsound(int tempint){
    	//防止重复播放
    	if(charsoundhashmap.containsKey(tempint)  && tempint!=prevplayedID){
    		my_mainTextView.setText(charsoundhashmap.get(tempint));
    		if(tempint >= 100){
    			prevplayedID = tempint /100;
    		}
    		else prevplayedID = tempint;
    		
    		//↓此处可用SoundPool替代
    		my_SignView.setImageResource(ImageHashMap.get(tempint));
    		playsound(tempint);
    	}
    }
    
    //soundpool播放音频使用办法见文章http://www.2cto.com/kf/201408/325318.html
    private void soundpoolplay(int i){
		if(mysoundHashMap.containsKey(i)){
			soundPoolMap.put("1",  soundPool.load(getBaseContext(), mysoundHashMap.get(i), 1));
			soundPool.play(soundPoolMap.get("1"),1,1, 0, 0, 1);
			//主要是ID的释放，具体原因见文章http://blog.csdn.net/lovewaterman/article/details/38021305/
			if(10 == soundPoolMap.get("1")){
				soundPool.release();
				soundPool=new SoundPool(12 , AudioManager.STREAM_MUSIC , 5);
				soundPoolMap.put("1",  soundPool.load(getBaseContext(), mysoundHashMap.get(i), 1));
				soundPool.play(soundPoolMap.get("1"),1,1, 0, 0, 1);
			}
		}
    }
    
    //由键找值 播放音频
	private void playsound(int key){
        int value = -1;
    	MediaPlayer myMediaPlayer;
	    if(mysoundHashMap.containsKey(key)){
	    		value = mysoundHashMap.get(key);
	    		myMediaPlayer = MediaPlayer.create(getBaseContext(), value);
	    		myMediaPlayer.start();
	    		if(!myMediaPlayer.isPlaying())myMediaPlayer.release();
	    }
    }
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gatt_services_characteristics2);

		Intent intent = getIntent();
		mDeviceName = intent.getStringExtra("name");
		mDeviceAddress = intent.getStringExtra("andrass");

		Log.e("a", "名字"+mDeviceName+"地址"+mDeviceAddress);
		getActionBar().setTitle(mDeviceName);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		DATA = "";
		i = 0;
		
		tvaddress = (TextView) findViewById(R.id.device_address);
		tvaddress.setText(mDeviceAddress);

		tvstate = (TextView) findViewById(R.id.connection_state);
		tvdata = (EditText) findViewById(R.id.data_value);
		
		//mycode
		my_ImageView_Red = (ImageView) findViewById(R.id.imageView1);
		my_mainTextView = (TextView) findViewById(R.id.my_mainTextView);
		my_SignView = (ImageView) findViewById(R.id.imageView2);
		soundPool=new SoundPool(12 , AudioManager.STREAM_MUSIC , 5);
		setSoundHashMap();
		
		tvdata.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		//tvdata.setSelected(true);
		tvdata.requestFocus();//get the focus
		tvrssi = (TextView) findViewById(R.id.data_rssi);

		
		mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
        mGattServicesList.setOnChildClickListener(servicesListClickListner);
		
        et_send = (EditText) findViewById(R.id.et_send);
		Button btsend = (Button) findViewById(R.id.btsend);
		
		btsend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				byte[]bb= new byte[]{(byte)1,2,3};
				String sendstr = et_send.getText().toString();
				Boolean boolean1 = mBluetoothLeService.write(mNotifyCharacteristic,sendstr);
				Log.e("a", "发送UUID"+mNotifyCharacteristic.getUuid().toString()+"是否发送成功::"+boolean1);
			}
		});
		
		 flg = true;
		Button btrssi=(Button) findViewById(R.id.btrssi);
		btrssi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						while (flg) {
						// TODO Auto-generated method stub
							try {
								Thread.sleep(1000);	
								flg=mBluetoothLeService.readrssi();	
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.e("a","断网了");
							}
						}
					
					}
				}).start();
			}
		});
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/**
		 * 注册广播
		 */
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		if (mBluetoothLeService != null) {
			Log.e("a", "来了");
			result = mBluetoothLeService.connect(mDeviceAddress);
			Log.e("a", "连接请求的结果=" + result);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:

			if (result) {
				result = false;
				mBluetoothLeService.disconnect();
			}
			onBackPressed();
			break;
		case R.id.action_cont:
			result = mBluetoothLeService.connect(mDeviceAddress);

			break;

		case R.id.action_close:
			if (result) {
				result = false;
			//	mBluetoothLeService.disconnect();
				Log.e("a", "断开了");
				mBluetoothLeService.close();
				tvstate.setText("连接断开");
			}

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 销毁广播接收器
	 */
	@Override
	protected void onPause() {
		super.onPause();
		flg=false;
		unregisterReceiver(mGattUpdateReceiver);
	
	}
	/**
	 * 结束服务
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		unbindService(mServiceConnection);

	}
	
    // 我们是注定的expandablelistview数据结构
	//  在UI。
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
        	return;
        String uuid = null;
        String unknownServiceString = "service_UUID";
        String unknownCharaString = "characteristic_UUID";
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // 循环遍历服务
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    "NAME", SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put("UUID", uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // 循环遍历特征
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        "NAME", SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put("UUID", uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
        
        final BluetoothGattCharacteristic characteristic = charas.get(charas.size()-1);
        final int charaProp = characteristic.getProperties();
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            if (mNotifyCharacteristic != null) {
                mBluetoothLeService.setCharacteristicNotification(
                        mNotifyCharacteristic, false);
                mNotifyCharacteristic = null;
            }
            mBluetoothLeService.readCharacteristic(characteristic);

        }
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            mNotifyCharacteristic = characteristic;
            mBluetoothLeService.setCharacteristicNotification(
                    characteristic, true);
        }

       /* SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {"NAME", "UUID"},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {"NAME", "UUID"},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        mGattServicesList.setAdapter(gattServiceAdapter);*/
    }
    
    
    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                	Log.e("a","点击了");
                    if (mGattCharacteristics != null) {
                        final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            if (mNotifyCharacteristic != null) {
                                mBluetoothLeService.setCharacteristicNotification(
                                        mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothLeService.readCharacteristic(characteristic);

                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothLeService.setCharacteristicNotification(
                                    characteristic, true);
                        }
                        return true;
                    }
                    return false;
                }
                
    };
	private boolean flg;
	private TextView tvrssi;

    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        tvdata.setText("木有数据");
    }

	/**
	 * 注册广播
	 * @return
	 */
    
    
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.ACTION_RSSI);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_RSSI);
		return intentFilter;
	}
	
}
