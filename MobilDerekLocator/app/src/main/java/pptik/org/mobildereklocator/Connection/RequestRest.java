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


    public void registerUser(String name, String email , String user,
                             String pass,String role,String idunit){
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("user", user);
        params.put("pass", pass);
        params.put("role", role);
        params.put("id_unit", idunit);



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
}
