package com.yidan.genieeffect;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dataseed.genieeffectlibrary.AnimSurface;
import com.dataseed.genieeffectlibrary.AnimSurfaceUtil;

public class GenieSampleActivity extends Activity {

    private Button button;
    private View image;

    private AnimSurface mAnimSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_genie_sample_activity);
        initView();
    }

    private void initView() {
        button = (Button) findViewById(R.id.button);
        image = findViewById(R.id.image);


        button.setOnClickListener(new View.OnClickListener() {
            boolean mReverse = false;

            @Override
            public void onClick(View v) {
                if (mAnimSurface == null) {
                    mAnimSurface = new AnimSurface(GenieSampleActivity.this);
                    mAnimSurface.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                }
                AnimSurfaceUtil.startAnimation(GenieSampleActivity.this, mAnimSurface, image, mReverse);
                mReverse = !mReverse;

            }
        });
    }
}
