package com.seawindsolution.podphotographer.Servicew;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Ronak Gopani on 29/6/19 at 7:15 PM.
 */
public interface Url {

//    String URL_BASE = "https://www.podahmedabad.com/Webservices/";//Live
    String URL_BASE = "https://www.podahmedabad.com/test/Webservices/";//Test

    /* GENERAL */
    String GET_PROFILE = "getphotographerProfileByphotographerId/{Id}";
    String UPDATEPROFILE = "updatephotographerProfile";
    String GET_PAGE = "getPage/{Slug}";

    /* LOGIN */
    String LOGIN = "photographerLogin";

    /*Registration*/
    String REG_OTP = "photographerRegistrationSendOTP";
    String REG_RS_OTP = "photographerRegistrationReSendOTP";
    String USER_REGISTER = "photographerRegistration";

    /*Forgot Password*/
    String FORGOT_PASSWORD_OTP = "photographerForgotPasswordSendOTP";
    String RESET_PASSWORD = "photographerResetPassword";
    String FORGOT_PASSWORD_OTP_RESEND = "photographerForgotPasswordReSendOTP";
    String RESET_PASSWORD_IN_APP = "changephotographerPassword";

    /*Freelancer*/
    String FREELANCER = "joinFreelancer";

    /*Get Order*/
    String GET_ORDER = "getOrderByPhotohrapherId/{Id}";
    String GET_TODAY_ORDER = "getTodayOrderByPhotohrapherId/{Id}";
    String GET_TOMORROW_ORDER = "getTommorowOrderByPhotohrapherId/{Id}";
    String GET_WEECKLY_ORDER = "getWeekOrderByPhotohrapherId/{Id}";
    String GET_ORDER_BY_ID = "getPhotographerOrderByOrderId/{pid}/{oid}";

    /*Get Availibility*/
    String GET_AVAILABILITY = "addAvailibility";
    String GET_AVAILABILITYS = "getAvailibility/{Id}";

    /*Get Order*/
    String ORDER_SEND_OTP = "sendOrderOTPtoCustomer";
    String ORDER_RESEND_OTP = "reSendOrderOTPtoCustomer";
    String VERIFY_SEND_OTP = "verifyOrderOTP";
    String COMPLATE_ORDER = "complateOrderByOrderId";

    /*Tracking data*/
    String TRACKINGDATA = "insertPhotographerTrackingData";

    /*Get Queries*/
    String GENERAL_ISSUE = "addGeneralQuery";
    String CHECK_LIST = "checklist";
    String GET_HELP = "get247SupportSettings";

    /*Extend Status*/
    String EXTEND_STATUS = "extendOrderRequestChangeStatus";

    /*MSG*/
    String SEND_MSG = "sendMessages";
    String RECIVE_MSG = "getMessages/{sender}/{reciver}/{orderId}";

    /*Notification*/
    String NOTIFICATION = "getCustomNotificationPhotographer/{PhotographerId}";
    String DELETE_NOTIFICATION = "deleteCustomNotificationPhotographer/{Id}";
    String CLEAR_ALL = "clearAllPhotographerNotificationByphotographerId/{photographerId}";

    String GETAPPVERSION = "GetAppVersion/{id}";

    String EXPORT_ORDER = "export_All_Orders_Of_Photographer";

