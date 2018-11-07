package com.example.dell.v.User;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.v.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserLogin extends Activity{
    private EditText nameInputET;
    private EditText pwInputET;
    String nameInput;
    String pwInput;
    private Button buttonLogin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_layout);
        nameInputET = findViewById(R.id.username_input);
        pwInputET = findViewById(R.id.password_input);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameInput = nameInputET.getText().toString();
                pwInput = pwInputET.getText().toString();
                LoginRequest(nameInput,pwInput,UserLogin.this);
            }// on click
        });//setOnclickListener
    }//on create


    public static void LoginRequest(final String userName, final String password, final Context context) {
                        //请求地址
                String url = "http://22m603457z.imwork.net:54231/TestApp/LoginServlet";
                String tag = "Login";

                //取得请求队列
                RequestQueue requestQueue = Volley.newRequestQueue(context);

                //防止重复请求，所以先取消tag标识的请求队列
                requestQueue.cancelAll(tag);

                //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
                final StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(!TextUtils.isEmpty(response)) {
                                        JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                                        String result = jsonObject.getString("Result");


                                    if (result.equals("success")) {
                                        //login success
                                        Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT).show();
                                    }
                                    }
                                } catch (JSONException e) {
                                    //提示异常
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", e.getMessage(), e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //响应错误
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", error.getMessage(), error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("AccountNumber", userName);
                        params.put("Password", password);
                        return params;
                    }
                };

                //设置Tag标签
                request.setTag(tag);

                //将请求添加到队列中
                requestQueue.add(request);
    }

}
