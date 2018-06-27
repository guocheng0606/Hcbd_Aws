package com.android.hcbd.aws.util;

/**
 * Created by guocheng on 2017/6/19.
 */

public class HttpUrlUtils {
    /*112.124.108.24*/
    //public static String BASEURL = "http://112.124.108.24:8080/oms";

    public static String login_url = "/awsApp/appLoginAction!login.action";//登录url
    public static String get_scan_data_url = "/awsApp/appCheckDataAction!getScanData.action";
    public static String pre_check_to_list_url = "/awsApp/appPreCheckDataAction!toList.action"; //预检
    public static String pre_check_list_url = "/awsApp/appPreCheckDataAction!list.action"; //预检查询
    public static String check_list_url = "/awsApp/appCheckDataAction!list.action"; //精检查询
    public static String check_to_edit_url = "/awsApp/appCheckDataAction!toEdit.action"; //精检条件
    public static String check_print_url = "/awsApp/appCheckDataAction!print.action"; //打印
    public static String initial_check_save_url = "/awsApp/appCheckDataAction!saveData.action"; //预检添加
    public static String re_check_save_url = "/awsApp/appCheckDataAction!saveCheckData.action"; //复检
    public static String check_setting_list_url = "/awsApp/appTypeAction!list.action"; //参数设置查询
    public static String check_setting_to_edit_url = "/awsApp/appTypeAction!toEdit.action"; //参数设置准备编辑
    public static String check_setting_edit_url = "/awsApp/appTypeAction!edit.action"; //参数设置准备编辑
    public static String update_password_url = "/awsApp/appLoginAction!editPwd.action"; //修改密码
    public static String pre_check_data_url = "/awsApp/appPreCheckDataAction!historyList.action"; //预检历史
    public static String led_show_list_url = "/awsApp/appLedAction!list.action"; //LED显示
    public static String pre_history_export_url = "/awsApp/appPreCheckDataAction!export.action"; //预检历史导出
    public static String inspection_history_export_url = "/awsApp/appCheckDataAction!export.action"; //精检历史导出
    public static String car_type_list_url = "/awsApp/appCarTypeAction!list.action"; //车型信息查询
    public static String car_type_edit_url = "/awsApp/appCarTypeAction!edit.action"; //车型信息编辑

    public static String pre_check_car_flow_url = "/awsApp/appPreCheckDataAction!carFlow.action"; //车流量分析
    public static String check_data_compare_url = "/awsApp/appCheckDataAction!dataCompare.action"; //和预检对比
    public static String led_car_notice_url = "/awsApp/appLedAction!carNotice.action"; //进站分析


}
