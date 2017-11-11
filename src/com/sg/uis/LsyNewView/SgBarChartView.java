package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;

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

import com.demo.xclcharts.view.BarChart01View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.mgrid.util.ExpressionUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/** 柱状图 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class SgBarChartView extends TextView implements IObject {

	private BarChart Bchart = null;
	private List<String> chartLabels = new LinkedList<String>();
	private List<BarData> chartData = new LinkedList<BarData>();

	public SgBarChartView(Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		chart = new BarChart01View(context);
		chart.setTouch(false);
		Bchart = chart.getBarChart();

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
		;
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
		} else if ("ScaleColor".equals(strName)) {
			if (!strValue.isEmpty())
			{
				Bchart.getDataAxis().getAxisPaint().setColor(Color.parseColor(strValue));
				Bchart.getCategoryAxis().getAxisPaint().setColor(Color.parseColor(strValue));			
				Bchart.getDataAxis().getTickMarksPaint().setColor(Color.parseColor(strValue));
				Bchart.getCategoryAxis().getTickMarksPaint().setColor(Color.parseColor(strValue));
			}
		
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			parse_content();
  
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
		} else if ("IsBold".equals(strName)) 
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			if (!strValue.isEmpty())
			{
				//x轴刻度文字画笔
				Bchart.getCategoryAxis().getTickLabelPaint().setColor(Color.parseColor(strValue));
			
				//y轴刻度文字画笔
				Bchart.getDataAxis().getTickLabelPaint().setColor(Color.parseColor(strValue));
				
			}
		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("ColorData".equals(strName)) {
			if (!strValue.isEmpty()) {
				color_data = strValue;
				parse_color();
			}
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			cmd = strValue;
			mExpression = strValue;
			parse_cmd();
			
		}else if ("Xlabel".equals(strName)) {
			labelData = strValue;
			parse_label();
		}
	}

	private void parse_label(){
		if (labelData == null || labelData.equals("")
				|| labelData.equals("设置内容")) {

			setlable();
			return;
		}
			
		String[] s = labelData.split("\\|");
		for (int i = 0; i < s.length; i++) {
			chartLabels.add(s[i]);
		}
		Bchart.setCategories(chartLabels);
		
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

	private void parse_content() {
		if (m_strContent == null || m_strContent.equals("")
				|| m_strContent.equals("设置内容")) {
			return;
		}
		String[] s = m_strContent.split("\\|");
		for (int i = 0; i < data_cmd.size(); i++) {
			if (i < s.length)
				data_label.add(s[i]);
			else
				data_label.add(s[s.length - 1]);
		}

	}

	private void parse_color() {
		if (data_cmd.size() == 0)
			return;
		String[] str = color_data.split("\\|");
		for (int i = 0; i < data_cmd.size(); i++) {
			if (i < str.length)
				data_color.add(str[i]);
			else
				data_color.add(str[str.length - 1]);
		}
	}

	// fjw add 按钮控制命令功能的控制命令的绑定表达式解析
	// 解析出控件表达式，返回控件表达式类
	public boolean parse_cmd() {
		if (cmd.equals("") || cmd == null)
			return false;
		String[] Expression = cmd.split("/");
		for (int i = 0; i < Expression.length; i++) {
			List<String> list_cmd = ExpressionUtils.getExpressionUtils().parse(
					Expression[i]);
			index = list_cmd.size();
			data_cmd.add(list_cmd);
		}
		return true;
	}

	private void setlable() {

		if (chartLabels.size() != 0)
			return;
		for (int i = 1; i <= index; i++) {
			chartLabels.add(i + "");
		}
		Bchart.setCategories(chartLabels);
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

				Bchart.getDataAxis().setAxisMax(max_Value + 50);
				Bchart.setDataSource(chartData);
				chart.invalidate();

				break;
			}

		};
	};

	@Override
	public boolean updateValue() {

		if (data_cmd.size() <= 0)
			return false;

		float value;

		chartData = new ArrayList<BarData>();

		List<Double> dataSeriesA = null;
		valueList.clear();
		int i = 0;
		for (List<String> list_cmd : data_cmd) {
			dataSeriesA = new LinkedList<Double>();
			// String label_name = "";
			for (String s : list_cmd) {
				String[] spl = s.split("-");
				equail = spl[0];
				signal = spl[2];

				// label_name = DataGetter.getSignalName(equail, signal);

				newValue = DataGetter.getSignalValue(equail, signal);
				if (newValue == null || newValue.equals(""))
					return false;
				value = Float.parseFloat(newValue);
				valueList.add((int) value);
				dataSeriesA.add((double) value);
			}

			compareMax(valueList);
			BarData BarDataA = new BarData(data_label.get(i), dataSeriesA,
					Color.parseColor(data_color.get(i)));
			chartData.add(BarDataA);
			i++;
		}

		m_bneedupdate = false;
		return true;
	}

	private void compareMax(List<Integer> value) {
		max_Value = 0;
		for (int i = 0; i < value.size(); i++) {
			int v = value.get(i);
			if (v > max_Value)
				max_Value = v;
		}
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
	String m_strContent = "按钮";
	String m_strFontFamily = "微软雅黑";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	String m_strClickEvent = "科士达-IDU系统设定UPS.xml";
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

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;

	Intent m_oHomeIntent = null;

	private String signal = "";
	private String equail = "";
	BarChart01View chart = null;
	// PieChart01View pieChart01View=null;
	private String newValue = "";
	private String cmd = "";
	public boolean m_bneedupdate = true;
	private String mExpression = "";
	private String color_data = null;

	private int index = 0;
	private List<List<String>> data_cmd = new ArrayList<List<String>>();// 表达式分类
	private List<String> data_color = new ArrayList<String>();// 颜色分类
	private List<String> data_label = new ArrayList<String>();// 各个柱状图的含义
	private int max_Value = 0; // 轴刻度最大值
	private List<Integer> valueList = new ArrayList<Integer>();
	
	private String labelData="";
}
