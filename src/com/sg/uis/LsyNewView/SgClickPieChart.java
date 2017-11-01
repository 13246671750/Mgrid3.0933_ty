package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.xclcharts.view.ClickPieChart01View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

/** ��ͼ */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class SgClickPieChart extends TextView implements IObject {

	private LinkedList<PieData> chartData = new LinkedList<PieData>();// ����Դ
	private PieChart Pchart;

	public SgClickPieChart(Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		chart = new ClickPieChart01View(context);
		Pchart = chart.getChart();
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l
				+ (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t
				+ (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			chart.layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(chart);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue,
			String strResFolder) {
		if ("ZIndex".equals(strName)) {
			m_nZIndex = Integer.parseInt(strValue);
			if (MainWindow.MAXZINDEX < m_nZIndex)
				MainWindow.MAXZINDEX = m_nZIndex;
		} else if ("Location".equals(strName)) {
			String[] arrStr = strValue.split(",");
			m_nPosX = Integer.parseInt(arrStr[0]);
			m_nPosY = Integer.parseInt(arrStr[1]);
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("BackgroundColor".equals(strName)) {
			if (strValue.isEmpty())
				return;
			m_cBackgroundColor = Color.parseColor(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			parse_label();
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			if(!strValue.isEmpty())
			{
				Pchart.getLabelPaint().setColor(Color.parseColor(strValue));
			}
		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("CmdExpression".equals(strName))
			m_strCmdExpression = strValue;
		else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			mExpression = strValue;
			parse_cmd();
		} else if ("ColorData".equals(strName)) {
			colorCmd = strValue;
			parse_Color();
		}
	}
	
	private void parse_label() {
		if (m_strContent == null || m_strContent.equals("")
				|| m_strContent.equals("��������"))
		{			
			return;
		}
		String[] s = m_strContent.split("\\|");
		for (int i = 0; i < s.length; i++) {
			label_list.add(s[i]);
		}
	}
	

	private void parse_Color() {
		if (colorCmd == null || colorCmd.equals("")||equAddSignl.size()==0)
			return;
		String[] colors = colorCmd.split("\\|");
		for (int i = 0; i < equAddSignl.size(); i++) {
			if(i<colors.length)
			colorData.add(colors[i]);
			else
			colorData.add(colors[colors.length-1]);	
		}
	}

	@Override
	public void initFinished() {
		int nFlag = Gravity.NO_GRAVITY;
		if ("Left".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.LEFT;
		else if ("Right".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.RIGHT;
		else if ("Center".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.CENTER_HORIZONTAL;

		if ("Top".equals(m_strVerticalContentAlignment))
			nFlag |= Gravity.TOP;
		else if ("Bottom".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.BOTTOM;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize()) / 2f;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}
		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return mExpression;
	}

	public void setUniqueID(String strID) {
		m_strID = strID;
	}

	public void setType(String strType) {
		m_strType = strType;
	}

	public String getUniqueID() {
		return m_strID;
	}

	public String getType() {
		return m_strType;
	}

	// fjw add ��ť��������ܵĿ�������İ󶨱���ʽ����
	// �������ؼ�����ʽ�����ؿؼ�����ʽ��
	public boolean parse_cmd() {

		if (mExpression.equals("") || mExpression == null)
			return false;
		// System.out.println(mExpression+":mExpression");
		stExpression oMathExpress = UtExpressionParser.getInstance()
				.parseExpression(mExpression);
		if (oMathExpress != null) {
			Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress
					.entrySet().iterator();
			while (it.hasNext()) {
				stBindingExpression oBindingExpression = it.next().getValue();
				int equipt_id = oBindingExpression.nEquipId;
				int signal_id = oBindingExpression.nSignalId;
				equAddSignl.add(equipt_id + "_" + signal_id);
			}
		}
		return true;
	}

	@Override
	public void updateWidget() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				m_bneedupdate = true;
			}
		}).start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				Pchart.setDataSource(chartData);
				chart.invalidate();
				break;
			case 1:
				Toast.makeText(getContext(), "SgClickPieChart���ñ���", 1000).show();
				m_bneedupdate = false;
				break;
			}

		};
	};

	@Override
	public boolean updateValue() {
		if (equAddSignl.size() <= 0)
			return false;
	
		ArrayList<Float> listCount = new ArrayList<Float>();
		ArrayList<String> listName = new ArrayList<String>();
		float value = 0, count = 0;
		for (String s : equAddSignl) {
			String[] spl = s.split("_");
			equail = spl[0];
			signal = spl[1];
         	newValue=DataGetter.getSignalValue(equail, signal);
		//	newValue = Math.random() * 100 + "";
			Name = DataGetter.getSignalName(equail, signal);
			if (newValue == null || newValue.equals(""))
				return false;
			value = Float.parseFloat(newValue);
			listCount.add(value);
			listName.add(Name);
			count += value;
		}
		chartData = null;
		chartData = new LinkedList<PieData>();
		int allSore = 0;
		for (int i = 0; i < listCount.size(); i++) {
			float f = listCount.get(i);
			int score = (int) (f / count * 100);

			if (i == listCount.size() - 1)
				score = 100 - allSore;
			allSore += score;
		
		    try {
		    	if (label_list.size()<=0) 
				{
					if(listName.size()==i+1)
					chartData.add(new PieData(listName.get(i), listName.get(i) + "-"
							+ score + "%", score, Color.parseColor(colorData.get(i)),true));
					else
					chartData.add(new PieData(listName.get(i), listName.get(i) + "-"
								+ score + "%", score, Color.parseColor(colorData.get(i))));	
				}			
				else
				{
					if(label_list.size()==i+1)
				    chartData.add(new PieData(label_list.get(i), label_list.get(i) + "-"
						    + score + "%", score, Color.parseColor(colorData.get(i)),true));
					else
					chartData.add(new PieData(label_list.get(i), label_list.get(i) + "-"
								+ score + "%", score, Color.parseColor(colorData.get(i))));	
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(1);
				
				return false;
			}
		
		  }
		
		m_bneedupdate = false;
		return true;
	}

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {

	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 7;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	int m_cBackgroundColor = 0xF00CF00C;
	String m_strContent = "��ť";
	String m_strFontFamily = "΢���ź�";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	String m_strClickEvent = "��ʿ��-IDUϵͳ�趨UPS.xml";
	String m_strUrl = "www.baidu.com";
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	String cmd_value = "";

	Paint m_oPaint = null;
	Rect m_rBBox = null;
	public static ProgressDialog dialog;

	// ��¼�������꣬���˻���������������������������⡣
	public float m_xscal = 0;
	public float m_yscal = 0;

	Intent m_oHomeIntent = null;

	private String signal = "";
	private String equail = "";
	ClickPieChart01View chart = null;
	// PieChart01View pieChart01View=null;
	private String newValue = "";
	private String Name = "";
	public boolean m_bneedupdate = true;
	private String mExpression = "";
	private ArrayList<String> equAddSignl = new ArrayList<String>();
	private String colorCmd;
	private ArrayList<String> colorData = new ArrayList<String>();
	private List<String> label_list=new ArrayList<String>();

}