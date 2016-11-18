package lib.zxing;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mumu.realmadrid.R;
import com.mumu.realmadrid.view.BaseActivity;

import java.util.Hashtable;

public class MeQrCodeActivity extends BaseActivity {

	private View _btnBack;
	private ImageView _imgCode;
	private Button _btnCopy;
	
	private ClipboardManager mClipboard = null;
	
	private int QRSize = 400;
	private String mQRCode = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_qrcode);
		
		_btnBack = (View)findViewById(R.id.ids_activity_me_qrcode_back);
		_imgCode = (ImageView)findViewById(R.id.ids_activity_me_qrcode_img);
		_btnCopy = (Button)findViewById(R.id.ids_activity_me_qrcode_copy);
		mClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		
		_btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		_btnCopy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mClipboard.setPrimaryClip(ClipData.newPlainText("text", mQRCode));
				Toast.makeText(MeQrCodeActivity.this, "已复制", Toast.LENGTH_SHORT).show();
			}
		});
		
//		QRSize = (int)(FicDisplay.GetD2p() * 200) / 100 * 100;
		mQRCode = "老老的老清";
		createQRImage(mQRCode);
	}
	
	/**
	 * create QR
	 */
	private void createQRImage(String val) {
		try {
			if (val == null || "".equals(val) || val.length() < 1) {
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(val, BarcodeFormat.QR_CODE, QRSize, QRSize, hints);
			int[] pixels = new int[QRSize * QRSize];
			for (int y = 0; y < QRSize; y++) {
				for (int x = 0; x < QRSize; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QRSize + x] = 0xff000000;
					} else {
						pixels[y * QRSize + x] = 0xffffffff;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(QRSize, QRSize, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QRSize, 0, 0, QRSize, QRSize);
			_imgCode.setImageBitmap(bitmap);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
