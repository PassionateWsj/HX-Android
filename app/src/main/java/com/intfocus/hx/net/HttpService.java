package com.intfocus.hx.net;

import com.intfocus.hx.data.request.CommentBody;
import com.intfocus.hx.data.request.RequestFavourite;
import com.intfocus.hx.data.response.BaseResult;
import com.intfocus.hx.data.response.article.ArticleResult;
import com.intfocus.hx.data.response.assets.AssetsResult;
import com.intfocus.hx.data.response.filter.MenuResult;
import com.intfocus.hx.data.response.home.HomeMsgResult;
import com.intfocus.hx.data.response.home.KpiResult;
import com.intfocus.hx.data.response.home.ReportListResult;
import com.intfocus.hx.data.response.home.WorkBoxResult;
import com.intfocus.hx.data.response.login.RegisterResult;
import com.intfocus.hx.data.response.mine_page.NoticeContentResult;
import com.intfocus.hx.data.response.mine_page.UserInfoResult;
import com.intfocus.hx.data.response.notice.NoticesResult;
import com.intfocus.hx.data.response.scanner.StoreListResult;
import com.intfocus.hx.login.bean.Device;
import com.intfocus.hx.login.bean.DeviceRequest;
import com.intfocus.hx.login.bean.NewUser;
import com.intfocus.hx.util.K;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by CANC on 2017/7/31.
 */

public interface HttpService {

    /**
     * 申请注册
     *
     * GET
     * /api/v1.1/config/info
     *
     * keyname
     */
    @GET(K.KRegister)
    Observable<RegisterResult> getRegister(@Query("keyname") String keyName);

    /**
     * 推送 push_token
     *
     * POST
     * /api/v1.1/device/push_token
     *
     * uuid
     * push_token
     */
    @POST(K.KPushToken)
    Observable<BaseResult> putPushToken(@Query("uuid") String uuid, @Query("push_token") String pushToken);

    /**
     * 获取AssetsMD5
     *
     * GET
     * /api/v1.1/assets/md5
     */
    @GET(K.KAssetsMD5)
    Observable<AssetsResult> getAssetsMD5();

    /**
     * 公告预警详情
     *
     * GET
     * /api/v1.1/my/view/notice
     *
     * notice_id
     * user_num
     */
    @GET(K.KNoticeContent)
    Observable<NoticeContentResult> getNoticeContent(@Query("notice_id") String noticeId, @Query("user_num") String userNum);

    /**
     * 发表评论
     *
     * POST
     * /api/v1.1/comment
     *
     * "user_num": "",
     * "content": "",
     * "object_type": ,
     * "object_id": ,
     * "object_title": ""
     */
    @POST(K.KComment)
    Observable<BaseResult> submitComment(@Body CommentBody commentBody);

    /**
     * 工具箱页
     *
     * GET
     * /api/v1.1/app/component/toolbox
     *
     * group_id
     * role_id
     */
    @GET(K.KWorkBoxList)
    Observable<WorkBoxResult> getWorkBox(@Query("group_id") String groupId, @Query("role_id") String roleId);

    /**
     * 报表页面列表
     *
     * GET
     * /api/v1.1/app/component/reports
     *
     * group_id
     * role_id
     */
    @GET(K.KReportList)
    Observable<ReportListResult> getReportList(@Query("group_id") String groupId, @Query("role_id") String roleId);

    /**
     * 门店列表
     *
     * GET
     * /api/v1.1/user/stores
     *
     * user_num
     */
    @GET(K.KStoreList)
    Observable<StoreListResult> getStoreList(@Query("user_num") String userNum);

    /**
     * 用户信息
     *
     * GET
     * /api/v1.1/my/statistics
     *
     * user_num
     */
    @GET(K.KUserInfo)
    Observable<UserInfoResult> getUserInfo(@Query("user_num") String userNum);

    /**
     * 获取概况页公告列表
     *
     * GET
     * /api/v1.1/user/notifications
     *
     * group_id
     * role_id
     */
    @GET(K.KNotifications)
    Observable<HomeMsgResult> getNotifications(@Query("group_id") String groupId, @Query("role_id") String roleId);

    /**
     * 扫码结果
     *
     * GET
     * /api/v1.1/scan/barcode
     *
     * store_id
     * code_info
     */
    @GET(K.KScannerResult)
    Observable<BaseResult> getScannerResult(@Query("store_id") String storeId, @Query("code_info") String codeInfo);

