package com.tmacbo.eqdushu.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmacbo.eqdushu.R;
import com.tmacbo.eqdushu.activity.QRScanActivity;
import com.tmacbo.eqdushu.utils.permissionlib.PerUtils;
import com.tmacbo.eqdushu.utils.permissionlib.PerimissionsCallback;
import com.tmacbo.eqdushu.utils.permissionlib.PermissionEnum;
import com.tmacbo.eqdushu.utils.permissionlib.PermissionManager;

import java.util.ArrayList;


/**
 * @Author tmacbo
 * @Since on 2017/6/26  21:30
 * @mail tang_bo@hotmail.com
 * @Description TODO
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView textView;
    private TextView textViewScan;
    private ImageView imageAddBook;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        addAction(rootView);
        return rootView;
    }

    public void addAction(View rootView) {
        textView = (TextView) rootView.findViewById(R.id.section_label);
        textViewScan = (TextView) rootView.findViewById(R.id.textViewScan);
        imageAddBook = (ImageView) rootView.findViewById(R.id.imageAddBook);

        textViewScan.setOnClickListener(this);
        textView.setText("HomeFragment");
    }


    @Override
    public void onClick(View view) {
        if (view == textViewScan) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermissionManager
                        .with(getActivity())
                        .tag(10)
                        .permission(PermissionEnum.CAMERA, PermissionEnum.WRITE_EXTERNAL_STORAGE)
                        .callback(new PerimissionsCallback() {
                            @Override
                            public void onGranted(ArrayList<PermissionEnum> grantedList) {
                                startActivity(new Intent(getActivity(), QRScanActivity.class));
                            }

                            @Override
                            public void onDenied(ArrayList<PermissionEnum> deniedList) {
                                PerUtils.PermissionDenied(getActivity(), deniedList);
                            }
                        })
                        .checkAsk();
            } else {
                if (PerUtils.cameraIsCanUse()) {
                    startActivity(new Intent(getActivity(), QRScanActivity.class));
                } else {
                    PerUtils.PermissionDenied(getActivity(), PermissionEnum.CAMERA);
                }
            }
        }
    }
}
