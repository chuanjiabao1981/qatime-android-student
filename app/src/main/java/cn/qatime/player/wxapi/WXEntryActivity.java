package cn.qatime.player.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.toolbox.JsonObjectRequest;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2016/11/30 11:36
 * @Describe 微信登录回调
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
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
        Logger.e("****" + baseReq.getType());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) baseResp;
            if (newResp.errCode == BaseResp.ErrCode.ERR_OK) {

                //获取微信传回的code
                String code = newResp.code;
                Logger.e("***" + code);
//                JsonObjectRequest request = new JsonObjectRequest("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constant.APP_ID + "&secret=" + Constant.APP_SECRET + "&code=" + code + "&grant_type=authorization_code", null, new VolleyListener(this) {
//                    @Override
//                    protected void onTokenOut() {
//
//                    }
//
//                    @Override
//                    protected void onSuccess(JSONObject response) {
//
//                    }
//
//                    @Override
//                    protected void onError(JSONObject response) {
//
//                    }
//                }, new VolleyErrorListener());
//                addToRequestQueue(request);
            }
        }
    }
}