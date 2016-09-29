package cn.qatime.player.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.utils.Constant;

/**
 * @author luntify
 * @date 2016/8/15 11:11
 * @Description
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        finish();
        if (baseResp.errCode == 0) {
            EventBus.getDefault().post(PayResultState.SUCCESS);
        } else if (baseResp.errCode == -2) {//用户取消
            EventBus.getDefault().post(PayResultState.CANCEL);
            finish();
        } else {
            EventBus.getDefault().post(PayResultState.ERROR);
            finish();
        }
    }


}
