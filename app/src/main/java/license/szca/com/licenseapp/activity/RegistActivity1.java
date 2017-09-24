package license.szca.com.licenseapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import license.szca.com.licenseapp.R;
import license.szca.com.licenseapp.presenter.RegistPresenter;
import license.szca.com.licenseapp.presenter.RegistPresenter1;
import license.szca.com.licenseapp.view.IRegistView;
import license.szca.com.licenseapp.view.IRegistView1;

/**
 * description :
 * author : JDNew
 * on : 2017/9/24.
 */

public class RegistActivity1 extends AppCompatActivity implements View.OnClickListener, IRegistView1 {

    private EditText et_input_name;
    private Button bt_gen;
    private EditText et_key;
    private Button bt_check;
    private RegistPresenter1 registPresenter;
    private final String TAG = "RegistActivity1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registPresenter = new RegistPresenter1(this, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        registPresenter.onStop();
    }

    private void initView() {
        et_input_name = (EditText) findViewById(R.id.et_input_name);
        bt_gen = (Button) findViewById(R.id.bt_gen);
        et_key = (EditText) findViewById(R.id.et_key);
        bt_check = (Button) findViewById(R.id.bt_check);

        bt_gen.setOnClickListener(this);
        bt_check.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_gen:
                registPresenter.genLicenseKey(et_input_name.getText().toString().trim());
                break;
            case R.id.bt_check:
                registPresenter.submitData(et_key.getText().toString().trim());
                break;
        }
    }


    @Override
    public void getLicenseKeyFailed(String msg) {
        dismissProgressDialog();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getLicenseKeySuccess(String key) {
        dismissProgressDialog();
        et_key.setText(key);
    }

    @Override
    public void checkLicenseKeySuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, RegistSuccessActivity.class));
        finish();
    }

    @Override
    public void checkLicenseKeyFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showLoading(String msg) {
        showProgressDialog(msg);
    }


    private ProgressDialog progressDialog;

    /**
     * 显示加载窗口
     * @param msg
     */
    private void showProgressDialog(String msg) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    /**
     * 隐藏加载窗口
     */
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
