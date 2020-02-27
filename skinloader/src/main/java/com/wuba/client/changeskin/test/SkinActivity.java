package com.wuba.client.changeskin.test;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;


import com.wuba.client.changeskin.R;
import com.wuba.client.changeskin.manager.SkinManager;
import com.wuba.client.changeskin.manager.SkinResource;

import java.io.File;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 18:08
 * Description:
 */
public class SkinActivity extends SkinBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skin_activity_main);
        Button button = findViewById(R.id.btn);
        button.setTextColor(SkinResource.getColor(this, R.color.skin_main_textcolor));
        button.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = Environment.getExternalStorageDirectory() + File.separator + "dark.skin";
                SkinManager.getInstance().load(s, "dark_");
            }
        });
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().setDefaultSkin();
            }
        });
    }
}
