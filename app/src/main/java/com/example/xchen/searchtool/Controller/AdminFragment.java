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

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XChen on 4/3/2018.
 */

public class AdminFragment extends Fragment {
    @BindView(R.id.btnAdminContent) Button btnAdminContent;
    @BindView(R.id.btnAdminAccessory) Button btnAdminAccessory;
    @BindView(R.id.expandable_layout) ExpandableLayout expandableLayout;


    /*private ExpandableLayout expandableLayout0;
    private ExpandableLayout expandableLayout1;*/


    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.managementfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        InitButtons();





        /*expandableLayout0 = view.findViewById(R.id.expandable_layout_0);
        expandableLayout1 = view.findViewById(R.id.expandable_layout_1);

        expandableLayout0.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });

        expandableLayout1.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });

        view.findViewById(R.id.expand_button_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout0.expand();
                expandableLayout1.collapse();
            }
        });
        view.findViewById(R.id.expand_button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout0.collapse();
                expandableLayout1.expand();
            }
        });*/




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
                /*AdminActivity.loadContentAdmin(getContext());*/
                if(expandableLayout.isExpanded())
                    expandableLayout.collapse();
                else
                    expandableLayout.expand();
            }
        });

        btnAdminAccessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
