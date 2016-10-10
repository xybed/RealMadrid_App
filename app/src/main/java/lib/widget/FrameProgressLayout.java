package lib.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mumu.realmadrid.R;

public class FrameProgressLayout extends LinearLayout {
    private Context mContext;
    private Button btnRefresh;
    private TextView txtStatus;
    private ImageView imgLoadStatus;
    private ProgressBar progressBar;

    public FrameProgressLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        this.mContext= context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.loading_progress_layout, this);
        progressBar = (ProgressBar) view.findViewById(R.id.load_progressbar);
        btnRefresh = (Button) view.findViewById(R.id.btn_refresh);
        txtStatus = (TextView)view.findViewById(R.id.txt_status);
        imgLoadStatus = (ImageView)view.findViewById(R.id.img_load_status);
    }

    public void show(){
        setVisibility(VISIBLE);
        progressBar.setVisibility(VISIBLE);

        btnRefresh.setVisibility(GONE);
        txtStatus.setVisibility(VISIBLE);
        txtStatus.setText("玩命加载中...");

        imgLoadStatus.setVisibility(GONE);
    }

    public void loadFail(String msg){
        progressBar.setVisibility(GONE);
        txtStatus.setVisibility(GONE);

        imgLoadStatus.setVisibility(VISIBLE);
        imgLoadStatus.setImageResource(R.drawable.icon_load_fail);

        btnRefresh.setVisibility(VISIBLE);
        btnRefresh.setText(msg);
        btnRefresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onClickRefreshListener != null) {
                    show();
                    onClickRefreshListener.onClickRefresh();
                }
            }
        });
    }

    public void loadFail(){
        progressBar.setVisibility(GONE);
        txtStatus.setVisibility(GONE);

        imgLoadStatus.setVisibility(VISIBLE);
        imgLoadStatus.setImageResource(R.drawable.icon_load_fail);

        btnRefresh.setVisibility(VISIBLE);
        btnRefresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onClickRefreshListener != null) {
                    show();
                    onClickRefreshListener.onClickRefresh();
                }
            }
        });
    }

    public void noImage(){
        imgLoadStatus.setVisibility(VISIBLE);
        imgLoadStatus.setImageResource(R.drawable.icon_load_fail);

        progressBar.setVisibility(GONE);
        txtStatus.setVisibility(GONE);
        btnRefresh.setVisibility(GONE);
    }

    public void noData(){
        imgLoadStatus.setVisibility(VISIBLE);
        imgLoadStatus.setImageResource(R.drawable.icon_load_no_data);
        txtStatus.setVisibility(VISIBLE);
        txtStatus.setText("暂无数据");

        progressBar.setVisibility(GONE);
        btnRefresh.setVisibility(GONE);
    }

    public void noData(String str){
        imgLoadStatus.setVisibility(VISIBLE);
        imgLoadStatus.setImageResource(R.drawable.icon_load_no_data);
        txtStatus.setText(str);
        txtStatus.setVisibility(VISIBLE);

        progressBar.setVisibility(GONE);
        btnRefresh.setVisibility(GONE);
    }

    public void dismiss(){
        setVisibility(GONE);
    }

    private OnClickRefreshListener onClickRefreshListener;

    public OnClickRefreshListener getOnClickRefreshListener()
    {
        return onClickRefreshListener;
    }

    public void setOnClickRefreshListener(OnClickRefreshListener onClickRefreshListener){
        this.onClickRefreshListener = onClickRefreshListener;
    }

    public interface OnClickRefreshListener{
        void onClickRefresh();
    }

}
