package info.androidhive.androidcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.alhazmy13.imagefilter.ImageFilter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilterActivity extends AppCompatActivity {
    private final static String TAG = FilterActivity.class.getSimpleName();
    private String file;
    private ImageView imageView;
    private Bitmap bitmap;
    private LinearLayout view_normal, view_gray, view_relief, view_neon, view_invert, view_sketch;
    private CircleImageView image_normal, image_gray, image_relief, image_neon, image_invert, image_sketch;
    private List<LinearLayout> mList = new ArrayList();
    private List<CircleImageView> mCircleImageViewList = new ArrayList();
    private String FILTER = "NORMAL";
    private ImageButton actionBack;
    private Button actionLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent() != null) {
            if (getIntent().hasExtra("URI")) {
                file = getIntent().getStringExtra("URI");
            }
        }
        Log.d("URI", file);
        init();
        initView();
        initialize();
    }

    private void init() {
        bitmap = CameraUtils.optimizeBitmap(MainActivity.BITMAP_SAMPLE_SIZE, file);
    }

    private void initView() {
        actionBack = (ImageButton) findViewById(R.id.action_back);
        actionLaunch = (Button) findViewById(R.id.action_launch);
        imageView = (ImageView) findViewById(R.id.image_view);
        view_normal = (LinearLayout) findViewById(R.id.view_normal);
        view_gray = (LinearLayout) findViewById(R.id.view_gray);
        view_relief = (LinearLayout) findViewById(R.id.view_relief);
        view_neon = (LinearLayout) findViewById(R.id.view_neon);
        view_invert = (LinearLayout) findViewById(R.id.view_invert);
        view_sketch = (LinearLayout) findViewById(R.id.view_sketch);

        image_normal = (CircleImageView) findViewById(R.id.image_normal);
        image_gray = (CircleImageView) findViewById(R.id.image_gray);
        image_relief = (CircleImageView) findViewById(R.id.image_relief);
        image_neon = (CircleImageView) findViewById(R.id.image_neon);
        image_invert = (CircleImageView) findViewById(R.id.image_invert);
        image_sketch = (CircleImageView) findViewById(R.id.image_sketch);

        mList.add(view_normal);
        mList.add(view_gray);
        mList.add(view_relief);
        mList.add(view_neon);
        mList.add(view_invert);
        mList.add(view_sketch);
        mCircleImageViewList.add(image_normal);
        mCircleImageViewList.add(image_gray);
        mCircleImageViewList.add(image_relief);
        mCircleImageViewList.add(image_neon);
        mCircleImageViewList.add(image_invert);
        mCircleImageViewList.add(image_sketch);

        view_normal.setOnClickListener(onSelectListener);
        view_gray.setOnClickListener(onSelectListener);
        view_relief.setOnClickListener(onSelectListener);
        view_neon.setOnClickListener(onSelectListener);
        view_invert.setOnClickListener(onSelectListener);
        view_sketch.setOnClickListener(onSelectListener);

        actionBack.setOnClickListener(onBackListener);
        actionLaunch.setOnClickListener(onLaunchListener);
    }

    private void initialize() {
        imageView.setImageBitmap(bitmap);
        image_normal.setImageBitmap(updateFilter(bitmap, image_normal.getTag().toString()));
        image_gray.setImageBitmap(updateFilter(bitmap, image_gray.getTag().toString()));
        image_relief.setImageBitmap(updateFilter(bitmap, image_relief.getTag().toString()));
        image_neon.setImageBitmap(updateFilter(bitmap, image_neon.getTag().toString()));
        image_invert.setImageBitmap(updateFilter(bitmap, image_invert.getTag().toString()));
        image_sketch.setImageBitmap(updateFilter(bitmap, image_sketch.getTag().toString()));
    }

    private View.OnClickListener onBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NavUtils.navigateUpFromSameTask(FilterActivity.this);
        }
    };

    private View.OnClickListener onLaunchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveFilterImage();
        }
    };

    private View.OnClickListener onSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onSelect(v.getTag());
        }
    };

    private void onSelect(Object tag) {
        for (CircleImageView view : mCircleImageViewList) {
            view.setBorderColor(view.getTag().equals(tag) ? ContextCompat.getColor(this, R.color.colorActionCenter) : ContextCompat.getColor(this, R.color.colorBlackTransparent_0));
            if (view.getTag().equals(tag)) {
                FILTER = tag.toString();
                imageView.setImageBitmap(updateFilter(bitmap, tag.toString()));
            }
        }
    }

    private void onSelectLayout(Object tag) {
        for (LinearLayout layout : mList) {
            layout.setBackgroundColor(layout.getTag().equals(tag) ? ContextCompat.getColor(this, R.color.colorBlackTransparent_75) : ContextCompat.getColor(this, R.color.colorBlackTransparent_50));
            if (layout.getTag().equals(tag)) {
                FILTER = tag.toString();
                imageView.setImageBitmap(updateFilter(bitmap, tag.toString()));
            }
        }
    }

    private Bitmap updateFilter(Bitmap bitmap, String filter) {
        switch (filter) {
            case "NORMAL":
                return bitmap;
            case "GRAY":
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.GRAY);
            case "RELIEF":
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.RELIEF);
            case "NEON":
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.NEON, 200, 50, 100);
            case "INVERT":
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.INVERT);
            case "SKETCH":
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SKETCH);
            default:
                return bitmap;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_done:
                saveFilterImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);*/
        return true;
    }

    private void saveFilterImage() {
        HashMap<String, String> map = StorageHelper.saveImageToExternalStorage1(this, updateFilter(bitmap, FILTER));
        if (map.containsKey("RESULT") && map.get("RESULT").equalsIgnoreCase("TRUE")) {
            if (map.containsKey("PATH") && !map.get("PATH").equalsIgnoreCase("")) {
                Log.d("PATH", map.get("PATH"));
                Intent intent = new Intent(this, UploadService.class);
                intent.setAction("UPLOAD");
                intent.putExtra("PATH", map.get("PATH"));
                getApplicationContext().startService(intent);
            }
            getApplicationContext().startActivity(new Intent(this, ThankActivity.class));
            finish();
        }
    }

    private Bitmap compressBitmap(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return newBitmap;
    }
}