    /**
     * 获取文章收藏列表
     *
     * GET
     * /api/v1.1/my/favourited/articles
     *
     * user_num
     * page
     * limit
     */
    @GET(K.KMyFavouritedList)
    Observable<ArticleResult> getMyFavouritedList(@QueryMap Map<String, String> queryMap);

    /**
     * 收藏状态
     *
     * POST
     * /api/v1.1/my/article/favourite_status
     *
     * "user_num": "123",
     * "article_id": "1",
     * "favourite_status": "1
     */
    @POST(K.KFavouriteStatus)
    Observable<BaseResult> articleOperating(@Body RequestFavourite requestFavourite);

    /**
     * 获取数据学院文章列表
     *
     * GET
     * /api/v1.1/my/articles
     *
     * user_num
     * page
     * limit
     */
    @GET(K.KArticlesList)
    Observable<ArticleResult> getArticleList(@QueryMap Map<String, String> queryMap);


    /**
     * 获取首页概况数据
     *
     * GET
     * /api/v1.1/app/component/overview
     *
     * group_id
     * role_id
     */
    @GET(K.KOverview)
    Observable<KpiResult> getHomeIndex(@QueryMap Map<String, String> queryMap);

    /**
     * 获取首页消息数据
     *
     * GET
     * /api/v1.1/user/notifications
     *
     * group_id
     * role_id
     */
    @GET(K.KNotifications)
    Observable<HomeMsgResult> getHomeMsg(@QueryMap Map<String, String> queryMap);

    /**
     * 公告预警列表
     *
     * GET
     * /api/v1.1/my/notices
     *
     * user_num
     * type
     * page
     * limit
     */
    @GET(K.KNoticeList)
    Observable<NoticesResult> getNoticeList(@QueryMap Map<String, String> queryMap);

    /**
     * 获取筛选菜单信息
     *
     * GET
     * /api/v1/report/menus
     */
    @GET(K.KFilterMenuPath)
    Observable<MenuResult> getFilterMenu();


    /**
     * 头像上传
     *
     * POST
     * /api/v1.1/upload/gravatar
     *
     * "user_num": "",
     * "device_id": "",
     * "gravatar": ""
     */
    @Multipart
    @POST(K.kNewUserIconUploadPath)
    Observable<BaseResult> userIconUpload(@Query("device_id") String deviceId, @Query("user_num") String user_num, @Part MultipartBody.Part file);

    /**
     * 登录post请求
     *
     * POST
     * /api/v1.1/user/authentication
     *
     * user_num  　用户名
     * password 　密码
     */
    @POST(K.KNewLogin)
    Observable<NewUser> userLogin(@Query("user_num") String userNum, @Query("password") String password, @Query("coordinate") String coordinate);

    /**
     * 上传设备信息
     *
     * POST
     * /api/v1.1/app/device
     *
     *   "user_num": "",
     *   "device": {
     *      "uuid": "",
     *      "os": "",
     *      "name": "",
     *      "os_version": "",
     *      "platform": ""
     *   },
     *   "app_version": "",
     *   "ip": "",
     *   "browser": ""
     */
    @POST(K.KNewDevice)
    Observable<Device> deviceUpLoad(@Body DeviceRequest deviceRequest);

    /**
     * 退出登录
     *
     * POST
     * /api/v1.1/user/logout
     *
     * user_device_id
     */
    @POST(K.KNewLogout)
    Observable<BaseResult> userLogout(@Query("user_device_id") String userDeviceId);

    /**
     * 更新密码
     *
     * POST
     * /api/v1.1/user/update_password
     *
     * user_num 　用户名
     * password  　新密码
     */
    @POST(K.KNewUpdatePwd)
    Observable<BaseResult> updatePwd(@Query("user_num") String userNum, @Query("password") String newPwd);

    /**
     * 重置密码
     *
     * POST
     * /api/v1.1/user/reset_password
     *
     * user_num 　用户名
     * mobile 　手机号
     */
    @POST(K.KNewResetPwd)
    Observable<BaseResult> resetPwd(@Query("user_num") String userNum, @Query("mobile") String mobile);

    /**
     * 用户信息
     *
     * GET
     * /api/v1.1/my/statistics
     *
     * user_num
     */
    @GET(K.KNewChoiceMenu)
    Call<MenuResult> getChoiceMenus(@Query("params") String params);

    /**
     * 下载静态资源
     *
     * GET
     * /api/v1.1/download/assets
     *
     * filename
     */
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
