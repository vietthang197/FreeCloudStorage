package com.tagroup.thangducanh.freecloudstorage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // code activity result
    private static final int FILE_SELECT_CODE = 11;
    private static final int READ_EXTERNAL_REQUEST = 12;

    // code item view
    private Button btnUploadFile, btnChooseFile;
    private FloatingActionButton fabUploadFile;

    // code data type
    private List<Uri> uriList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvents();
        initData();
        requirePermission();
    }

    private void requirePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (PackageManager.PERMISSION_GRANTED != result) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_REQUEST);
            }
        }
    }

    private void initData() {
        uriList = new ArrayList<>();
    }

    private void initEvents() {
        btnChooseFile.setOnClickListener(this);
        fabUploadFile.setOnClickListener(this);
    }

    private void initView() {
        btnUploadFile = findViewById(R.id.btn_upload_file);
        btnChooseFile = findViewById(R.id.btn_choose_file);
        fabUploadFile = findViewById(R.id.btn_bottom_sheet_dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_file:
                showFileChooser();
                break;
            case R.id.btn_bottom_sheet_dialog : {
                showMenuUpload();
                break;
            }
        }
    }

    private void showMenuUpload() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

    private void showFileChooser() {
        Intent intentShowFileChooser = new Intent(Intent.ACTION_GET_CONTENT);
        intentShowFileChooser.setType("*/*");
        intentShowFileChooser.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intentShowFileChooser.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intentShowFileChooser, "Chọn file để upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Yêu cầu cài đặt File Manager",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE: {
                if (RESULT_OK == resultCode) {
                    if ( null != data) {
                        uriList.clear();
                        if ( null != data.getClipData()) {
                            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                uriList.add(data.getClipData().getItemAt(i).getUri());
                            }
                        } else {
                            uriList.add(data.getData());
                        }
                        for (int j = 0; j < uriList.size(); j++) {
                            Toast.makeText(getApplicationContext(), uriList.get(j).getPath(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_REQUEST: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Bạn phải cấp quyền cho ứng dụng", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
