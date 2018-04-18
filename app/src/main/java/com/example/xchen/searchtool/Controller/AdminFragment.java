package com.example.xchen.searchtool.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.xchen.searchtool.AdminActivity;
import com.example.xchen.searchtool.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XChen on 4/3/2018.
 */

public class AdminFragment extends Fragment {
    @BindView(R.id.btnAdminContent) Button btnAdminContent;
    @BindView(R.id.btnAdminAccessory) Button btnAdminAccessory;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.adminfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        InitButtons();
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    private void InitButtons()
    {
        btnAdminContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminActivity.loadContentAdmin(getContext());
            }
        });

        btnAdminAccessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
