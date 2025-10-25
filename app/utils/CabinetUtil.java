package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import play.libs.Json;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import java.net.URLEncoder;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

import static constants.BusinessConstant.PARAM_KEY_QIANMING_APP_KEY;
import static constants.BusinessConstant.PARAM_KEY_QIANMING_SECRET;


@Singleton
public class CabinetUtil {

    private static String appkey;
    private static String secretKey;

    @Inject
    CabinetUtil(ConfigUtils configUtils){
        appkey = configUtils.getPlatformConfigValue(PARAM_KEY_QIANMING_APP_KEY);
        secretKey = configUtils.getPlatformConfigValue(PARAM_KEY_QIANMING_SECRET);
    }

    public static final String API_URL = "https://open.qianmingyun.com/webapi/apiService/v1/s1/";
    /**
     * 查询设备是否在线
     */
    public static final String API_QUERY_DEVICE_STATUS = "chkDevice";

    public static final String API_SET_DEVICE_NAME = "set_deviceName";
    //set_qrParam为小屏幕(5寸以下)设备设置二维码接口
    //set_qrParamStr为大屏幕(大于等于5寸)设备设置二维码接口
    public static final String API_SET_QR_CODE = "set_qrParam";
    //设置设备文本显示信息
    public static final String API_SET_TEXT = "set_text";
    /**
     * 设置设备箱门总数
     */
    public static final String API_SET_TOTAL_BOX_NUM = "set_totalBoxNum";

    /**
     * 查询箱门状态(需锁支持反馈,无反馈锁状态均为打开)
     */
    public static final String API_GET_BOX_INFO = "get_boxInfo";

    /**
     * 用户或管理员打开指定箱门(此接口会覆盖密码)
     */
    public static final String API_OPEN_ONE_BOX = "openOneBox";

    /**
     * 用户或管理员设置指定箱门的开箱密码(可控制是否开箱) 六位数字密码
     * 需要配合 3.16 设置设备工作模式 接口一块使用,设备默认 工作模式为 5：手机号+密码
     */
    public static final String API_SET_OPEN_CODE6 = "setOpenCode6";

    /**
     * 用户或管理员设置指定箱门的开箱密码(可控制是否开箱) 八位数字密码
     * 需要配合 3.16 设置设备工作模式 接口一块使用,设备默认 工作模式为 5：手机号+密码
     */
    public static final String API_SET_OPEN_CODE8 = "setOpenCode8";

    /**
     * 用户或管理员设置指定箱门的开箱密码(可控制是否开箱) 11位手机号+四位数字密码
     * 需要配合 3.16 设置设备工作模式 接口一块使用,设备默认 工作模式为 5：手机号+密码
     */
    public static final String API_SET_OPEN_CODE = "setOpenCode";

    /**
     * 管理员打开全部箱门,不清数据
     */
    public static final String API_OPEN_ALL_BOX = "openAllBox";

    /**
     * 管理员或用户打开指定箱门(可控制是否开箱和清除密码)
     */
    public static final String API_CLEAR_ONE_BOX = "clearOneBox";

    /**
     * 管理员打开全部箱门并清除所有开箱密码 直接开门并清除数据
     */
    public static final String API_CLEAR_ALL_BOX = "clearAllBox";

    /**
     * 管理员打开全部箱门并清除所有开箱密码  可控制是否开门是否清除数据
     */
    public static final String API_CLEAR_ALL_BOX2 = "clearAllBox2";

    /**
     * 设置设备工作模式
     */
    public static final String API_SET_WORK_MODE = "setWorkMode";

    /**
     * 用户开箱操作(带存取标志开箱接口,无密码)
     */
    public static final String API_OPEN_BOX_CQ = "openBoxCQ";

    /**
     * 管理员开箱操作(可一次操作多个箱子，无密码；可选是否开箱，是否清除数据)
     */
    public static final String API_OPEN_MULIT_BOX = "openMulitBox";

    /**
     * 初始化箱门(清除所有数据后重建)
     */
    public static final String API_INIT_BOXS = "initBoxs";

    /**
     * 设置设备语音开关
     */
    public static final String API_IS_OPEN_VOICE = "isOpenVoice";

