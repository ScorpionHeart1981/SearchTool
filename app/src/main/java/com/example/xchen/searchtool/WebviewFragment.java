package com.example.xchen.searchtool;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XChen on 4/9/2018.
 */

public class WebviewFragment extends Fragment {

    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.btnBack) Button btnBack;
    private Unbinder unbinder;

    private ProgressDialog pd;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.webviewfragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        setProgressDialog();

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                if (!pd.isShowing()) {
                    pd.show();
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                System.out.println("---sslError:" + error);
                super.onReceivedSslError(view, handler, error);
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null) {
            String data = bundle.getString("DATA");
            if (data != null)
            {
                String query = Uri.encode("书画");
                String url = "http://wap.baidu.com/sf/vsearch?pd=image_content&word="+query+"&tn=vsearch&sa=vs_tab&lid=6597869933256736211&ms=1&atn=page&fr=tab";
                url = "www.sohu.com";
                SetupWebViewSetting(webView);
                /*webView.loadUrl(url);*/

            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void SetupWebViewSetting(WebView webView){
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可


        webView.loadUrl("www.google.com");

        /*webSettings.setDomStorageEnabled(true);
        webSettings.setMinimumFontSize(20);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式*/


        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
    }

    @Override
    public void onDestroyView(){
        webView = null;
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setProgressDialog(){
        pd = new ProgressDialog(getContext());
        pd.setMessage(getResources().getString(R.string.loading));
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
    }
}
