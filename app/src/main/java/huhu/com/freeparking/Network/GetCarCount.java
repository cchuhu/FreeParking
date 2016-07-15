package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/8/16.
 * 获取停车数量的工具类
 */
public class GetCarCount {
    public GetCarCount(String url, String manager_id, final getSuccess getSuccess, final getFailed getFailed) {
        new NetConnection(url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String rt) {
                getSuccess.onSuccess(rt);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                getFailed.onFailed();
            }
        }, "manager_id", manager_id);
    }

    public interface getSuccess {
        void onSuccess(String result);
    }

    public interface getFailed {
        void onFailed();
    }
}
