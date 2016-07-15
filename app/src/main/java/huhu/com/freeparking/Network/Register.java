package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/6/16.
 * 注册新用户的接口
 */
public class Register {
    public Register(String url, String img_url, String mid, String mname, String mpwd, String parklot_id, final registerSuccess registerSuccess, final registerFailed registerFailed) {
        new NetConnection(url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String rt) {
                registerSuccess.onSuccess(rt);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                registerFailed.onFailed();
            }
        }, "manager_img", img_url, "manager_id", mid, "manager_name", mname, "manager_password", mpwd, "parklot_id",parklot_id);
    }

    public interface registerSuccess {
        void onSuccess(String result);
    }

    public interface registerFailed {
        void onFailed();
    }
}
