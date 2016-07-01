package huhu.com.freeparking.Util;

/**
 * Created by Huhu on 5/5/16.
 * 常量类
 */
public class Config {
    //字符集
    public static final String CHARSET = "utf-8";
    //获取七牛云token
    public static final String URL_GETOKEN = "http://139.129.24.127/parking_app/Qiniu/GetToken.php";
    //实时显示头像
    public static final String URL_GETICON = "http://139.129.24.127/parking_app/ParkingLot/GetIcon.php";
    //登陆接口
    public static final String URL_LOGIN = "http://139.129.24.127/parking_app/ParkingLot/Login.php";
    //注册接口
    public static final String URL_REGISTER = "http://139.129.24.127/parking_app/ParkingLot/Register.php";
    //修改账户信息接口
    public static final String URL_CHANGE = "http://139.129.24.127/parking_app/ParkingLot/ChangeInfo.php";
    //修改密码接口
    public static final String URL_CHANGEPWD = "http://139.129.24.127/parking_app/ParkingLot/ChangePwd.php";
    //验证停车券接口
    public static final String URL_CHECK = "http://139.129.24.127/parking_app/ParkingLot/ScanQR.php";
    //获取停车数量接口
    public static final String URL_GETCOUNT = "http://139.129.24.127/parking_app/ParkingLot/GetCount.php";

}
