package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.Constants;
import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/8/16.
 */
public class ChangePwd {
    public ChangePwd(String url, String oldpwd, String newpwd, final changeSuccess changeSuccess, final changeFailed changeFailed) {
        new NetConnection(url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String rt) {
                changeSuccess.onSuccess(rt);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                changeFailed.onFailed();
            }
        }, "manager_id", Constants.Manager_Account, "manager_oldpassword", oldpwd, "manager_newpassword", newpwd);
    }

    public interface changeSuccess {
        void onSuccess(String result);
    }

    public interface changeFailed {
        void onFailed();
    }
}
