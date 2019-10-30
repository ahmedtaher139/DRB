package wakeb.tech.drb.data.Retrofit;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiServices {


    public String BaseURL = "http://3.17.76.229/api/";

    public String IMAGE_USERS_URL = "http://3.17.76.229/api/";


    @POST("publisher/get-notifications")
    @FormUrlEncoded
    Observable<ApiResponse> get_notifications(@FieldMap Map<String, String> qStringMap);


    @POST("publisher/send-emailReset")
    @FormUrlEncoded
    Observable<ApiResponse> send_emailReset(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/get")
    @FormUrlEncoded
    Observable<ApiResponse> get_user(@FieldMap Map<String, String> qStringMap);


    @POST("publisher/log-action")
    @FormUrlEncoded
    Observable<ApiResponse> log_action(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/get-logs")
    @FormUrlEncoded
    Observable<ApiResponse> get_logs(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/check-tempPassword")
    @FormUrlEncoded
    Observable<ApiResponse> check_tempPassword(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/change-password")
    @FormUrlEncoded
    Observable<ApiResponse> change_password(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/reset-password")
    @FormUrlEncoded
    Observable<ApiResponse> reset_password(@FieldMap Map<String, String> qStringMap);


    @POST("publisher/login")
    @FormUrlEncoded
    Observable<ApiResponse> login_user(@FieldMap Map<String, String> qStringMap);


    @POST("publisher/sign-in")
    @FormUrlEncoded
    Observable<ApiResponse> user_register(@FieldMap Map<String, String> qStringMap);


    @POST("publisher/get")
    @FormUrlEncoded
    Observable<ApiResponse> getUser(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/follows-list")
    @FormUrlEncoded
    Observable<ApiResponse> follows_list(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/followers-list")
    @FormUrlEncoded
    Observable<ApiResponse> followers_list(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/block-list")
    @FormUrlEncoded
    Observable<ApiResponse> block_list(@FieldMap Map<String, String> qStringMap);


    @POST("trip/get-profile")
    @FormUrlEncoded
    Observable<ApiResponse> get_profile(@FieldMap Map<String, String> qStringMap);


    @POST("info/get-storeTypes")
    @FormUrlEncoded
    Observable<ApiResponse> get_storeTypes(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/update-profile")
    @FormUrlEncoded
    Observable<ApiResponse> update_profile(@FieldMap Map<String, String> qStringMap);

    @POST("info/get-settings")
    Observable<ApiResponse> get_settings();

    @POST("setting/contact")
    @FormUrlEncoded
    Observable<ApiResponse> contactUs(@FieldMap Map<String, String> qStringMap);

    @POST("info/get-riskTypes")
    Observable<ApiResponse> get_riskTypes();

    @POST("store/sign-in")
    @FormUrlEncoded
    Observable<ApiResponse> store_register(@FieldMap Map<String, String> qStringMap);

    @POST("store/get")
    @FormUrlEncoded
    Observable<ApiResponse> getStore(@FieldMap Map<String, String> qStringMap);

    @POST("store/login")
    @FormUrlEncoded
    Observable<ApiResponse> login_store(@FieldMap Map<String, String> qStringMap);

    @POST("trip/start-trip")
    @FormUrlEncoded
    Observable<ApiResponse> start_trip(@FieldMap Map<String, String> qStringMap);


    @POST("store-places/add-store-place")
    @FormUrlEncoded
    Observable<ApiResponse> add_store_place(@FieldMap Map<String, String> qStringMap);

    @POST("store-places/delete-store-place")
    @FormUrlEncoded
    Observable<ApiResponse> delete_store_place(@FieldMap Map<String, String> qStringMap);

    @POST("store-places/index")
    @FormUrlEncoded
    Observable<ApiResponse> get_places(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/action-follow")
    @FormUrlEncoded
    Observable<ApiResponse> follow_action(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/action-block")
    @FormUrlEncoded
    Observable<ApiResponse> action_block(@FieldMap Map<String, String> qStringMap);

    @POST("publisher/random-publisher")
    @FormUrlEncoded
    Observable<ApiResponse> random_publisher(@FieldMap Map<String, String> qStringMap);

    @POST("trip/fav-action")
    @FormUrlEncoded
    Observable<ApiResponse> fav_action(@FieldMap Map<String, String> qStringMap);

    @POST("trip/delete-trip")
    @FormUrlEncoded
    Observable<ApiResponse> delete_trip(@FieldMap Map<String, String> qStringMap);

    @POST("trip/change-statusPublisher")
    @FormUrlEncoded
    Observable<ApiResponse> change_statusPublisher(@FieldMap Map<String, String> qStringMap);

    @POST("trip/change-privacy")
    @FormUrlEncoded
    Observable<ApiResponse> change_privacy(@FieldMap Map<String, String> qStringMap);

    @POST("risk-comment/add-comment")
    @FormUrlEncoded
    Observable<ApiResponse> add_comment(@FieldMap Map<String, String> qStringMap);


    @POST("trip/share-trip")
    @FormUrlEncoded
    Observable<ApiResponse> share_action(@FieldMap Map<String, String> qStringMap);


    @POST("trip/get-comments")
    @FormUrlEncoded
    Observable<ApiResponse> get_comments(@FieldMap Map<String, String> qStringMap);

    @POST("trip/update-comment")
    @FormUrlEncoded
    Observable<ApiResponse> update_comment(@FieldMap Map<String, String> qStringMap);

    @POST("trip/delete-comment")
    @FormUrlEncoded
    Observable<ApiResponse> delete_comment(@FieldMap Map<String, String> qStringMap);


    @POST("trip/comment-publishing")
    @FormUrlEncoded
    Observable<ApiResponse> save_comment(@FieldMap Map<String, String> qStringMap);


    @POST("trip/get-currentTrip")
    @FormUrlEncoded
    Observable<ApiResponse> get_currentTrip(@FieldMap Map<String, String> qStringMap);

    @POST("trip/get-public")
    @FormUrlEncoded
    Observable<ApiResponse> get_public(@FieldMap Map<String, String> qStringMap);

    @POST("trip/list-trips")
    @FormUrlEncoded
    Observable<ApiResponse> list_trips(@FieldMap Map<String, String> qStringMap);

    @POST("trip/list-favourit")
    @FormUrlEncoded
    Observable<ApiResponse> list_favourit(@FieldMap Map<String, String> qStringMap);

    @POST("trip/share-trip")
    @FormUrlEncoded
    Observable<ApiResponse> share_trip(@FieldMap Map<String, String> qStringMap);

    @POST("trip/get-follower")
    @FormUrlEncoded
    Observable<ApiResponse> get_followers(@FieldMap Map<String, String> qStringMap);

    @POST("trip/like-action")
    @FormUrlEncoded
    Observable<ApiResponse> like_action(@FieldMap Map<String, String> qStringMap);


    @POST("trip/end-trip")
    @FormUrlEncoded
    Observable<ApiResponse> end_trip(@FieldMap Map<String, String> qStringMap);

    @POST("trip/get-publishing")
    @FormUrlEncoded
    Observable<ApiResponse> get_publishing(@FieldMap Map<String, String> qStringMap);

    @POST("trip/get-resource")
    @FormUrlEncoded
    Observable<ApiResponse> get_resource(@FieldMap Map<String, String> qStringMap);


    @POST("risks/add-risks")
    @FormUrlEncoded
    Observable<ApiResponse> add_risk(@FieldMap Map<String, String> qStringMap);

    @POST("risks/show-risks")
    @FormUrlEncoded
    Observable<ApiResponse> get_risks(@FieldMap Map<String, String> qStringMap);

    @POST("suggest/list-suggest")
    @FormUrlEncoded
    Observable<ApiResponse> list_suggest(@FieldMap Map<String, String> qStringMap);


    @POST("store-places/get-three")
    Observable<ApiResponse> get_all();


    @Multipart
    @POST("trip/upload-resource")
    Observable<ApiResponse> upload_resource(@Part("trip_id") RequestBody trip_id,
                                            @Part("address") RequestBody address,
                                            @Part("type") RequestBody type,
                                            @Part("lat") RequestBody lat,
                                            @Part("lng") RequestBody lng,
                                            @Part MultipartBody.Part VIDEO);

    @POST("risks/add-risks-without-image")
    @FormUrlEncoded
    Observable<ApiResponse> add_risks_without_image(@FieldMap Map<String, String> qStringMap);

    @Multipart
    @POST("risks/add-risks")
    Observable<ApiResponse> add_risks(@Part("lat") RequestBody lat,
                                      @Part("lng") RequestBody lng,
                                      @Part("address") RequestBody address,
                                      @Part("desc") RequestBody desc,
                                      @Part("risk_type_id") RequestBody risk_type_id,
                                      @Part("publisher_id") RequestBody publisher_id,
                                      @Part MultipartBody.Part IMAGE);

    @POST("suggest/add-suggest-without-image")
    @FormUrlEncoded
    Observable<ApiResponse> suggest_without_image(@FieldMap Map<String, String> qStringMap);

    @Multipart
    @POST("suggest/add-suggest")
    Observable<ApiResponse> add_suggest(@Part("lat") RequestBody lat,
                                        @Part("lng") RequestBody lng,
                                        @Part("address") RequestBody address,
                                        @Part("desc") RequestBody desc,
                                        @Part("user_id") RequestBody risk_type_id,
                                        @Part MultipartBody.Part IMAGE);


    @Multipart
    @POST("publisher/update-profile")
    Observable<ApiResponse> update_image(@Part("user_id") RequestBody risk_type_id,
                                         @Part("username") RequestBody username,
                                         @Part("display_name") RequestBody display_name,
                                         @Part("mobile") RequestBody mobile,
                                         @Part("email") RequestBody email,
                                         @Part("bio") RequestBody bio,
                                         @Part("city") RequestBody city,
                                         @Part MultipartBody.Part IMAGE);

}
