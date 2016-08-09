package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.activity.PersonalInformationActivity;
import cn.qatime.player.activity.PersonalMyOrderActivity;
import cn.qatime.player.base.BaseFragment;

public class Fragment4 extends BaseFragment implements View.OnClickListener {


    private ImageView information;
    private TextView ic;
    private TextView waitting;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
//        myinformation = (ImageView) view.findViewById(R.id.information1);
//        myinformation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
//                intent.putExtra("pager", 0);
//                startActivity(intent);
//            }
//        });

        ic = (TextView) view.findViewById(R.id.today);

        ic.setOnClickListener(this);
        return view;
    }


//
//
//    private void initview(View view) {
//
//
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.today:
                Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.waitting:
                intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                intent.putExtra("pager", 0);
                startActivity(intent);
                break;
////            case R.id.today:
//                Intent intent1 = new Intent(getActivity(), PersonalMyTutorshipActivity.class);
//                intent1.putExtra("pager", 0);
//                startActivity(intent1);
//                break;
        }
    }
}