    /**
     * 配餐柜存餐(调用接口后服务器返回用户使用的箱门)
     */
    public static final String API_BUSINESS_PUT_MEAL = "businessPutMeal";


    /**
     * 配餐柜取餐
     */
    public static final String API_BUSINESS_TAKE_MEAL = "businessTakeMeal";

    /**
     * 配餐柜撤餐
     */
    public static final String API_RECALL_MEAL = "recallMeal";


    /**
     * 配餐柜管理指令
     */
    public static final String API_BUFFET_MANAGE = "buffetManage";

    /**
     * 自定义箱门类型(调用3.6初始化箱门后可用,否则即使调用也无效,重复调用初始化箱门后需重新修改箱门类型)
     */
    public static final String API_UP_BOX_TYPE = "upBoxType";

    /**
     * 查询柜状态
     */
    public static final String API_QUERY_BUFFET_BOX_INFO = "queryBuffetBoxInfo";

    /**
     * 查询设备是否在线
     * query device status
     */
    public ObjectNode queryDeviceStatus(String cabinetId) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_QUERY_DEVICE_STATUS, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"操作成功","data":true} true在线 false离线
        return resultNode;
    }

    /**
     * 设置设备名称
     */
    public ObjectNode setDeviceName(String cabinetId, String cabinetName) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("cabinetName", encodeUrl(cabinetName));
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_SET_DEVICE_NAME, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
        //{"code":200,"message":"success","data":null}
    }

    /**
     * 设置设备二维码
     */
    public ObjectNode setQrCode(String cabinetId, String qrCode) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("qrCode", encodeUrl(qrCode));
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_SET_QR_CODE, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"success","data":null}
        return resultNode;
    }

    /**
     * 设置设备文本显示信息
     */
    public ObjectNode setText(String cabinetId, String text1, String text2, String text3) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        if (!ValidationUtil.isEmpty(text1)) map.put("text1", encodeUrl(text1));
        if (!ValidationUtil.isEmpty(text2)) map.put("text2", encodeUrl(text2));
        if (!ValidationUtil.isEmpty(text3)) map.put("text3", encodeUrl(text3));
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_SET_TEXT, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"success","data":null}
        //{"code":212,"message":"设备不在线","data":null}
        return resultNode;
    }

    /**
     * 设置设备箱门总数
     */
    public ObjectNode setTotalBoxNum( String cabinetId, String boxNum) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxCount", boxNum);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_SET_TOTAL_BOX_NUM, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
        //{"code":212,"message":"设备不在线","data":null}
        //{"code":200,"message":"success","data":null}
    }

    /**
     * 查询箱门状态(需锁支持反馈,无反馈锁状态均为打开)
     */
    public ObjectNode getBoxInfo( String cabinetId, String boxNum) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", boxNum);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_GET_BOX_INFO, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"success","data":"[\"消息服务器返回数据为空\"]"}
        return resultNode;
    }

    /**
     * 用户或管理员打开指定箱门(此接口会覆盖密码)
     */
    public ObjectNode openOneBox(String cabinetId, String boxNum) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", boxNum);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_OPEN_ONE_BOX, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"操作成功","data":{"phone":"61592027780","pwd":"7104"}}
        return resultNode;
    }

    /**
     * 用户或管理员设置指定箱门的开箱密码(可控制是否开箱) 六位数字密码
     * 需要配合 3.16 设置设备工作模式 接口一块使用,设备默认 工作模式为 5：手机号+密码
     * openCode6 String 6位数字开箱密码
     * effectFlag int 0:临时 1:长期
     * isOpen int 0:不开箱 1:开箱
     */

    public ObjectNode setOpenCode6(String cabinetId, String boxNo, String password, String effectFlag, String isOpen) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", boxNo);
        map.put("openCode6", password);
        map.put("effectFlag", effectFlag);
        map.put("isOpen", isOpen);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_SET_OPEN_CODE6, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
        //{"code":200,"message":"success","data":null}
    }

    /**
     * 用户或管理员设置指定箱门的开箱密码(可控制是否开箱) 八位数字密码
     * 需要配合 3.16 设置设备工作模式 接口一块使用,设备默认 工作模式为 5：手机号+密码
     * openCode8 String 8位数字开箱密码
     * effectFlag int 0:临时 1:长期
     * isOpen int 0:不开箱 1:开箱
     */
    public ObjectNode setOpenCode8(String cabinetId, String boxNo, String password, String effectFlag, String isOpen) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", boxNo);
        map.put("openCode8", password);
        map.put("effectFlag", effectFlag);
        map.put("isOpen", isOpen);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_SET_OPEN_CODE8, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
        //{"code":200,"message":"success","data":null}
    }

    /**
     * 用户或管理员设置指定箱门的开箱密码(可控制是否开箱) 11位手机号+四位数字密码
     * 需要配合 3.16 设置设备工作模式 接口一块使用,设备默认 工作模式为 5：手机号+密码
     * openCode11 String 11位手机号(setOpenCode接口使用，同时需要填写pwd参数)
     * pwd String 4位数字开箱密码(setOpenCode接口)
     * effectFlag int 0:临时 1:长期
     * isOpen int 0:不开箱 1:开箱
     */
    public ObjectNode setOpenCode(String cabinetId, String boxNo, String phoneNumber, String password, String effectFlag, String isOpen) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", boxNo);
        map.put("openCode11", phoneNumber);
        map.put("pwd", password);
        map.put("effectFlag", effectFlag);
        map.put("isOpen", isOpen);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_SET_OPEN_CODE, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
        //{"code":200,"message":"操作成功","data":{"phone":"18888888888","pwd":"123"}}
    }

    /**
     * 管理员打开全部箱门,不清数据
     */
    public ObjectNode openAllBox( String cabinetId) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_OPEN_ALL_BOX, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
        //{"code":200,"message":"success","data":null}
    }

    /**
     * 管理员或用户打开指定箱门(可控制是否开箱和清除密码)
     * optType int 是否清除数据 0:不清楚数据 1:清除数据
     * isOpen int 是否开门 0:不开箱 1:开箱
     */
    public ObjectNode clearOneBox(String cabinetId, String boxNo, String optType, String isOpen) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", boxNo);
        map.put("optType", optType);
        map.put("isOpen", isOpen);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_CLEAR_ONE_BOX, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"success","data":null}
        return resultNode;
    }

    /**
     * 管理员打开全部箱门并清除所有开箱密码 直接开门并清除数据
     */
    public ObjectNode clearAllBox(String cabinetId) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_CLEAR_ALL_BOX, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
        //{"code":200,"message":"success","data":null}
    }

    /**
     * 管理员打开全部箱门并清除所有开箱密码  可控制是否开门是否清除数据
     * optType int (接口clearAllBox2必填项)是否清除数据 0:不清楚数据 1:清除数据
     * isOpen int (接口clearAllBox2必填项)是否开门 0:不开箱 1:开箱
     */

    public ObjectNode clearAllBox2(String cabinetId, String optType, String isOpen) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("optType", optType);
        map.put("isOpen", isOpen);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_CLEAR_ALL_BOX2, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"success","data":null}
        return resultNode;
    }

    /**
     * 设置设备工作模式
     * openMode int 工作模式，2：刷卡 3：6位密码 4：8位密码 5：手机号+密码
     */
    public ObjectNode setWorkMode(String cabinetId, String openMode) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("openMode", "5");
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_SET_WORK_MODE, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"success","data":null}
        return resultNode;
    }

    /**
     * 用户开箱操作(带存取标志开箱接口,无密码)
     * optType int 存取标志 1存 2取 3中途开箱
     */
    public ObjectNode openBoxCQ( String cabinetId, String boxNo, String optType) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", boxNo);
        map.put("optType", optType);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_OPEN_BOX_CQ, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"success","data":null}
        return resultNode;
    }

    /**
     * 管理员开箱操作(可一次操作多个箱子，无密码；可选是否开箱，是否清除数据)
     * boxNo String 逗号分隔的箱号数据,理论值1至200，建议1~120 示例: 1,3,5,6,7,10
     * optType int 是否清除数据 0:不清楚数据 1:清除数据
     * isOpen int 是否开门 0:不开箱 1:开箱
     */
    public ObjectNode openMulitBox( String cabinetId, String multiBoxNo, String optType, String isOpen) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", multiBoxNo);
        map.put("optType", optType);
        map.put("isOpen", isOpen);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_OPEN_MULIT_BOX, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"success","data":null}
        return resultNode;
    }

    /**
     * 初始化箱门(清除所有数据后重建)
     * boxCount int 箱门总数，范围：1-120
     */

    public ObjectNode initBoxs( String cabinetId, String boxCount) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxCount", boxCount);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_INIT_BOXS, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        //{"code":200,"message":"创建箱门数:12,删除箱门数0","data":{"msgId":null,"cabinetId":"931146737","timeStamp":"1703832574721","appKey":"voqu055xo0zjrjqq363d4pdqj4m8b6j9","sign":"49fb1b53b3b658bd69ec3f64d5225803","cabinetName":null,"halfWayType":null,"startBoxNo":null,"boxCount":"12","openMode":null,"text1":null,"text2":null,"text3":null,"qrCode":null,"qrCodeLevel":null,"isOpenVoice":null,"boxNo":null,"effectFlag":null,"isOpen":null,"openCode11":null,"pwd":null,"openCode6":null,"openCode8":null,"optType":null,"lightFlag":null,"heatFlag":null,"disinfectFlag":null,"chanelNum":null,"remark":null,"rcifUrl":null,"rcifType":null,"ip":"127.0.0.1","port":"8282","totalSize":null,"bbh":null,"acc":null,"userFlag":null,"pointsBoxModel":null,"orderId":null,"takeCode":null,"manageCodeType":null}}
        return resultNode;
    }

    /**
     *  设置设备语音开关
     *  isOpenVoice String 1:打开，0:关闭。为保证并发需关闭此项
     */
    public ObjectNode isOpenVoice( String cabinetId,String isOpenVoice) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("isOpenVoice", isOpenVoice);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_IS_OPEN_VOICE, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
    }


    /**
     *  配餐柜存餐(调用接口后服务器返回用户使用的箱门)
     *  orderId String 订单编号(不重复)
     *  openCode11 String 手机号(可虚拟手机,平台核对使用)
     *  lightFlag int 是否开启照明(0:保持现状，1:开启，2:关闭)
     *  heatFlag int 是否开启加热(0:保持现状，1:开启，2:关闭)
     *  disinfectFlag int 是否开启消毒(0:保持现状，1:开启，2:关闭)
     *  boxType int 用户调用3.11接口自定义的箱门类型,不传默认0随机分配
     *  takeCode string 取餐码,4位纯数字.不传时由服务器生成
     */
    public ObjectNode businessPutMeal(String cabinetId,String orderId,String openCode11,String lightFlag, String heatFlag, String disinfectFlag ,String boxType,String takeCode) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("orderId", orderId);
        map.put("openCode11", openCode11);
        map.put("lightFlag",lightFlag);
        map.put("heatFlag",heatFlag);
        map.put("disinfectFlag",disinfectFlag);
        map.put("boxType",boxType);
        map.put("takeCode",takeCode);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_BUSINESS_PUT_MEAL, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
    }


    /**
     *  配餐柜取餐
     *  orderId String 订单编号(不重复)
     */
    public ObjectNode businessTakeMeal( String cabinetId,String orderId) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("orderId", orderId);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_BUSINESS_TAKE_MEAL, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
    }

    /**
     * 配餐柜撤餐
     * orderId String 订单编号(不重复)
     * isOpen int 是否开箱0:不开箱 1:开箱(不传默认不开)
     */
    public ObjectNode recallMeal(String cabinetId,String orderId,String isOpen) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("orderId", orderId);
        map.put("isOpen",isOpen);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_RECALL_MEAL, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
    }

    /**
     * 配餐柜管理指令
     * boxNo int 箱门号号
     * manageCodeType int 管理指令类型5:管理开箱 6锁定 7解锁 8清箱 9.全清
     */
    public ObjectNode buffetManage( String cabinetId,String boxNo,String manageCodeType) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", boxNo);
        map.put("manageCodeType",manageCodeType);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_BUFFET_MANAGE, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
    }


    /**
     *  自定义箱门类型(调用3.6初始化箱门后可用,否则即使调用也无效,重复调用初始化箱门后需重新修改箱门类型)
     *  boxNo string 箱门号字符串,多个箱门使用英文逗号隔开.例: 1,2,3
     *  boxType int 箱门类型,服务器默认0。用户自定义不可为0.例用户自定义 1:大箱 2:中箱 3:小箱。如自定义箱门,调用存餐时需传入
     */
    public ObjectNode upBoxType( String cabinetId,String boxNo,String boxType) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
        map.put("boxNo", boxNo);
        map.put("boxType",boxType);
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_UP_BOX_TYPE, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
    }


    /**
     *  自定义箱门类型(调用3.6初始化箱门后可用,否则即使调用也无效,重复调用初始化箱门后需重新修改箱门类型)
     *  boxNo string 箱门号,纯数字。不传时查询全部箱门
     */
    public ObjectNode queryBuffetBoxInfo( String cabinetId,String boxNo) {
        Map<String, String> map = makeSignParamMap(appkey, cabinetId);
//        map.put("cabinetId", cabinetId);
        if (!ValidationUtil.isEmpty(boxNo)) {
            map.put("boxNo",boxNo);
        }
        String paramStr = buildParamStr(map);
        String sign = calcSign(paramStr, secretKey);
        String url = buildUrl(API_QUERY_BUFFET_BOX_INFO, paramStr, sign);
        ObjectNode node = mapToJson(map, sign);
        ObjectNode resultNode = executeJsonRequest(url, node);
        return resultNode;
    }


    public String encodeUrl(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private String buildUrl(String api, String paramStr, String sign) {
        return API_URL + api + "?" + paramStr + getSignParamStr(sign);
    }

    private String getSignParamStr(String sign) {
        return "&sign=" + sign;
    }

    private Map<String, String> makeSignParamMap(String appkey, String cabinetId) {
        Map<String, String> map = new TreeMap<>();
        map.put("appKey", appkey);
        map.put("cabinetId", cabinetId);
        String timeStamp = System.currentTimeMillis() + "";
        map.put("timeStamp", timeStamp);
        return map;
    }

    private ObjectNode mapToJson(Map<String, String> map, String sign) {
        ObjectNode node = Json.newObject();
        map.put("sign", sign);
        map.forEach((k, v) -> node.put(k, v));
        return node;
    }

    private ObjectNode executeJsonRequest(String url, ObjectNode param) {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null,
                    (TrustStrategy) (chain, authType) -> true).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient client = HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
            HttpPost post = new HttpPost(url);
            post.addHeader("content-type", "application/json");
            post.setEntity(new StringEntity(Json.stringify(param)));
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                ObjectNode objectNode = (ObjectNode) Json.parse(result);
                System.out.println(url);
                System.out.println(Json.stringify(objectNode));
                return objectNode;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

//    
//    public void getBoxInfo() {
//        String url = API_URL + API_GET_BOX_INFO;
//        ObjectNode jsonBody = Json.newObject();
//        String timeStamp = System.currentTimeMillis() + "";
//        Map<String, String> map = new TreeMap<>();
//        map.put("appKey", appkey);
//        map.put("cabinetId", cabinetId);
//        map.put("timeStamp", timeStamp);
//        map.put("boxNo", "255");
//        //加签名
//        String sign = buildParam(map, secretKey);
//        System.out.println("sign: " + sign);
//        String url2 = buildParamStr(keyValueList);
//        url2 = "?" + url2 + "&sign=" + sign;
//        url += url2;
//        for (KeyValue keyValue : keyValueList) {
//            jsonBody.put(keyValue.key, keyValue.value);
//        }
//        jsonBody.put("sign", sign);
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(jsonBody));
//
//
//        Request myRequest = new Request.Builder()
//                .url(url)
//                .post(body)
//                .addHeader("content-type", "application/json")
//                .build();
//
//        try {
//            Response response = client.newCall(myRequest).execute();
//            System.out.println(response.toString());
//            System.out.println("message" + response.message().toString());
//            System.out.println(response.body().string());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//    }


    //设置门箱数
    //String url = "https://open.qianmingyun.com/webapi/apiService/v1/s1/set_totalBoxNum";