    static GRYBWebService getWebService() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(10, TimeUnit.MINUTES);
        httpClient.readTimeout(10, TimeUnit.MINUTES);
        httpClient.writeTimeout(10, TimeUnit.MINUTES);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("Connection", "close")
                    .addHeader(AppConstant.HEADER_KEY, AppConstant.HEADER_KEY_VALUE).build();
            return chain.proceed(request);
        }).addInterceptor(interceptor);
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL_BASE).client(httpClient.build()).build().create(GRYBWebService.class);
    }

    interface GRYBWebService {

        @Multipart
        @POST(REG_OTP)
        Call<ResponseBody> photographerRegistrationSendOTP(
                @Part("Name") RequestBody Name,
                @Part("Email") RequestBody Email,
                @Part("Phone") RequestBody Phone
        );

        @Multipart
        @POST(REG_RS_OTP)
        Call<ResponseBody> photographerRegistrationReSendOTP(
                @Part("Name") RequestBody Name,
                @Part("Email") RequestBody Email,
                @Part("Phone") RequestBody Phone
        );

        @Multipart
        @POST(USER_REGISTER)
        Call<ResponseBody> photographerRegistration(
                @Part("Name") RequestBody Name,
                @Part("Email") RequestBody Email,
                @Part("Phone") RequestBody Phone,
                @Part("Address") RequestBody Address,
                @Part("OTP") RequestBody OTP,
                @Part("Password") RequestBody Password,
                @Part("SignBy") RequestBody SignBy,
                @Part("SocialId") RequestBody SocialId,
                @Part("ProfileImageUrl") RequestBody ProfileImageUrl,
                @Part MultipartBody.Part ProfileImage
        );

        @POST(FORGOT_PASSWORD_OTP)
        @Multipart
        Call<ResponseBody> photographerForgotPasswordSendOTP(
                @Part("E_O_P") RequestBody E_O_P
        );

        @POST(RESET_PASSWORD)
        @Multipart
        Call<ResponseBody> photographerResetPassword(
                @Part("E_O_P") RequestBody E_O_P,
                @Part("Password") RequestBody Password,
                @Part("OTP") RequestBody OTP
        );

        @POST(FORGOT_PASSWORD_OTP_RESEND)
        @Multipart
        Call<ResponseBody> photographerForgotPasswordReSendOTP(
                @Part("E_O_P") RequestBody E_O_P
        );

        @POST(LOGIN)
        @Multipart
        Call<ResponseBody> photographerLogin(
                @Part("UserName") RequestBody UserName,
                @Part("Password") RequestBody Password
        );

        @GET(GET_PROFILE)
        Call<ResponseBody> getphotographerProfileByphotographerId(
                @Path("Id") int Id
        );

        @Multipart
        @POST(UPDATEPROFILE)
        Call<ResponseBody> updatephotographerProfile(
                @Part("Id") RequestBody Id,
                @Part("Name") RequestBody Name,
                @Part("Phone") RequestBody Phone,
                @Part("Address") RequestBody Address,
                @Part MultipartBody.Part ProfileImage
        );

        @Multipart
        @POST(RESET_PASSWORD_IN_APP)
        Call<ResponseBody> changephotographerPassword(
                @Part("Id") RequestBody role,
                @Part("OldPassword") RequestBody OldPassword,
                @Part("NewPassword") RequestBody NewPassword
        );

        @GET(GET_PAGE)
        Call<ResponseBody> getPage(
                @Path("Slug") String Slug
        );

        @Multipart
        @POST(FREELANCER)
        Call<ResponseBody> joinFreelancer(
                @Part("Name") RequestBody Name,
                @Part("Email") RequestBody Email,
                @Part("Phone") RequestBody Phone,
                @Part("Gender") RequestBody Gender,
                @Part("DOB") RequestBody DateOfBirth,
                @Part("Employment") RequestBody Employment,
                @Part("Area") RequestBody Area,
                @Part("City") RequestBody City,
                @Part("State") RequestBody State,
                @Part("Pin") RequestBody Pin,
                @Part("Country") RequestBody Country,
                @Part("Aadhar") RequestBody Aadhar,
                @Part("Experience") RequestBody Experience,
                @Part("ElectivePractice") RequestBody ElectivePractice,
                @Part("InstagramURL") RequestBody InstagramURL,
                @Part("PortfolioUrl") RequestBody PortfolioUrl,
                @Part("CameraBody") RequestBody CameraBody,
                @Part("Lances1") RequestBody Lances1,
                @Part("Lances2") RequestBody Lances2,
                @Part("Lances3") RequestBody Lances3,
                @Part("Availibility") RequestBody Availibility,
                @Part("ReadyToTrawelPodProject") RequestBody ReadyToTrawelPodProject,
                @Part("aboutPOD") RequestBody aboutPOD,
                @Part("Source") RequestBody Source,
                @Part("RegType") RequestBody RegType,
                @Part MultipartBody.Part ProfileImage,
                @Part MultipartBody.Part IdProof
        );

        @GET(GET_ORDER)
        Call<ResponseBody> getOrderByPhotohrapherId(
                @Path("Id") int Id
        );

        @GET(CLEAR_ALL)
        Call<ResponseBody> clearAllPhotographerNotificationByphotographerId(
                @Path("photographerId") int photographerId
        );

        @GET(GET_TODAY_ORDER)
        Call<ResponseBody> getTodayOrderByPhotohrapherId(
                @Path("Id") int Id
        );

        @GET(GET_TOMORROW_ORDER)
        Call<ResponseBody> getTommorowOrderByPhotohrapherId(
                @Path("Id") int Id
        );

        @GET(GET_WEECKLY_ORDER)
        Call<ResponseBody> getWeekOrderByPhotohrapherId(
                @Path("Id") int Id
        );

        @Multipart
        @POST(GET_AVAILABILITY)
        Call<ResponseBody> addAvailibility(
                @Part("photographerId") int photographerId,
                @Part("Date") RequestBody Date,
                @Part("00:00") int r1,
                @Part("00:30") int r2,
                @Part("1:00") int r3,

                @Part("1:30") int r31,
                @Part("2:00") int r32,
                @Part("2:30") int r33,

                @Part("3:00") int r4,
                @Part("3:30") int r5,
                @Part("4:00") int r6,

                @Part("4:30") int r34,
                @Part("5:00") int r35,
                @Part("5:30") int r36,

                @Part("6:00") int r7,
                @Part("6:30") int r8,
                @Part("7:00") int r9,

                @Part("7:30") int r37,
                @Part("8:00") int r38,
                @Part("8:30") int r39,

                @Part("9:00") int r10,
                @Part("9:30") int r11,
                @Part("10:00") int r12,

                @Part("10:30") int r40,
                @Part("11:00") int r41,
                @Part("11:30") int r42,

                @Part("12:00") int r13,
                @Part("12:30") int r14,
                @Part("13:00") int r15,

                @Part("13:30") int r43,
                @Part("14:00") int r44,
                @Part("14:30") int r45,

                @Part("15:00") int r16,
                @Part("15:30") int r17,
                @Part("16:00") int r18,

                @Part("16:30") int r46,
                @Part("17:00") int r47,
                @Part("17:30") int r48,

                @Part("18:00") int r19,
                @Part("18:30") int r20,
                @Part("19:00") int r21,

                @Part("19:30") int r49,
                @Part("20:00") int r50,
                @Part("20:30") int r51,

                @Part("21:00") int r22,
                @Part("21:30") int r23,
                @Part("22:00") int r24,

                @Part("22:30") int r52,
                @Part("23:00") int r53,
                @Part("23:30") int r54
        );

        @GET(GET_AVAILABILITYS)
        Call<ResponseBody> getAvailibility(
                @Path("Id") int Id
        );

        @Multipart
        @POST(ORDER_SEND_OTP)
        Call<ResponseBody> sendOrderOTPtoCustomer(
                @Part("OrderId") RequestBody OrderId,
                @Part("PhotographerId") RequestBody PhotographerId,
                @Part("CustomerId") RequestBody CustomerId
        );

        @Multipart
        @POST(ORDER_RESEND_OTP)
        Call<ResponseBody> reSendOrderOTPtoCustomer(
                @Part("OrderId") RequestBody OrderId,
                @Part("PhotographerId") RequestBody PhotographerId,
                @Part("CustomerId") RequestBody CustomerId
        );

        @Multipart
        @POST(VERIFY_SEND_OTP)
        Call<ResponseBody> verifyOrderOTP(
                @Part("OrderId") RequestBody OrderId,
                @Part("PhotographerId") RequestBody PhotographerId,
                @Part("CustomerId") RequestBody CustomerId,
                @Part("OTP") RequestBody OTP
        );

        @Multipart
        @POST(COMPLATE_ORDER)
        Call<ResponseBody> complateOrderByOrderId(
                @Part("OrderId") RequestBody OrderId
        );

        @Multipart
        @POST(TRACKINGDATA)
        Call<ResponseBody> insertPhotographerTrackingData(
                @Part("userId") RequestBody userId,
                @Part("deviceId") RequestBody deviceId,
                @Part("lat") RequestBody lat,
                @Part("lng") RequestBody lng
        );

        @GET(GET_HELP)
        Call<ResponseBody> get247SupportSettings();

        @Multipart
        @POST(GENERAL_ISSUE)
        Call<ResponseBody> addGeneralQuery(
                @Part("Name") RequestBody Name,
                @Part("Email") RequestBody Email,
                @Part("Message") RequestBody Message,
                @Part MultipartBody.Part ProfileImage
        );

        @Multipart
        @POST(CHECK_LIST)
        Call<ResponseBody> checklist(
                @Part("PhotographerId") int PhotographerId,
                @Part("OrderId") int OrderId,
                @Part("Checklist") RequestBody Checklist
        );

        @Multipart
        @POST(EXTEND_STATUS)
        Call<ResponseBody> extendOrderRequestChangeStatus(
                @Part("ExtId") RequestBody ExtId,
                @Part("ExtOrderId") RequestBody ExtOrderId,
                @Part("ExtPhotographerId") RequestBody ExtPhotographerId,
                @Part("ExtCustomerId") RequestBody ExtCustomerId,
                @Part("ExtStatus") RequestBody ExtStatus
        );

        @Multipart
        @POST(SEND_MSG)
        Call<ResponseBody> sendMessages(
                @Part("Sender") int sender,
                @Part("Receiver") int reciver,
                @Part("Message") RequestBody Message,
                @Part("uType") RequestBody uType,
                @Part("isLocation") RequestBody isLocation,
                @Part("orderId") RequestBody orderId
        );

        @GET(RECIVE_MSG)
        Call<ResponseBody> getMessages(
                @Path("sender") int sender,
                @Path("reciver") int reciver,
                @Path("orderId") int orderId
        );

        @GET(GET_ORDER_BY_ID)
        Call<ResponseBody> getPhotographerOrderByOrderId(
                @Path("pid") int pid,
                @Path("oid") int oid
        );

        @GET(NOTIFICATION)
        Call<ResponseBody> getCustomNotificationPhotographer(
                @Path("PhotographerId") int PhotographerId
        );

        @GET(DELETE_NOTIFICATION)
        Call<ResponseBody> deleteCustomNotificationPhotographer(
                @Path("Id") int Id
        );

        @GET(GETAPPVERSION)
        Call<ResponseBody> getAppVersion(
                @Path("id") int Id
        );
        @Multipart
        @POST(EXPORT_ORDER)
        Call<ResponseBody> exportOrders(
                @Part("fromDate") String fromDate,
                @Part("toDate") String toDate,
                @Part("photographerId") int photographerId
        );
    }
}
