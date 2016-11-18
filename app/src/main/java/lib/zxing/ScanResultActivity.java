package lib.zxing;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mumu.realmadrid.R;

public class ScanResultActivity extends Activity {

	public static final String Params_Val = "val";
	
	private static final int Type_Null	= 0;
	private static final int Type_Text	= 1;
	private static final int Type_Url	= 2;
	private static final int Type_Custom = 3;
	
	private View _btnBack;
	private TextView _txtType;
	private View _btnContent;
	private TextView _txtVal;
	private Button _btnCopy;
	
	private String mVal = "";
	private int			mType = Type_Null;
	private ClipboardManager mClipboard = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrresult);
		
		_btnBack = (View)findViewById(R.id.ids_activity_qrresult_back);
		_txtType = (TextView)findViewById(R.id.ids_activity_qrresult_type);
		_btnContent = (View)findViewById(R.id.ids_activity_qrresult_content);
		_txtVal = (TextView)findViewById(R.id.ids_activity_qrresult_text);
		_btnCopy = (Button)findViewById(R.id.ids_activity_qrresult_copy);
		
		mClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		
		_btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		_btnContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				contentClick();
			}
		});
		_btnCopy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mClipboard.setPrimaryClip(ClipData.newPlainText("text", mVal));
				Toast.makeText(ScanResultActivity.this, "已复制", Toast.LENGTH_SHORT).show();
			}
		});
		
		mType = Type_Null;
		_txtType.setText("");
		_txtVal.setText("");
		
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		if (data != null && data.containsKey(Params_Val)) {
			mVal = data.getString(Params_Val);
		}
		dealVal();
	}
	
	private void dealVal() {
		if (null == mVal || mVal.trim().length() == 0) return;
		if (mVal.startsWith("http://") || mVal.startsWith("https://")) {
			mType = Type_Url;
			_txtType.setText("网址");
		} else {
			mType = Type_Text;
			_txtType.setText("文本");
		}
		_txtVal.setText(mVal);
	}
	
	private void contentClick() {
		if (mType == Type_Text) {
			
		} else if (mType == Type_Url) {
			
		}
	}
}
