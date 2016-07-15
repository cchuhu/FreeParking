package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/8/16.
 * 验证停车券信息
 */
public class CheckTicket {
    public CheckTicket(String url, String ticket_id, String manager_id,final checkSuccess checkSuccess, final checkFailed checkFailed) {
        new NetConnection(url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String rt) {
                checkSuccess.onSuccess(rt);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                checkFailed.onFailed();
            }
        }, "ticket_id", ticket_id,"manager_id",manager_id);
    }

    public interface checkSuccess {
        void onSuccess(String result);
    }

    public interface checkFailed {
        void onFailed();
    }
}
