package com.moeiny.reza.commbox_test;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ViewGroup testView;
    private WindowManager windowManager;
    private final int width = 200;
    private final int height = 201;
    private final int REQUEST_CODE = 1;
    private TextView link;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                int xOffset = view.getWidth() / 2;
                int yOffset = view.getHeight() / 2;
                int x = (int) event.getRawX() - xOffset;
                int y = (int) event.getRawY() - yOffset;

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        width,
                        height,
                        x, y,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.TOP | Gravity.LEFT;
                windowManager.updateViewLayout(testView, params);
                return true;
            }
            return false;
        }
    };

    private View.OnClickListener onClickListenerLink = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String url = (String) link.getText();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            browserIntent.setPackage("com.android.chrome");
            try {
                MainActivity.this.startActivity(browserIntent);
            } catch (Exception e) {
                browserIntent.setPackage("com.amazon.cloud9");
                MainActivity.this.startActivity(browserIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        link = findViewById(R.id.link);
        link.setOnClickListener(onClickListenerLink);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        testView = (ViewGroup) inflator.inflate(R.layout.test, null);
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                width,
                height,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) getSystemService(MainActivity.this.WINDOW_SERVICE);

        testView.setOnTouchListener(onTouchListener);
        windowManager.addView(testView, mParams);
    }
}