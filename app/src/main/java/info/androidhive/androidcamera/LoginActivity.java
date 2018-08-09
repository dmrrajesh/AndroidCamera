package info.androidhive.androidcamera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import info.androidhive.androidcamera.utils.Constant;
import info.androidhive.androidcamera.utils.Preference;
import info.androidhive.androidcamera.utils.Validation;

public class LoginActivity extends AppCompatActivity {
    private Button actionNext;
    private EditText editEmail, editMobileNumber, editFullName;
    private Preference mPreference;
    private Validation mValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        init();
        initView();
    }

    private void init() {
        mPreference = Preference.build(this);
        mValidation = Validation.build(this);
        if (mPreference.isLogin()) {
            doLogin();
        }
    }
    
    private void initView() {
        actionNext = (Button) findViewById(R.id.action_next);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editMobileNumber = (EditText) findViewById(R.id.edit_mobile_number);
        editFullName = (EditText) findViewById(R.id.edit_full_name);
        actionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFullName.setError(null);
                editMobileNumber.setError(null);
                editEmail.setError(null);
                boolean isValid = true;
                if (!mValidation.isPersonName(editFullName.getText().toString())) {
                    editFullName.setError("Invalid");
                    isValid = false;
                }
                if (!mValidation.isMobile(editMobileNumber.getText().toString())) {
                    editMobileNumber.setError("Invalid");
                    isValid = false;
                }
                if (!mValidation.isEmail(editEmail.getText().toString())) {
                    editEmail.setError("Invalid");
                    isValid = false;
                }
                if (isValid) {
                    mPreference.set(Constant.Pref.User.FULL_NAME, editFullName.getText().toString());
                    mPreference.set(Constant.Pref.User.EMAIL, editEmail.getText().toString());
                    mPreference.set(Constant.Pref.User.MOBILE, editMobileNumber.getText().toString());
                    mPreference.set(Constant.Pref.User.LOGIN_STATUS, true);
                    doLogin();
                }
            }
        });
    }

    private void doLogin() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
