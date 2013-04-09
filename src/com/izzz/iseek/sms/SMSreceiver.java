package com.izzz.iseek.sms;


import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.iseek.R;
import com.izzz.iseek.map.IseekApplication;
import com.izzz.iseek.map.BaseMapMain;
import com.izzz.iseek.setting.SettingActivity;
import com.izzz.iseek.vars.StaticVar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;



/* �Զ���̳���BroadcastReceiver��,����ϵͳ����㲥����Ϣ */
public class SMSreceiver extends BroadcastReceiver 
{ 
	
	@Override 
	public void onReceive(Context context, Intent intent) 
	{ 
		// TODO Auto-generated method stub 	
		
		//ϵͳ���յ����ţ�����
		if (intent.getAction().equals(StaticVar.SYSTEM_SMS_ACTION)) 
		{ 
			ReceiveMsgCase(context, intent);
		}		
		//���յ�refresh����״̬�㲥
		else if (intent.getAction().equals(StaticVar.COM_SMS_SEND_REFRESH))
		{
			BaseMapMain.mainLogMessage = ReceiveDialogUpdate(BaseMapMain.mainProDialog,BaseMapMain.mainLogMessage, 
					(String)context.getResources().getText(R.string.DialogSendOK), StaticVar.COM_SMS_SEND_REFRESH);
		}
		//���յ�refresh���ͻ�ִ�㲥
		else if (intent.getAction().equals(StaticVar.COM_SMS_DELIVERY_REFRESH))
		{
			BaseMapMain.mainLogMessage = ReceiveDialogUpdate(BaseMapMain.mainProDialog, BaseMapMain.mainLogMessage, 
					(String)context.getResources().getText(R.string.DialogDeliveryOK), StaticVar.COM_SMS_DELIVERY_REFRESH);
		}
		//���յ�sos���÷���״̬�㲥
		else if(intent.getAction().equals(StaticVar.COM_SMS_SEND_SOS))
		{
			SettingActivity.setLogMessage = ReceiveDialogUpdate(SettingActivity.setProDialog, SettingActivity.setLogMessage, 
					(String)context.getResources().getText(R.string.DialogSosSendGpsOK), StaticVar.COM_SMS_SEND_SOS);
		}
		//���յ�sos���÷��ͻ�ִ�㲥
		else if(intent.getAction().equals(StaticVar.COM_SMS_DELIVERY_SOS))
		{
			SettingActivity.setLogMessage = ReceiveDialogUpdate(SettingActivity.setProDialog, SettingActivity.setLogMessage,
					(String)context.getResources().getText(R.string.DialogSosDeliveryGpsOK),StaticVar.COM_SMS_DELIVERY_SOS);
		}
		//���յ�sos�ֻ��ŵ�֪ͨ����״̬�㲥
		else if(intent.getAction().equals(StaticVar.COM_SMS_SEND_SOS_TAR))
		{
			SettingActivity.setLogMessage = ReceiveDialogUpdate(SettingActivity.setProDialog, SettingActivity.setLogMessage, 
					(String)context.getResources().getText(R.string.DialogSosSendTarOK), StaticVar.COM_SMS_SEND_SOS_TAR);
		}
		//���յ�sos�ֻ��ŵ�֪ͨ��ִ�㲥
		else if(intent.getAction().equals(StaticVar.COM_SMS_DELIVERY_SOS_TAR))
		{
			SettingActivity.setLogMessage = ReceiveDialogUpdate(SettingActivity.setProDialog, SettingActivity.setLogMessage, 
					(String)context.getResources().getText(R.string.DialogSosDeliveryTarOK), StaticVar.COM_SMS_DELIVERY_SOS_TAR);
		}
		//���յ�refresh���ӹ㲥
		else if(intent.getAction().equals(StaticVar.COM_ALARM_REFRESH))
		{
			if(StaticVar.DEBUG_ENABLE)
				StaticVar.logPrint('D', "alarm got!");
			BaseMapMain.mainLogMessage = ReceiveDialogUpdate(BaseMapMain.mainProDialog, BaseMapMain.mainLogMessage, 
					(String)context.getResources().getString(R.string.DialogAlarmGot), StaticVar.COM_ALARM_REFRESH);
		}
		//���յ�sos set���ӹ㲥
		else if(intent.getAction().equals(StaticVar.COM_ALARM_SOS_SET))
		{
			if(StaticVar.DEBUG_ENABLE)
				StaticVar.logPrint('D', "alarm got!");
			SettingActivity.setLogMessage = ReceiveDialogUpdate(SettingActivity.setProDialog, SettingActivity.setLogMessage, 
					(String)context.getResources().getString(R.string.DialogAlarmGot), StaticVar.COM_ALARM_SOS_SET);
		}
	}
	
