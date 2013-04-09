package com.izzz.iseek.map;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.izzz.iseek.vars.StaticVar;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class BaseOnTouchListener implements OnTouchListener{

	private GeoPoint corrPoint;
	private OverlayItem corrItem;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		// �����Ļ�����λ��
		if(StaticVar.CORRECTION_ENABLE)
		{
			
            int x = (int) event.getX();
            int y = (int) event.getY();

            // ����������תΪ��ַ����
            Projection getProjection = BaseMapMain.mMapView.getProjection();
            GeoPoint pt = getProjection.fromPixels(x, y);
            
            String longitude = Integer.toString(pt.getLongitudeE6());
            String latitude = Integer.toString(pt.getLatitudeE6());	           
            
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            	BaseMapMain.logText.setText("x: " + x + " y: " + y
                        + '\n' + " latitude: " + latitude
                        +" longitude: " + longitude);
            	
            	corrPoint = new GeoPoint(pt.getLatitudeE6(), pt.getLongitudeE6());
        		corrItem = new OverlayItem(corrPoint, "title", "snippet");
        		//corrItem.setMarker(getResources().getDrawable(R.drawable.icon_marka));
        		//���ε��ã�����У׼��
        		if(!BaseMapMain.ADD_LAYER_FLAG)	
        		{
        			BaseMapMain.mMapView.getOverlays().add(BaseMapMain.correctionOverlay);
        			BaseMapMain.ADD_LAYER_FLAG = true;
        		}
        		BaseMapMain.correctionOverlay.refreshItem(corrItem);
        		BaseMapMain.mMapView.refresh();
        		
        		BaseMapMain.corrPoint = corrPoint;
        		BaseMapMain.corrItem = corrItem;
        		
                break;
            }
		}
		return false;
	}

}