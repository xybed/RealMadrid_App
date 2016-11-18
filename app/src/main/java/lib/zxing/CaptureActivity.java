package lib.zxing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mumu.realmadrid.R;

import java.io.IOException;
import java.util.Vector;

import lib.zxing.ScanView;
import lib.zxing.camera.CameraManager;
import lib.zxing.decoding.CaptureActivityHandler;
import lib.zxing.decoding.InactivityTimer;

public class CaptureActivity extends Activity implements Callback
{
	public static final String Params_RetState = "success";
	public static final String Params_RetCode = "code";
	
	private CaptureActivityHandler handler;
	private ScanView scanView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
//	private MediaPlayer mediaPlayer;
//	private boolean playBeep;
//	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	
	private View _btnBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcapture);
		// CameraManager
		CameraManager.init(getApplication());
		scanView = (ScanView)findViewById(R.id.ids_qrcapture_scanview);
		_btnBack = (View)findViewById(R.id.btn_left);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		//
		_btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				handler.ReStart();
				backToParent(false, "");
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.ids_qrcapture_surfaceview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		}
		else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

//		playBeep = true;
//		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//			playBeep = false;
//		}
//		initBeepSound();
		vibrate = true;
		scanView.StartScan();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		scanView.StopScan();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public Handler getHandler() {
		return handler;
	}

	public void handleDecode(final Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		backToParent(true, obj.getText());
		//
//		saveBitmap(barcode);
//		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//		if (barcode == null)
//		{
//			dialog.setIcon(null);
//		}
//		else
//		{
//
//			Drawable drawable = new BitmapDrawable(barcode);
//			dialog.setIcon(drawable);
//		}
//		dialog.setTitle("ɨ����");
//		dialog.setMessage(obj.getText());
//		dialog.setNegativeButton("ȷ��", new DialogInterface.OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				//��Ĭ���������ɨ��õ��ĵ�ַ
//				Intent intent = new Intent();
//				intent.setAction("android.intent.action.VIEW");
//				Uri content_url = Uri.parse(obj.getText());
//				intent.setData(content_url);
//				startActivity(intent);
//				finish();
//			}
//		});
//		dialog.setPositiveButton("ȡ��", new DialogInterface.OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				finish();
//			}
//		});
//		dialog.create().show();
	}

	private void backToParent(boolean isSuc, String qrcode) {
		Intent intent = getIntent();
		intent.putExtra(Params_RetState, isSuc);
		intent.putExtra(Params_RetCode, qrcode);
		setResult(RESULT_OK, intent);
		finish();
	}
	
//	private void initBeepSound() {
//		if (playBeep && mediaPlayer == null) {
//			// The volume on STREAM_SYSTEM is not adjustable, and users found it
//			// too loud,
//			// so we now play on the music stream.
//			setVolumeControlStream(AudioManager.STREAM_MUSIC);
//			mediaPlayer = new MediaPlayer();
//			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//			mediaPlayer.setOnCompletionListener(beepListener);
//
//			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.tick);
//			try {
//				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
//				file.close();
//				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//				mediaPlayer.prepare();
//			} catch (IOException e) {
//				mediaPlayer = null;
//			}
//		}
//	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
//		if (playBeep && mediaPlayer != null) {
//			mediaPlayer.start();
//		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

//	//
//	int index = 0;
//	private void saveBitmap(Bitmap bmp) {
//		String dirName = "/sdcard/capture/";
//		File dir = new File(dirName);
//		if (!dir.exists()) {
//			dir.mkdir();
//		}
//		File f = new File(dirName, "img_" + index + ".png");
//		if (f.exists()) {
//			f.delete();
//		}
//		try {
//			FileOutputStream out = new FileOutputStream(f);
//			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
//			out.flush();
//			out.close();			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
//		index++;
//	}
}