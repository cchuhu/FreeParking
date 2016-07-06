package huhu.com.freeparking.Network;

import huhu.com.freeparking.Util.HttpMethod;
import huhu.com.freeparking.Util.NetConnection;

/**
 * Created by Huhu on 7/6/16.
 */
public class ChangeInfo {
    public ChangeInfo(String url, String mid, String img_url, String mname, final changeSuccess changeSuccess, final changeFailed changeFailed) {
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
        }, "manager_id", mid, "manager_img", img_url, "manager_name", mname);
    }

    public interface changeSuccess {
        void onSuccess(String result);
    }

    public interface changeFailed {
        void onFailed();
    }
}
