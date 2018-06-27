package com.tanapruk.autorefresh

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        wvContent.webChromeClient = customWebChromeClient()
        wvContent.webViewClient = customWebViewClient()

        wvContent.loadUrl("https://www.google.com/")
    }

    private fun customWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                pbLoading.progress = newProgress
            }

        }

    }

    private fun customWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url !== "specificUrl") {
                    view?.loadUrl(url)
                }
            }

        }
    }

    fun showNowLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    fun hideNowLoading() {
        pbLoading.visibility = View.INVISIBLE
    }


}
