package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import cn.qatime.player.R;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.VerticalSlide;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentNEVideoPlayer3 extends BaseFragment {

    private ImageView point3;
    private ImageView point1;
    private ImageView point2;
    private FragmentNEVideoPlayer32 player2;
    private FragmentNEVideoPlayer31 player1;
    private FragmentNEVideoPlayer33 player3;
    private int id; //获取联网的id

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player3, null);
        point1 = (ImageView) view.findViewById(R.id.point1);
        point2 = (ImageView) view.findViewById(R.id.point2);
        point3 = (ImageView) view.findViewById(R.id.point3);
        VerticalSlide dragLayout = (VerticalSlide) view.findViewById(R.id.dragLayout);
        dragLayout.setOnPageChangeListener(new VerticalSlide.OnPageChangeListener() {
            @Override
            public void onChange(int page) {
                if (page == 0) {
                    point1.setImageResource(R.mipmap.point_select);
                    point2.setImageResource(R.mipmap.point_default);
                    point3.setImageResource(R.mipmap.point_default);
                } else if (page == 1) {
                    point1.setImageResource(R.mipmap.point_default);
                    point2.setImageResource(R.mipmap.point_select);
                    point3.setImageResource(R.mipmap.point_default);
                } else {
                    point1.setImageResource(R.mipmap.point_default);
                    point2.setImageResource(R.mipmap.point_default);
                    point3.setImageResource(R.mipmap.point_select);
                }
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        player1 = new FragmentNEVideoPlayer31();
        player2 = new FragmentNEVideoPlayer32();
        player3 = new FragmentNEVideoPlayer33();
        transaction.replace(R.id.first, player1);
        transaction.replace(R.id.second, player2);
        transaction.replace(R.id.three, player3);
        transaction.commit();
        initData();
        return view;
    }

    private void initData() {
        if (id != 0) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id+"/play_info", null,
                    new VolleyListener(getActivity()) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            RemedialClassDetailBean data = JsonUtils.objectFromJson(response.toString(), RemedialClassDetailBean.class);
                            if (data != null) {
                                player1.setData(data);
                                player2.setData(data);
                                player3.setData(data);
                            }
                        }

                        @Override
                        protected void onError(JSONObject response) {

                        }

                        @Override
                        protected void onTokenOut() {
                            tokenOut();
                        }
                    }

                    , new VolleyErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    super.onErrorResponse(volleyError);
                }
            });
            addToRequestQueue(request);
        }
    }

    public void setId(int id) {
        this.id = id;
    }
}
