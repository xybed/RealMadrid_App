package lib.zxing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.mumu.realmadrid.R;

import lib.zxing.camera.CameraManager;

public class ScanView extends View {

	private static final long ANIMATION_DELAY = 80L;
	private static final int	QRCorner_Size = 20;
	
	private final Paint paint;
	
	private final int	maskColor	= 0x60000000;
	
	private boolean	isScan = false;
	private int		posScan = 0;
	
	private Bitmap bmpTL;
	private Bitmap bmpTR;
	private Bitmap bmpBL;
	private Bitmap bmpBR;
	private Bitmap bmpLine;
	private Rect rectBmp;
	private Rect rectBmpLine;
	private Rect rectDraw;
	
	
	public ScanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		paint = new Paint();
		//
		bmpTL = BitmapFactory.decodeResource(getResources(), R.drawable.qrscan_tl);
		bmpTR = BitmapFactory.decodeResource(getResources(), R.drawable.qrscan_tr);
		bmpBL = BitmapFactory.decodeResource(getResources(), R.drawable.qrscan_bl);
		bmpBR = BitmapFactory.decodeResource(getResources(), R.drawable.qrscan_br);
		bmpLine = BitmapFactory.decodeResource(getResources(), R.drawable.qrscan_line);
		rectBmp = new Rect();
		rectBmp.set(0, 0, bmpTL.getWidth(), bmpTL.getHeight());
		rectBmpLine = new Rect();
		rectBmpLine.set(0, 0, bmpLine.getWidth(), bmpLine.getHeight());
		rectDraw = new Rect();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) return;
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		// draw outside
		paint.setColor(maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
	    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
	    canvas.drawRect(0, frame.bottom + 1, width, height, paint);
	    
	    //
	    rectDraw.set(frame.left, frame.top, frame.left + QRCorner_Size, frame.top + QRCorner_Size);
	    canvas.drawBitmap(bmpTL, rectBmp, rectDraw, null);
	    rectDraw.set(frame.right - QRCorner_Size, frame.top, frame.right, frame.top + QRCorner_Size);
	    canvas.drawBitmap(bmpTR, rectBmp, rectDraw, null);
	    rectDraw.set(frame.left, frame.bottom - QRCorner_Size, frame.left + QRCorner_Size, frame.bottom);
	    canvas.drawBitmap(bmpBL, rectBmp, rectDraw, null);
	    rectDraw.set(frame.right - QRCorner_Size, frame.bottom - QRCorner_Size, frame.right, frame.bottom);
	    canvas.drawBitmap(bmpBR, rectBmp, rectDraw, null);
	    	    
	    //
	    if (isScan) {
	    	int pos = getScanPos(frame.height());
	    	rectDraw.set(frame.left + 5, frame.top + pos - 2, frame.right - 5, frame.top + pos + 2);
	    	canvas.drawBitmap(bmpLine, rectBmpLine, rectDraw, null);
	    }
	    
	    //
	    postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
	}
	private int getScanPos(int height) {
		posScan += 10;
		if (posScan > height - 15) {
			posScan = 10;
		}
		return posScan;
	}
	
	public void StartScan() {
		isScan = true;
		posScan = 0;
		invalidate();
	}
	
	public void StopScan() {
		isScan = false;
		posScan = 0;
		invalidate();
	}
	
}
