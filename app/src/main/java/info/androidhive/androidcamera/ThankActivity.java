package info.androidhive.androidcamera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
public class ThankActivity extends AppCompatActivity {
    private ImageView action_thanks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_thank);
        action_thanks = (ImageView) findViewById(R.id.action_thanks);
        Glide.with(this).load(R.drawable.thanks).into(action_thanks);
        action_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThankActivity.this, SplashActivity.class));
            }
        });
    }
}
