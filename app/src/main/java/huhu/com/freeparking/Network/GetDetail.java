package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/13/16.
 * 获取商家与停车券数量对应关系
 */
public class GetDetail {
    public GetDetail(String url, String manager_id,final getSuccess getSuccess, final getFailed getFailed) {
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
        },"gstmid",manager_id);
    }

    public interface getSuccess {
        void onSuccess(String result);
    }

    public interface getFailed {
        void onFailed();
    }
}