	//��ϵͳ���ն��Ž��й��˺ͽ���
	private void ReceiveMsgCase(Context context, Intent intent)
	{
		String mesNumber;
		String mesContext;
		
		//������Intent����������
		Bundle bundle = intent.getExtras(); 
		
		//�ж�Intent��������
		if (bundle != null) 
		{ 
			if(StaticVar.DEBUG_ENABLE)
				StaticVar.logPrint('D', "bundle is not null!");
			
			//pdusΪ android�ڽ����Ų��� identifier
			//͸��bundle.get("")����һ������pdus�Ķ���
			Object[] myOBJpdus = (Object[]) bundle.get("pdus");
			
			//�������Ŷ���array,�������յ��Ķ��󳤶�������array�Ĵ�С
			SmsMessage[] messages = new SmsMessage[myOBJpdus.length];	          
          	messages[0] = SmsMessage.createFromPdu ((byte[]) myOBJpdus[0]); 
			
			mesNumber = new String(messages[0].getDisplayOriginatingAddress()); 			  

			String targetPhone = IseekApplication.getInstance().prefs.getString(
					IseekApplication.getInstance().prefTargetPhoneKey,"unset");	
			if(StaticVar.DEBUG_ENABLE)
				StaticVar.logPrint('D', "mesNumber:" + mesNumber + " targetPhone:" + targetPhone);
			
			//�е��ֻ���ȡ�������"+86"�������еĲ����У��ж��Ƿ��������������
			if(mesNumber.equals(targetPhone) || mesNumber.equals("+86" + targetPhone))
			{
			
				mesContext = new String(messages[0].getDisplayMessageBody());
				if(StaticVar.DEBUG_ENABLE)
				{
					StaticVar.logPrint('D', "mesNumber:" + mesNumber);
					StaticVar.logPrint('D', "mesContext:" + mesContext);
					StaticVar.logPrint('D', "SMS header:" + mesContext.substring(0, 7));
				}
				//����ͷ--��λ�ɹ�����
				if(mesContext.substring(0, 7).equals(StaticVar.SMS_Header_LOC_SUCCESS))
				{					
					ReceiveMsgCaseLocOK(mesContext);
				}
				//����ͷ--����sos�����gps�ظ���Ϣ
				else if(mesContext.substring(0, 7).equals(StaticVar.SMS_Header_SET_SOS_OK))
				{
					ReceiveMsgCaseSetSosOK(context, mesContext);
				}
				else
				{
					//����ͷ��ƥ��--Ϊ�˵��Է��㣬���ڽ�Ҫɾ��
					Toast.makeText(context, "SMS-header Error", Toast.LENGTH_LONG).show();
				}
				
				//���ٹ㲥��Ϣ��ȡ������
				//abortBroadcast();
			}
			else
			{
				//�ֻ����벻ƥ��
				mesContext = new String(messages[0].getDisplayMessageBody());
				if(StaticVar.DEBUG_ENABLE)
				{
					StaticVar.logPrint('D', "SMSreceiver:different targetPhone");					
					StaticVar.logPrint('D', "diff-mesNumber:" + mesNumber);
					StaticVar.logPrint('D', "diff-mesContext:" + mesContext);
				}
				return ;
			}
		}
		else
		{
			//bundle��Ϊ��
			if(StaticVar.DEBUG_ENABLE)
				StaticVar.logPrint('D', "bundle is null");
		}   
	}
	
