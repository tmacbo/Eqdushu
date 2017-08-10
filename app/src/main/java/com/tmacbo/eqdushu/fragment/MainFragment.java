package com.tmacbo.eqdushu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tmacbo.eqdushu.R;

/**
 * @Author tmacbo
 * @Since on 2017/8/10  11:35
 * @mail tang_bo@hotmail.com
 * @Description TODO
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("HomeFragment");
        return rootView;
    }


//    public void onSomeButtonClicked(View view) {
//        getActivity().getWindow().setExitTransition(new Explode());
//        Intent intent = new Intent(getContext(), LoginActivity.class);
//        startActivity(intent,
//                ActivityOptions
//                        .makeSceneTransitionAnimation(getActivity()).toBundle());
//    }

    @Override
    public void onClick(View view) {
    }
}