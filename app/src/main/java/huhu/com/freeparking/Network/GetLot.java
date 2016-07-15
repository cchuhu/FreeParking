package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/15/16.
 * 获取所有停车场信息
 */
public class GetLot {
    public GetLot(String url, final getLotSuccess getLotSuccess, final getLotFailed getLotFailed) {
        new NetConnection(url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String rt) {
                getLotSuccess.onSuccess(rt);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                getLotFailed.onFailed();
            }
        });
    }

    public interface getLotSuccess {
        void onSuccess(String result);
    }

    public interface getLotFailed {
        void onFailed();
    }
}
