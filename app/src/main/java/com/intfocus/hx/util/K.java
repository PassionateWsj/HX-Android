package com.intfocus.hx.util;

import java.io.Serializable;

/**
 * api 链接，宏
 *
 * @author jay
 * @version 1.0
 * @created 2016-01-06
 * Created by lijunjie on 16/9/22.
 */
public class K extends PrivateURLs implements Serializable {

    public final static Integer kTimerInterval = 30;

    /**
     * API#paths
     */
    public final static String kUserAuthenticateAPIPath = "%s/api/v1/%s/%s/%s/authentication";
    public final static String kUserForgetAPIPath = "%s/api/v1/:platform/reset_password";
    public final static String kReportDataAPIPath = "%s/api/v1/group/%s/template/%s/report/%s/zip";
    public final static String kReportJsonDataAPIPath = "%s/api/v1/group/%s/template/%s/report/%s/jzip";
    public final static String kReportJsonAPIPath = "%s/api/v1/group/%s/template/%s/report/%s/json";
//    public final static String kCommentAPIPath = "%s/api/v1/user/%d/id/%d/type/%d";

    public final static String kDeviceStateAPIPath = "%s/api/v1/user_device/%s/state";
    public final static String kRsetPwdAPIPath = "%s/api/v1/update/%s/password";
    public final static String kPushDeviceTokenAPIPath = "%s/api/v1/device/%s/push_token/%s";
    public final static String kBarCodeScanAPIPath = "%s/api/v1/group/%s/role/%s/user/%s/store/%s/barcode_scan?code_info=%s&code_type=%s";
    public final static String kBarCodeScanAPIViewPath = "%s/mobile/v2/store/%s/barcode/%s/view";
    public final static String kBarCodeScanAPIDataPath = "%s/mobile/v2/store/%s/barcode/%s/attachment";
    //    public final static String kDownloadAssetsAPIPath = "%s/api/v1/download/%s.zip";
//    public final static String kDeviceTokenAPIPath = "%s/api/v1/user/%s/devices";
    public final static String kDeleteDeviceIdAPIPath = "%s/api/v1/android/%s/logout";

    /**
     * Mobile#View Path
     */
    public final static String kKPIMobileDataPath = "%s/mobile/%s/data/group/%s/role/%s/kpi";
    public final static String kKPIApiDataPath = "%s/api/v1/group/%s/role/%s/kpi";
    public final static String kMessageDataMobilePath = "%s/api/v1/role/%s/group/%s/user/%s/message";
    public final static String kCommentMobilePath = "%s/mobile/%s/id/%s/type/%s/comment";
    public final static String kThursdaySayMobilePath = "%s/mobile/%s/thursday_say";
    public final static String kNewKPIApiDataPath = "/api/v1/group/{groupId}/role/{roleId}/kpi";
    public final static String kNewMsgDataMobilePath = "/api/v1/role/{roleId}/group/{groupId}/user/{userId}/message";

    /**
     * MinePage#API
     */
    public final static String KUserInfoPath = "%s/api/v1/user/%s/group/%s/role/%s/statistics";
    public final static String kNoticeListPath = "%s/api/v1/user/%s/notices?type=%s&page=%s&limit=%s";
    public final static String KAppListPath = "%s/api/v1/group/%s/role/%s/apps";
    public final static String KWorkBoxListPath = "%s/api/v1/group/%s/role/%s/app_covers";
    public final static String KReportsListPath = "%s/api/v1/group/%s/role/%s/analyses";
    public final static String KInstituteListPath = "/api/v1/user/{userId}/page/{page}/limit/{pageSize}/articles";
    public final static String KInstituteCollectionPath = "%s/api/v1/user/%s/article/%s/favourite_status/%s";
    public final static String KFavouriteArticlesPath = "/api/v1/user/{userId}/page/{page}/limit/{pageSize}/favourite_articles";
    public final static String KArticleCollectionPath = "/api/v1/user/{userId}/article/{articleId}/favourite_status/{status}";
    public final static String kNewNoticeListPath = "/api/v1/user/{userId}/notices";
    public final static String kUserIconUploadPath = "/api/v1/device/{deviceId}/upload/user/{userId}/gravatar";

    /**
     * 筛选
     */
    public final static String KFilterMenuPath = "/api/v1/report/menus";

    /**
     * Config#Application
     */
    public final static String kConfigDirName = "Configs";
    public final static String kSharedDirName = "Shared";
    public final static String kCachedDirName = "Cached";
    public final static String kHTMLDirName = "HTML";
    public final static String kAssetsDirName = "Assets";
    public final static String kReportDataFileName = "group_%s_template_%s_report_%s.js";
    public final static String kUserConfigFileName = "user.json";
    public final static String kPushMessageFileName = "push_message.json";
    public final static String kSettingConfigFileName = "setting.json";
    public final static String kTabIndexConfigFileName = "page_tab_index.json";
    public final static String kGesturePwdConfigFileName = "gesture_password.json";
    public final static String kLocalNotificationConfigFileName = "local_notification.json";
    public final static String kCachedHeaderConfigFileName = "cached_header.json";
    public final static String kPgyerVersionConfigFileName = "pgyer_version.json";
    public final static String kGravatarConfigFileName = "gravatar.json";
    public final static String kBetaConfigFileName = "beta_v0.json";
    public final static String kBarCodeResultFileName = "barcode_result.json";
    public final static String kScanBarCodeHTMLName = "scan_bar_code.html";
    public final static String kCurrentVersionFileName = "current_version.txt";
    public final static String kBehaviorConfigFileName = "behavior.json";
    public final static String kTemplateV1 = "templateV1";

