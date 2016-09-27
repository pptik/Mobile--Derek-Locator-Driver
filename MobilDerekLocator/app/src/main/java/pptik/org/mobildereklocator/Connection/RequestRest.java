package pptik.org.mobildereklocator.Connection;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import pptik.org.mobildereklocator.Setup.ApplicationConstants;

public class RequestRest extends ConnectionHandler {

    final int DEFAULT_TIMEOUT = 400 * 1000;

    protected static AsyncHttpClient mClient = new AsyncHttpClient();

    public RequestRest(Context context, IConnectionResponseHandler handler) {
        this.mContext = context;
        this.responseHandler = handler;
    }

    @Override
    public String getAbsoluteUrl(String relativeUrl) {
        return ApplicationConstants.HTTP_URL + relativeUrl;
    }


    public void testConnection(){
        RequestParams params = new RequestParams();
       mClient.addHeader("x-ami-cc", "MOBILE");
        System.setProperty("http.keepAlive", "false");
        get("network.json", params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                 responseHandler.onSuccessJSONObject(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                responseHandler.onFailure(responseBody);
            }

            @Override
            public void onFinish() {
                super.onFinish();
             }

        }, mClient);
    }


    public void registerUser(String name, String email ,String nomor_telepon, String user,
                             String pass,String role){
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("nomor_telepon", nomor_telepon);
        params.put("user", user);
        params.put("pass", pass);
        params.put("role", role);



        post("signup", params, new JsonHttpResponseHandler() {


            @Override
            public void onStart() {
                super.onStart();
                //   dialog = ProgressDialog.show(mContext, "Connecting", "Check Connection", true);
            }

            @Override
            public void onSuccess(JSONObject status) {
                super.onSuccess(status);
                responseHandler.onSuccessJSONObject(status.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                responseHandler.onFailure(e.toString());//e.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                 //   dialog.dismiss();
            }

        }, mClient);
    }
    public void login(String username, String password ){
        RequestParams params = new RequestParams();
        params.put("user", username);
        params.put("pass", password);



        post("login", params, new JsonHttpResponseHandler() {


            @Override
            public void onStart() {
                super.onStart();
                //   dialog = ProgressDialog.show(mContext, "Connecting", "Check Connection", true);
            }

            @Override
            public void onSuccess(JSONObject status) {
                super.onSuccess(status);
                responseHandler.onSuccessJSONObject(status.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                responseHandler.onFailure(e.toString());//e.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //   dialog.dismiss();
            }

        }, mClient);
    }
    public void panicButtonReport(String username, String location,String latitude,String longitude,String nomor_telepon ){
        RequestParams params = new RequestParams();
        params.put("user", username);
        params.put("location", location);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("nomor_telepon", nomor_telepon);

        post("login", params, new JsonHttpResponseHandler() {


            @Override
            public void onStart() {
                super.onStart();
                //   dialog = ProgressDialog.show(mContext, "Connecting", "Check Connection", true);
            }

            @Override
            public void onSuccess(JSONObject status) {
                super.onSuccess(status);
                responseHandler.onSuccessJSONObject(status.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                responseHandler.onFailure(e.toString());//e.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //   dialog.dismiss();
            }

        }, mClient);
    }
    public void updateLokasiDriver(String username, String location,String latitude,String longitude ){
        RequestParams params = new RequestParams();
        params.put("user", username);
        params.put("location", location);
        params.put("latitude", latitude);
        params.put("longitude", longitude);

        post("updateuser", params, new JsonHttpResponseHandler() {


            @Override
            public void onStart() {
                super.onStart();
                //   dialog = ProgressDialog.show(mContext, "Connecting", "Check Connection", true);
            }

            @Override
            public void onSuccess(JSONObject status) {
                super.onSuccess(status);
                responseHandler.onSuccessJSONObject(status.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                responseHandler.onFailure(e.toString());//e.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //   dialog.dismiss();
            }

        }, mClient);
    }
}
