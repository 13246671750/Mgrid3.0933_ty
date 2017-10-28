/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android鍥捐〃鍩虹被搴�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.demo.xclcharts.view;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.GaugeChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;

/**
 * @ClassName GaugeChart01View
 * @Description  浠〃鐩樹緥瀛�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class GaugeChart01View  extends GraphicalView {

	private String TAG = "GaugeChart01View";
	private GaugeChart chart = new GaugeChart();
	
	private List<String> mLabels = new ArrayList<String>();
	private List<Pair> mPartitionSet = new ArrayList<Pair>();		
	private float mAngle = 0.0f;

	
	public GaugeChart01View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();	
	}
		
	public GaugeChart01View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public GaugeChart01View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	 }
	 
	 private void initView()
	 {
		chartLabels();
		chartDataSet();	
		chartRender();
	 }
	 
	 
	 public GaugeChart getChart()
	 {
		 return chart;
	 }
	 
	 @Override  
     protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //鍥炬墍鍗犺寖鍥村ぇ灏�
        //xml涓殑璁剧疆:  android:layout_width="300dip"   
        //			   android:layout_height="300dip"           
        chart.setChartRange(w ,h );        
        //缁樺浘鍖鸿寖鍥�
        //宸﹀彸鍚勭缉杩�10%
        //int offsetX = DensityUtil.dip2px(getContext(), (float) (300 * 0.1)); 
        //鍋忕Щ楂樺害鐨�25%涓嬫潵
        //int offsetY = DensityUtil.dip2px(getContext(), (float) (300 * 0.25));        
       // chart.setPadding(offsetY, 0, 0,  0);
     
     }  
	 
	
	//浠巗eekbar浼犲叆鐨勫��
	public void setAngle(float currentAngle)
	{
		mAngle = currentAngle;
	}	
		
	public void chartRender()
	{
		try {								
						
			//璁剧疆鏍囬
			//chart.setTitle("鍒诲害鐩� ");
								
			//鍒诲害姝ラ暱
			chart.setTickSteps(5d);
			
			chart.getPointerLinePaint().setColor(Color.WHITE);
			chart.getPinterCirclePaint().setColor((int)Color.rgb(76, 128, 164));
			chart.getPinterCirclePaint().setStrokeWidth(5);
			chart.getDountPaint().setColor(Color.rgb(178, 212, 233));
			//鏍囩(鏍囩鍜屾闀垮垎寮�锛屾闀垮嵆鍒诲害鍙互瀵嗙偣锛屾爣绛惧彲浠ユ澗鐐�)	
            chart.getLabelPaint().setColor(Color.WHITE);
            chart.getLabelPaint().setTextSize(15);
			chart.setCategories(mLabels);					
			//鍒嗗尯 
			chart.setPartition(mPartitionSet); 
			
			//璁剧疆褰撳墠鎸囧悜瑙掑害(0-180).
			//chart.setCurrentAngle(90f); 
			chart.setCurrentAngle(mAngle);
			//缁樺埗杈规 
	//		chart.showRoundBorder();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		
	}
	
	//鍒嗗尯[瑙掑害(0-mStartAngle)锛岄鑹瞉		
	private void chartDataSet()
	{
		int Angle = 180/3;
		mPartitionSet.add(new Pair<Float,Integer>((float)Angle, (int)Color.rgb(92, 153, 199)));
		mPartitionSet.add(new Pair<Float,Integer>((float)Angle, (int)Color.rgb(92, 153, 199)));
		mPartitionSet.add(new Pair<Float,Integer>((float)Angle, (int)Color.rgb(92, 153, 199)));
	}
	
	private void chartLabels()
	{
		//鏍囩		
		mLabels.add("0");
		mLabels.add("10");
		mLabels.add("20");
		mLabels.add("30");
		mLabels.add("40");
		mLabels.add("50");
	}

	
	@Override
    public void render(Canvas canvas) {
        try{
        	
            chart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }
}
