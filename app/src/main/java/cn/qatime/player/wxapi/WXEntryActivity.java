package cn.qatime.player.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.weixin.callback.WXCallbackActivity;

import org.greenrobot.eventbus.EventBus;

import cn.qatime.player.utils.Constant;

/**
 * @author lungtify
 * @Time 2016/11/30 11:36
 * @Describe 微信登录回调
 */

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
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
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) baseResp;
            if (newResp.errCode == BaseResp.ErrCode.ERR_OK) {
                //获取微信传回的code
                String code = newResp.code;
                Logger.e("***" + code);
                EventBus.getDefault().post(code);
            }
            finish();
        }
        finish();
    }
}
