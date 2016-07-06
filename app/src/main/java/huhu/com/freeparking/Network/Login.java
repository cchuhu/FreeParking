package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/6/16.
 */
public class Login {
    public Login(String url, String mid, String mpwd, final loginSuccess loginSuccess, final loginFailed loginFailed) {
        new NetConnection(url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String rt) {
                loginSuccess.onSuccess(rt);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                loginFailed.onFailed();
            }
        }, "manager_id", mid, "manager_password", mpwd);
    }

    public interface loginSuccess {
        void onSuccess(String result);
    }

    public interface loginFailed {
        void onFailed();
    }
}