	//��λ�ɹ�
	private void ReceiveMsgCaseLocOK(String msgContext)
	{
		//������γ��
		IseekApplication app = null;
		
		//For example:
		//W00,051,34.234442N,108.913805E,1.574Km/h,13-03-21,16:04:43
		int indexTmp = msgContext.indexOf("N");
		String Latitude = msgContext.substring(8, 16);//ԭ����8-17
		
		//��N���濪ʼ��ȡ����Ϊ����
		String Longitude = msgContext.substring(indexTmp+2, indexTmp+10);//ԭ����19-29
		if(StaticVar.DEBUG_ENABLE)
			StaticVar.logPrint('D', "OnReceive--Latitude:" + Latitude + " Longitude:" + Longitude);
		
		//����MainActivity�еľ�̬���������õ�ͼ
		if(isValidGeo(Longitude) && isValidGeo(Latitude))
		{
			int LatitudeInt;
			int LongitudeInt;
			
			LatitudeInt = (int)(Double.parseDouble(Latitude) * 1E6);
			LongitudeInt = (int)(Double.parseDouble(Longitude) * 1E6);
			
			app = (IseekApplication)IseekApplication.getInstance();
			app.prefsEditor.putString(app.prefOriginLatitude, Integer.toString(LatitudeInt)).commit();
			app.prefsEditor.putString(app.prefOriginLongitude, Integer.toString(LongitudeInt)).commit();
			
			//����Ҫ����ȡ�����ӹر�logDialog
			IseekApplication.alarmManager.cancel(IseekApplication.alarmPI);
			BaseMapMain.mainProDialog.dismiss();
			
			//WGS84����ת��Ϊ�ٶ�����
//			CoordinateConver.fromGcjToBaidu   --  GCJ-20(���Ĺȸ��ͼ)���ٶ�����ϵ 
//			CoordinateConver.fromWgs84ToBaidu --  WGS81���ٶ�����ϵת��
			GeoPoint tmpPoint = new GeoPoint(LatitudeInt, LongitudeInt);
			
			BaseMapMain.setNewPosition(tmpPoint);
		}
	}
	
	//����sos����ɹ�
	private void ReceiveMsgCaseSetSosOK(Context context, String msgContext)
	{
		if(StaticVar.DEBUG_ENABLE)
			StaticVar.logPrint('D', msgContext.substring(8));
		if(msgContext.substring(8).equals(StaticVar.SMS_BODY_SET_SOS_OK))
		{
			ReceiveDialogUpdate(SettingActivity.setProDialog, SettingActivity.setLogMessage, 
					(String)context.getResources().getText(R.string.DialogSosFeedBackGpsOK), msgContext.substring(8));
		}
	}
	
	//ʵ�ֶԶ��ŷ���״̬�Լ����Ż�ִ��dialog���洦��
	private String ReceiveDialogUpdate(ProgressDialog progressDialog, String logMessage, 
			String strLogAppend, String strCase)
	{
		
		if(getResultCode()== Activity.RESULT_OK || strCase == StaticVar.COM_ALARM_REFRESH
				|| strCase == StaticVar.COM_ALARM_SOS_SET)
		{
			if(StaticVar.DEBUG_ENABLE)
				StaticVar.logPrint('D', "receive success flag for " + strCase);
			logMessage = logMessage + "\n" + strLogAppend;
		    progressDialog.setMessage(logMessage);
		    progressDialog.show();
		}
		else
		{
			if(StaticVar.DEBUG_ENABLE)
				StaticVar.logPrint('E', "Error in case " + strCase);
		}
		return logMessage;
	}

	
	//���ھ�γ�ȵĺϷ��Լ��
	public boolean isValidGeo(String geoStr)
	{
		boolean isValid = false;
		
		double tmp;
		tmp = Double.parseDouble(geoStr);
		
		if(StaticVar.DEBUG_ENABLE)
			StaticVar.logPrint('D', "isValidGeo--str:" + geoStr + " tmp:" + tmp);
		
		if((tmp>0) && (tmp<140) )
			isValid = true;		
		return isValid;
	}
} 



