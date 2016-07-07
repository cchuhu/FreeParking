package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/7/16.
 * 获取个人基本信息的工具类
 */
public class GetInfo {
    public GetInfo(String url, String mid, final getInfoSuccess getInfoSuccess, final getInfoFailed getInfoFailed) {
        new NetConnection(url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String rt) {
                getInfoSuccess.onSuccess(rt);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                getInfoFailed.onFailed();
            }
        }, "manager_id", mid);
    }

    public interface getInfoSuccess {
        void onSuccess(String result);
    }

    public interface getInfoFailed {
        void onFailed();
    }
}