//    
//    public void openAllBox() {

    //检查设备在线
    //打开所有柜子
    //String url = "https://open.qianmingyun.com/webapi/apiService/v1/s1/chkDevice";
//        String url = "https://open.qianmingyun.com/webapi/apiService/v1/s1/openAllBox";
//        OkHttpClient client = new OkHttpClient();
//        JSONObject jsonBody = new JSONObject();
//
//        String appkey = "voqu055xo0zjrjqq363d4pdqj4m8b6j9";
//        String secretKey = "gic122v1hamv3ss8ip1m6r0icfckpvfz";
//        String cabinetId = "931146737";
//
//        String timeStamp = System.currentTimeMillis() + "";
//        ArrayList<KeyValue> keyValueList = new ArrayList<>();
//        keyValueList.add(new KeyValue("appKey", appkey));
//        keyValueList.add(new KeyValue("cabinetId", cabinetId));
//        keyValueList.add(new KeyValue("timeStamp", timeStamp));
//        //keyValueList.add(new KeyValue("remark", "99@6.6"));
//        //keyValueList.add(new KeyValue("msgId", "66@99"));
//
//
//        //加签名
//        String sign = buildParam(keyValueList, secretKey);
//        System.out.println("sign: " + sign);
//        String url2 = buildParamStr(keyValueList);
//        url2 = "?" + url2 + "&sign=" + sign;
//        url += url2;
//        for (KeyValue keyValue : keyValueList) {
//            jsonBody.put(keyValue.key, keyValue.value);
//        }
//        jsonBody.put("sign", sign);
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(jsonBody));
//
//
//        Request myRequest = new Request.Builder()
//                .url(url)
//                .post(body)
//                .addHeader("content-type", "application/json")
//                .build();
//
//        try {
//            Response response = client.newCall(myRequest).execute();
//            System.out.println(response.toString());
//            System.out.println("message" + response.message().toString());
//            System.out.println(response.body().string());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//    }


    public void mytest() {
        //验签
//        String url = "https://open.qianmingyun.com/webapi/apiService/v1/s1/getSign";
//        OkHttpClient client = new OkHttpClient();
//        JSONObject jsonBody = new JSONObject();
//
//        String appkey = "AMPAR8N61MjYQLMf1PIDRFILYknC1YEUgAN5Q4O0U6Q";
//        String secretKey = "tWrGMD4C3J7G468936291";
//        String timeStamp = System.currentTimeMillis() + "";
//        ArrayList<KeyValue> keyValueList = new ArrayList<>();
//        keyValueList.add(new KeyValue("appKey", appkey));
//        keyValueList.add(new KeyValue("cabinetId", "538564867"));
//        keyValueList.add(new KeyValue("disinfectFlag", "1"));
//        keyValueList.add(new KeyValue("heatFlag", "2"));
//        keyValueList.add(new KeyValue("isOpen", "1"));
//        keyValueList.add(new KeyValue("lightFlag", "1"));
//        keyValueList.add(new KeyValue("optType", "1"));
//        keyValueList.add(new KeyValue("timeStamp", "1597644971397"));
//        keyValueList.add(new KeyValue("boxNo", "2"));
//        keyValueList.add(new KeyValue("remark", "99@6.6"));
//        keyValueList.add(new KeyValue("msgId", "66@99"));
//        //加签名
//        String sign = buildParam(keyValueList, secretKey);
//        System.out.println("sign: " + sign);
//        String url2 = buildParamStr(keyValueList);
//        url2 = "?" + url2 + "&sign=" + sign;
//        url += url2;
//        for (KeyValue keyValue : keyValueList) {
//            jsonBody.put(keyValue.key, keyValue.value);
//        }
//        jsonBody.put("sign", sign);
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(jsonBody));
//
//
//        Request myRequest = new Request.Builder()
//                .url(url)
//                .post(body)
//                .addHeader("content-type", "application/json")
//                .build();
//
//        try {
//            Response response = client.newCall(myRequest).execute();
//            System.out.println(response.toString());
//            System.out.println("message" + response.message().toString());
//            System.out.println(response.body().string());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public String calcSign(String param, String secretKey) {
        String finalStr = param + secretKey;
        EncodeUtils encodeUtils = new EncodeUtils();
        return encodeUtils.getMd5(finalStr);
    }

    public String buildParamStr(Map<String, String> map) {
        StringJoiner stringJoiner = new StringJoiner("&");
        map.forEach((k, v) -> {
            stringJoiner.add(k + "=" + v);
        });
        return stringJoiner.toString();
    }

}
