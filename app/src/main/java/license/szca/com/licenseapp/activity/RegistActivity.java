package license.szca.com.licenseapp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import license.szca.com.licenseapp.R;
import license.szca.com.licenseapp.presenter.RegistPresenter;
import license.szca.com.licenseapp.service.CheckLicenseService;
import license.szca.com.licenseapp.view.IRegistView;

/**
 * description 注册页面
 * Created by JD
 * on 2017/9/12.
 */
public class RegistActivity extends AppCompatActivity implements View.OnClickListener, IRegistView {

    private EditText et_input_name;
    private Button bt_gen;
    private EditText et_key;
    private Button bt_check;
    private RegistPresenter registPresenter;
    private final String TAG = "RegistActivity";
    private Messenger mSendDataMessenger;
    private Messenger mGetReplyMessenger;
    private final int MSG_CLIENT_DATA = 1;


    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case Constant.MSG_FROM_SERVICE:
//                    Log.i(TAG, "receive msg from Service:" + msg.getData().getString("reply"));
//                    break;
//                default:
//                    super.handleMessage(msg);
//            }

        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSendDataMessenger = new Messenger(service);
            try {

                Message message = new Message();
                message.what = MSG_CLIENT_DATA;
                Bundle bundle = new Bundle();
                bundle.putString("submitData" , registPresenter.submitData(et_key.getText().toString().trim()));
                message.setData(bundle);
                //发送数据给服务端
                mSendDataMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initView();
    }

    private void initView() {
        et_input_name = (EditText) findViewById(R.id.et_input_name);
        bt_gen = (Button) findViewById(R.id.bt_gen);
        et_key = (EditText) findViewById(R.id.et_key);
        bt_check = (Button) findViewById(R.id.bt_check);

        bt_gen.setOnClickListener(this);
        bt_check.setOnClickListener(this);


        registPresenter = new RegistPresenter(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_gen:
                registPresenter.genLicenseKey(et_input_name.getText().toString().trim());
                break;
            case R.id.bt_check:
                sendMessageToService();

//                registPresenter.checkLicenseKey(et_key.getText().toString().trim());
                break;
        }
    }

    /**
     * 发送消息给服务端
     */
    private void sendMessageToService() {
        Intent intent = new Intent(this , CheckLicenseService.class);
        bindService(intent , mConnection , Context.BIND_AUTO_CREATE);
    }


    @Override
    public void genLicenseKeyFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void genLicenseKeySuccess(String key) {
et_key.setText(key);
    }

    @Override
    public void checkLicenseKeySuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this , RegistSuccessActivity.class));
        finish();
    }

    @Override
    public void checkLicenseKeyFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

