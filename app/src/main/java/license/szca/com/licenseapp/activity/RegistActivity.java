package license.szca.com.licenseapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import license.szca.com.licenseapp.R;
import license.szca.com.licenseapp.presenter.RegistPresenter;
import license.szca.com.licenseapp.view.IRegistView;

/**
 * description
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
                registPresenter.checkLicenseKey(et_key.getText().toString().trim());
                break;
        }
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

    }

    @Override
    public void checkLicenseKeyFailed(String msg) {

    }
}

