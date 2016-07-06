package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/1/16.
 * 获取七牛上传token工具类
 */
public class GetToken {
    /**
     * 获取token
     *
     * @param url
     * @param onTokenSuccess
     * @param onTokenFailed
     */
    public GetToken(String url, final onTokenSuccess onTokenSuccess, final onTokenFailed onTokenFailed) {
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
        });
    }

    public interface onTokenSuccess {
        void onSuccess(String result);
    }

    public interface onTokenFailed {
        void onFailed();
    }
}
