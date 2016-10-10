package com.mumu.realmadrid.view.im;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mumu.realmadrid.R;
import com.mumu.realmadrid.view.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.utils.PhotoUtil;
import lib.widget.TouchImageView;

/**
 * 腾讯的图片预览界面
 */
public class PhotoPreviewActivity extends BaseActivity {
    public static final String IMAGE_PATH = "image_path";
    public static final String PHOTO_VIEW_TYPE = "photo_view_type";//send_photo发送图片，view_photo预览图片

    @Bind(R.id.cb_pic_org)
    CheckBox cbPicOrg;
    @Bind(R.id.txt_img_size)
    TextView txtImgSize;
    @Bind(R.id.img_preview)
    TouchImageView imgPreview;
    @Bind(R.id.rlay_bottom_bar)
    RelativeLayout rlayBottomBar;

    private String imagePath;
    private String photoViewType;
    private Bitmap bitmap,newBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        initUI();
    }

    private void initUI() {
        ButterKnife.bind(this);
        imagePath = getIntent().getStringExtra(IMAGE_PATH);
        photoViewType = getIntent().getStringExtra(PHOTO_VIEW_TYPE);
        if (imagePath != null) {
            bitmap = PhotoUtil.file2Bitmap(imagePath);
            int degree = PhotoUtil.getBitmapDegree(imagePath);
            if(degree != 0){
                newBitmap = PhotoUtil.rotateBitmapByDegree(bitmap, degree);
            }
            imgPreview.setImageBitmap(degree == 0 ? bitmap : newBitmap);
        }
        if("view_photo".equals(photoViewType)){
            rlayBottomBar.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.cb_pic_org, R.id.btn_photo_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_pic_org:
                break;
            case R.id.btn_photo_send:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
        if(newBitmap != null){
            newBitmap.recycle();
            newBitmap = null;
        }
    }
}
