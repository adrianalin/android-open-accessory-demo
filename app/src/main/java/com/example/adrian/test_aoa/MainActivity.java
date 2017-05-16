package com.example.adrian.test_aoa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.example.adrian.test_aoa.AccessoryEngine.IEngineCallback;

/**
 * @author mdc
 *
 */
public class MainActivity extends Activity {

    private SeekBar mBrightnessBar;
    private AccessoryEngine mEngine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        onNewIntent(getIntent());
        setContentView(R.layout.activity_main);
        mBrightnessBar = (SeekBar) findViewById(R.id.sbBrightness);
        mBrightnessBar.setOnSeekBarChangeListener(mSeekBarListener);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        L.d("handling intent action: " + intent.getAction());
        if (mEngine == null) {
            mEngine = new AccessoryEngine(getApplicationContext(), mCallback);
        }
        mEngine.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        mEngine.onDestroy();
        mEngine = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private final OnSeekBarChangeListener mSeekBarListener = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            L.d("value is %d", progress);
            if (fromUser && mEngine != null) {
                mEngine.write(new byte[] { (byte) progress });
            }
        }
    };

    private final IEngineCallback mCallback = new IEngineCallback() {
        @Override
        public void onDeviceDisconnected() {
            L.d("device physically disconnected");
        }

        @Override
        public void onConnectionEstablished() {
            L.d("device connected! ready to go!");
        }

        @Override
        public void onConnectionClosed() {
            L.d("connection closed");
        }

        @Override
        public void onDataRecieved(byte[] data, int num) {
            L.d("received %d bytes", num);
        }
    };

}
