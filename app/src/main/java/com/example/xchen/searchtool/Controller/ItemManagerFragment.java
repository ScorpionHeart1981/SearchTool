package com.example.xchen.searchtool.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xchen.searchtool.R;

/**
 * Created by XChen on 4/19/2018.
 */

public class ItemManagerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.adminitemfragment, container, false);

        if(savedInstanceState!=null) {
            String catalogName = savedInstanceState.getString("catalogName");
            TextView textView= view.findViewById(R.id.textView2);
            textView.setText(catalogName);
        }
        return view;
    }
}
