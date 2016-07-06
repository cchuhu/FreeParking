package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/6/16.
 * 登陆首页获取头像的接口
 */
public class GetIcon {
    public GetIcon(String url, String mid, final onTokenSuccess onTokenSuccess, final onTokenFailed onTokenFailed) {
        new NetConnection(url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String rt) {
                onTokenSuccess.onSuccess(rt);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                onTokenFailed.onFailed();
            }
        }, "manager_id", mid);
    }

    public interface onTokenSuccess {
        void onSuccess(String result);
    }

    public interface onTokenFailed {
        void onFailed();
    }
}
