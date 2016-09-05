package cn.qatime.player.im.cache;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luntify
 * @date 2016/8/16 13:38
 * @Description 云信用户数据缓存
 */
public class UserInfoCache {
    public static UserInfoCache getInstance() {
        return InstanceHolder.instance;
    }

    private Map<String, NimUserInfo> account2UserMap = new ConcurrentHashMap<>();

    private Map<String, List<RequestCallback<NimUserInfo>>> requestUserInfoMap = new ConcurrentHashMap<>(); // 重复请求处理

    public NimUserInfo getUserInfo(String account) {
        if (TextUtils.isEmpty(account) || account2UserMap == null) {
            return null;
        }
        return account2UserMap.get(account);
    }

    public void getUserInfoFromRemote(final String account, final RequestCallback<NimUserInfo> callback) {
        if (TextUtils.isEmpty(account)) {
            return;
        }

        if (requestUserInfoMap.containsKey(account)) {
            if (callback != null) {
                requestUserInfoMap.get(account).add(callback);
            }
            return; // 已经在请求中，不要重复请求
        } else {
            List<RequestCallback<NimUserInfo>> cbs = new ArrayList<>();
            if (callback != null) {
                cbs.add(callback);
            }
            requestUserInfoMap.put(account, cbs);
        }

        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);

        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {

            @Override
            public void onResult(int code, List<NimUserInfo> users, Throwable exception) {
                if (exception != null) {
                    callback.onException(exception);
                    return;
                }

                NimUserInfo user = null;
                boolean hasCallback = requestUserInfoMap.get(account).size() > 0;
                if (code == ResponseCode.RES_SUCCESS && users != null && !users.isEmpty()) {
                    user = users.get(0);
                    // 这里不需要更新缓存，由监听用户资料变更（添加）来更新缓存
                }

                // 处理回调
                if (hasCallback) {
                    List<RequestCallback<NimUserInfo>> cbs = requestUserInfoMap.get(account);
                    for (RequestCallback<NimUserInfo> cb : cbs) {
                        if (code == ResponseCode.RES_SUCCESS) {
                            cb.onSuccess(user);
                        } else {
                            cb.onFailed(code);
                        }
                    }
                }

                requestUserInfoMap.remove(account);
            }
        });
    }


    /**
     * 构建缓存与清理
     */
    public void buildCache() {
        //TODO 有登录数据时调用构建
        List<NimUserInfo> users = NIMClient.getService(UserService.class).getAllUserInfo();
        addOrUpdateUsers(users, false);
    }

    public void clear() {
        clearUserCache();
    }

    private void clearUserCache() {
        account2UserMap.clear();
    }


    /**
     * *************************************** User缓存管理与变更通知 ********************************************
     */

    private void addOrUpdateUsers(final List<NimUserInfo> users, boolean notify) {
        if (users == null || users.isEmpty()) {
            return;
        }

        // update cache
        for (NimUserInfo u : users) {
            account2UserMap.put(u.getAccount(), u);
        }

        // log
        List<String> accounts = getAccounts(users);
//        DataCacheManager.Log(accounts, "on userInfo changed", UIKitLogTag.USER_CACHE);

        // 通知变更
        if (notify && accounts != null && !accounts.isEmpty()) {
//            NimUIKit.notifyUserInfoChanged(accounts); // 通知到UI组件
        }
    }

    private List<String> getAccounts(List<NimUserInfo> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }

        List<String> accounts = new ArrayList<>(users.size());
        for (NimUserInfo user : users) {
            accounts.add(user.getAccount());
        }

        return accounts;
    }

    public String getAlias(String account) {
        Friend friend = FriendDataCache.getInstance().getFriendByAccount(account);
        if (friend != null && !TextUtils.isEmpty(friend.getAlias())) {
            return friend.getAlias();
        }
        return null;
    }

    // 获取用户原本的昵称
    public String getUserName(String account) {
        NimUserInfo userInfo = getUserInfo(account);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            return userInfo.getName();
        } else {
            return account;
        }
    }

    /**
     * 在Application的onCreate中向SDK注册用户资料变更观察者
     */
    public void registerObservers(boolean register) {
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoUpdateObserver, register);
    }

    private Observer<List<NimUserInfo>> userInfoUpdateObserver = new Observer<List<NimUserInfo>>() {
        @Override
        public void onEvent(List<NimUserInfo> users) {
            if (users == null || users.isEmpty()) {
                return;
            }

            addOrUpdateUsers(users, true);
        }
    };

    /**
     * ************************************ 单例 **********************************************
     */

    static class InstanceHolder {
        final static UserInfoCache instance = new UserInfoCache();
    }
}
