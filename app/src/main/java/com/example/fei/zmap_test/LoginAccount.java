
         package com.example.fei.zmap_test;

         import android.content.Intent;
         import android.os.Bundle;
         import android.os.Handler;
         import android.os.Message;
         import android.support.v7.app.AppCompatActivity;
         import android.util.Log;
         import android.view.View;
         import android.widget.EditText;
         import android.widget.Toast;

         import com.example.fei.zmap_test.db.users;
         import com.google.gson.Gson;

         import org.apache.http.HttpEntity;
         import org.apache.http.HttpResponse;
         import org.apache.http.client.HttpClient;
         import org.apache.http.client.methods.HttpGet;
         import org.apache.http.impl.client.DefaultHttpClient;
         import org.apache.http.util.EntityUtils;
         import org.apache.http.util.TextUtils;
         import org.json.JSONException;
         import org.json.JSONObject;

public class LoginAccount extends AppCompatActivity {
    private static final String TAG = "LoginAccount";
    private String url="http://www.idooooo.tk";//�������ӿڵ�ַ
    private EditText username;
    private EditText password;//�û���������
    private String username_text;
    private String password_text;
    public static final int SHOW_RESPONSE = 0;
    public users resp_user;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String Response=msg.obj.toString();
                    if(!TextUtils.isEmpty(Response)){
                        resp_user=new users();
                        try {
                            JSONObject userObject = new JSONObject(Response);
                            resp_user.setId(userObject.getInt("id"));
                            resp_user.setUsername(userObject.getString("username"));
                            resp_user.setId_head(userObject.getInt("id_head"));
                            resp_user.setSearchHistory(userObject.getString("searchHistory"));
                            resp_user.setStatusCode(userObject.getInt("statusCode"));
                            if(resp_user.getId()!=0){
                                Toast.makeText(LoginAccount.this,"��½�ɹ�",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginAccount.this,Profile.class);
                                intent.putExtra("resp_user",new Gson().toJson(resp_user));
                                setResult(RESULT_OK,intent);
                                finish();
                            } else {
                                Toast.makeText(LoginAccount.this,"�˺��������",Toast.LENGTH_SHORT).show();
                                password.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Log.e(TAG,"!=null");
        intent.putExtra("nothing",0);
        setResult(RESULT_CANCELED);
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account_layout);
        getSupportActionBar().hide();

        username=(EditText)findViewById(R.id.login_account_username);
        password=(EditText)findViewById(R.id.login_account_password);

        addListener(R.id.go_login_phone_text);
        addListener(R.id.find_password);                    //��ťĿǰֻ�з��ع���
        addListener(R.id.login_account_button);
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.go_login_phone_text:
                        Intent intent = new Intent(LoginAccount.this, LoginPhone.class);
                        startActivity(intent);
                        finish();break;
                    case R.id.find_password:
                        Toast.makeText(LoginAccount.this, "����", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.login_account_button:
                        sendRequestWithHttpClient();
                        break;
                }
            }
        });
    }

    private void sendRequestWithHttpClient(){
        username_text = username.getText().toString().trim();
        password_text = password.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //����HttpClient����
                HttpGet httpGet = new HttpGet(url+"/?action=login&username="+username_text+"&password="+password_text);
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//��������ִ�����󣬻�ȡ��������������Ӧ����
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//��entity���е�����ת��Ϊ�ַ���
                        Message message = new Message();//�����߳��н�Message���󷢳�ȥ
                        message.what = SHOW_RESPONSE;
                        message.obj =response;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}