package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/6/16.
 * 登陆首页获取头像的接口
 */
public class GetIcon {
    public GetIcon(String url, String mid, final getIconSuccess getIconSuccess, final getIconFailed getIconFailed) {
        new NetConnection(url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String rt) {
                getIconSuccess.onSuccess(rt);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                getIconFailed.onFailed();
            }
        }, "manager_id", mid);
    }

    public interface getIconSuccess {
        void onSuccess(String result);
    }

    public interface getIconFailed {
        void onFailed();
    }
}
