package com.example.iseek;

import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.Toast;

public class StaticVar {

	//菜单order
	public static final int MENU_REFRESH  = 100;
	public static final int MENU_SETTINGS = 200;
	public static final int MENU_TEST     = 300;
	public static final int MENU_EXIT     = 400;
	
	//发送短信
	public static final String SMS_GEO_REQU = "w000000,051";//请求位置
	public static final String SMS_SET_SOS = "w000000,003,2,1,";//设置sos号码
	
	//接受短信权限设置
	public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
		
	//设置存储文件接口
	public static SharedPreferences prefs = null;
	
	//百度地图	
	public static MapController mMapController = null;
	public static MyLocationOverlay myLocationOverlay = null;
	public static LocationData tarLocData = null;
	
	//控件对应的key字符串声明
	public static String prefTargetPhoneKey = null;
	public static String prefSosNumberKey   = null;
	public static String prefSaveAllKey     = null;
	public static String prefAboutKey       = null;	
	
	//短信头解析字符串
	public static final String SMS_Header_LOC_SUCCESS = "W00,051";
	
	//短信发送函数
	public static void SendMessage(Context context, String mesContext)
	{
		//获取手机号码		
		
		String strDestAddress = StaticVar.prefs.getString(prefTargetPhoneKey, "unset");

		if(strDestAddress.equals("unset"))
		{
			Toast.makeText(context, "Please Set Phone Number" , Toast.LENGTH_SHORT).show();
			return ;
		}
		
		//默认指令，gps回传经度和纬度
	    String strMessage = mesContext; 		  
	    SmsManager smsManager = SmsManager.getDefault();		     
	    try 
	    { 
	    	//发送短信
		    PendingIntent mPI = PendingIntent.getBroadcast(context, 0, new Intent(), 0); 
		    smsManager.sendTextMessage(strDestAddress, null, strMessage, mPI, null);
	    } 
	    catch(Exception e) 
	    {
	    	e.printStackTrace();
	    }
	    Toast.makeText(context, "送出成功!!" , Toast.LENGTH_SHORT).show();
	}
	
	public static void setNewPosition(double Latitude, double Longitude)
	{
		System.out.println("Latitude:" + Latitude + "  Longitude:" + Longitude);
		
		
		tarLocData.latitude = Latitude;
		tarLocData.longitude = Longitude;
		tarLocData.direction = 2.0f;		
			
//		mMapController.setCenter(new GeoPoint((int)(Latitude* 1E6),(int)(Longitude* 1E6)));//设置地图中心点
		
		myLocationOverlay.setData(StaticVar.tarLocData);
		MainActivity.mMapView.getOverlays().add(StaticVar.myLocationOverlay);
		MainActivity.mMapView.refresh();
		mMapController.animateTo(new GeoPoint((int)(Latitude* 1E6),(int)(Longitude* 1E6)));
		mMapController.setZoom(18);//设置地图zoom级别
	}
		
}