    /**
     * Config#User Model
     */
    public final static String kAppVersion = "app_version";
    public final static String kFontsMd5 = "fonts_md5";
    public final static String kImagesMd5 = "images_md5";
    public final static String kAssetsMd5 = "assets_md5";
    public final static String kIconsMd5 = "icons_md5";
    public final static String kStylesheetsMd5 = "stylesheets_md5";
    public final static String kJavaScriptsMd5 = "javascripts_md5";
    public final static String kInfo = "info";
    public final static String kValid = "valid";
    public final static String kUserId = "user_id";
    public final static String kUserName = "user_name";
    public final static String kUserDeviceId = "user_device_id";
    public final static String kCurrentUIVersion = "current_ui_version";

    /**
     * Config#Push Message
     */
    public final static String kPushConfigFileName = "push_message_config.json";
    public final static String kPushIsValid = "push_valid";
    public final static String kPushDeviceToken = "push_token";
    public final static String kPushDeviceUUID = "device_uuid";

    /**
     * Config#User Device Info
     */
    public final static int kMinSdkVersion = 14;
    public final static int kMaxSdkVersion = 25;

    /**
     * 新API及所需api key
     */
    public final static String API_TOKEN = "api_token";
    public final static String ANDROID_API_KEY = "578905f6e0c4189caa344ee4b1e460e5";//加密所需api key
    public final static String KScannerResult = "/api/v1.1/scan/barcode"; //扫码结果
    public final static String KUserInfo = "/api/v1.1/my/statistics"; //个人信息
    public final static String KNoticeList = "/api/v1.1/my/notices";//公告预警
    public final static String KNoticeContent = "/api/v1.1/my/view/notice"; //公告预警详情
    public final static String KArticlesList = "/api/v1.1/my/articles";//数据学院文章列表
    public final static String KFavouriteStatus = "/api/v1.1/my/article/favourite_status";//收藏状态
    public final static String KMyFavouritedList = "/api/v1.1/my/favourited/articles";//我的收藏列表
    public final static String KOverview = "/api/v1.1/app/component/overview";//生意概况
    public final static String KNotifications = "/api/v1.1/user/notifications";//用户公告
    public final static String KStoreList = "/api/v1.1/user/stores"; //门店列表
    public final static String KReportList = "/api/v1.1/app/component/reports"; //报表列表
    public final static String KWorkBoxList = "/api/v1.1/app/component/toolbox"; //工具箱
    public final static String KNewLogin = "/api/v1.1/user/authentication"; //用户验证
    public final static String KNewDevice = "/api/v1.1/app/device"; //上传设备信息
    public final static String KNewLogout = "/api/v1.1/user/logout"; //退出登录
    public final static String KNewUpdatePwd = "/api/v1.1/user/update_password"; //更新密码
    public final static String KNewResetPwd = "/api/v1.1/user/reset_password"; //重置密码
    public final static String KComment = "/api/v1.1/comment"; //发表评论
    public final static String KRegister = "/api/v1.1/config/info"; //申请注册

    /**
     * "api_token": "",
     * "user_num": ,
     * "title": "",
     * "content": "",
     * "app_version":"",
     * "platform":"",
     * "platform_version":""
     */
    public final static String KFeedBack = "/api/v1.1/feedback"; //提交反馈
    public final static String KAssetsMD5 = "/api/v1.1/assets/md5"; //静态资源MD5
    public final static String KPushToken = "/api/v1.1/device/push_token"; //推送设备 push_token
    public final static String KReportBaseApi = "/api/v1.1/report/data"; //报表 Zip 数据
    public final static String KReportZipData = "%s/api/v1.1/report/data?api_token=%s&group_id=%s&template_id=%s&report_id=%s&disposition=zip"; //报表 Zip 数据
    public final static String kActionLog = "%s/api/v1.1/device/logger"; //上传设备
    public final static String kScreenLockAPIPath = "%s/api/v1.1/device/screen_lock"; //上传锁屏信息
    /**
     * /api/v1.1/download/assets
     * <p>
     * filename=javascripts.zip
     */
    public final static String kDownloadAssetsAPIPath = "%s/api/v1.1/download/assets?api_token=d93c1a0dc03fe4ffad55a82febd1c94f&filename=%s.zip"; //下载静态资源
    /**
     * user_num
     * id
     */
    public final static String kDeviceTokenAPIPath = "/api/v1.1/user/devices";  //设备列表
    public final static String kNewUserIconUploadPath = "/api/v1.1/upload/gravatar"; //上传头像
    public final static String KNewChoiceMenu = "/api/v1.1/report/choice_menus";
    public final static String kDownloadAssetsZip = "/api/v1.1/download/assets";
}
