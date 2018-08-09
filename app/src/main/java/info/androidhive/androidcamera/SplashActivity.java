package info.androidhive.androidcamera;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import info.androidhive.androidcamera.utils.Constant;
import info.androidhive.androidcamera.utils.Preference;

public class SplashActivity extends AppCompatActivity {
    private Preference mPreference;
    private Button mButton;
    private ImageButton actionSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        init();
        initView();
    }

    private void init() {
        mPreference = Preference.build(this);
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.action_next);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });
        actionSetting = (ImageButton) findViewById(R.id.action_setting);
        actionSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingDialog();
            }
        });
    }

    private void onNext() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    private void openSettingDialog() {
        View view = View.inflate(this, R.layout.dialog_setting, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final EditText editText = (EditText) view.findViewById(R.id.edit_path);
        editText.setText(mPreference.get(Constant.Pref.Setting.IP_OR_HOST_PATH, ""));
        Button cancel = (Button) view.findViewById(R.id.action_cancel);
        Button change = (Button) view.findViewById(R.id.action_change);
        final Dialog dialog = builder.create();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreference.set(Constant.Pref.Setting.IP_OR_HOST_PATH, editText.getText().toString());
                editText.setText(mPreference.get(Constant.Pref.Setting.IP_OR_HOST_PATH, ""));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
