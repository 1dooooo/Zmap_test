package com.example.fei.zmap_test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fei.zmap_test.db.Users;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.litepal.crud.DataSupport;

public class AccountProfile extends AppCompatActivity {
    private static final String TAG = "AccountProfile";
    public Users current_user;
    private LinearLayout top_view;
    private ImageButton user_head_icon_btn;
    private LinearLayout bottom_view;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile_layout);
        getSupportActionBar().hide();

        url=getString(R.string.URl); //服务器接口地址

        user_head_icon_btn = (ImageButton)findViewById(R.id.Account_profile_head_icon_btn);
        top_view = findViewById(R.id.Account_profile_head_icon_choose_view);
        bottom_view = findViewById(R.id.Account_profile_view);
        TextView username_view = findViewById(R.id.Account_profile_nick_name_text);

        current_user = DataSupport.findLast(Users.class);  //从数据库读出当前登陆的用户
        username_view.setText( current_user.getUsername());
        user_head_icon_btn.setImageResource(getHeadIconResourceFromId(current_user.getId_head()));



        addListener(R.id.Account_profile_head_icon_setting);//头像设置界面
        addListener(R.id.Account_profile_logout); //logout
        addListener(R.id.Account_profile_user_name_setting);//设置用户名
        addListener(R.id.Account_profile_head_icon_choose_view);//头像选择界面
        addListenerForChooseButton();
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){

                    case R.id.Account_profile_head_icon_setting:
                        topViewAnimShow();
                        break;
                    case R.id.Account_profile_logout:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AccountProfile.this);
                        dialog.setTitle("退出登录？");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                current_user=DataSupport.findLast(Users.class);
                                DataSupport.deleteAll(Users.class,"User_id > ?","0");
                                finish();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.Account_profile_user_name_setting:
                        Toast.makeText(AccountProfile.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Account_profile_head_icon_choose_view:
                        topViewAnimDisappear();

                        break;
                    case R.id.Account_profile_head_icon_choose_btn_1:

                        break;
                }
            }
        });
    }
    //为每个头像选择图标设置监听器
    public void addListenerForChooseButton(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id_head= Integer.parseInt((String)v.getTag());
                user_head_icon_btn.setImageResource(getHeadIconResourceFromId(id_head));
                sendRequestWithHttpClient(id_head);
                Users users= new Users();
                users.setId_head(id_head);
                users.updateAll("User_id > ?","0");
                topViewAnimDisappear();

            }
        };
        findViewById(R.id.Account_profile_head_icon_choose_btn_1).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_2).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_3).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_4).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_5).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_6).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_7).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_8).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_9).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_10).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_11).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_12).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_13).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_14).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_15).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_16).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_17).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_18).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        if(top_view.getVisibility() == View.GONE) finish();
        else {
            topViewAnimDisappear();
        }
    }

    //头像选择界面动画出现，同时取消底层界面激活状态
    public void topViewAnimShow(){
        Animation pop_up_anim = AnimationUtils.loadAnimation(AccountProfile.this, R.anim.activity_account_profile_head_icon_choose_pop_up);
        top_view.startAnimation(pop_up_anim);
        top_view.setVisibility(View.VISIBLE);
        bottom_view.setActivated(false);
    }

    //头像选择界面动画消失，同时激活底层界面
    public void topViewAnimDisappear(){
        Animation pop_down_anim = AnimationUtils.loadAnimation(AccountProfile.this, R.anim.activity_account_profile_head_icon_pop_down);
        top_view.startAnimation(pop_down_anim);
        top_view.setVisibility(View.GONE);
        bottom_view.setActivated(true);
    }

    //发送远程更换头像数据
    private void sendRequestWithHttpClient(final int id_head){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/?action=modifyheadid&username="+current_user.getUsername()+"&id_head="+id_head);
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串

                        //TODO 数据根据返回值设置需要才更加严谨
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //通过ID获得View
    public static int getHeadIconResourceFromId(int id){
        switch (id){
            case 0:return R.drawable.profile_head;
            case 1:return R.drawable.avatar_1;
            case 2:return R.drawable.avatar_2;
            case 3:return R.drawable.avatar_3;
            case 4:return R.drawable.avatar_4;
            case 5:return R.drawable.avatar_5;
            case 6:return R.drawable.avatar_6;
            case 7:return R.drawable.avatar_7;
            case 8:return R.drawable.avatar_8;
            case 9:return R.drawable.avatar_9;
            case 10:return R.drawable.avatar_10;
            case 11:return R.drawable.avatar_11;
            case 12:return R.drawable.avatar_12;
            case 13:return R.drawable.avatar_13;
            case 14:return R.drawable.avatar_14;
            case 15:return R.drawable.avatar_15;
            case 16:return R.drawable.avatar_16;
            case 17:return R.drawable.avatar_17;
            case 18:return R.drawable.avatar_18;
            default:return 0;
        }
    }
}
