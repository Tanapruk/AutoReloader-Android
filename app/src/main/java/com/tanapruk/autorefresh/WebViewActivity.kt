package com.tanapruk.autorefresh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_view.*
import java.net.MalformedURLException
import java.net.URL

class WebViewActivity : AppCompatActivity() {

    var urlToSpam: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        urlToSpam = intent.getStringExtra("URL_TO_SPAM")

        setupViews()

        urlToSpam?.let {
            wvContent.loadUrl(urlToSpam)
        }
    }

    private fun setupViews() {
        supportActionBar?.title = urlToSpam
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        wvContent.webChromeClient = customWebChromeClient()
        wvContent.webViewClient = customWebViewClient()
        wvContent.clearCache(true)
        wvContent.settings.cacheMode = WebSettings.LOAD_NO_CACHE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun customWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                supportActionBar?.title = view?.url
                pbLoading.progress = newProgress
                if (newProgress >= 100) {
                    hideNowLoading()
                } else {
                    showNowLoading()
                }
            }

        }

    }

    private fun customWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (shouldReload(url)) {
                    wvContent.clearCache(true)
                    view?.loadUrl(urlToSpam)
                }
            }

        }
    }

    fun shouldReload(url: String?): Boolean {
        return try {
            var expectedUrl = URL(urlToSpam)

            if (expectedUrl.path.isEmpty()) {
                urlToSpam += "/"
                expectedUrl = URL(urlToSpam)
            }

            val urlExtractFromPage = URL(url)
            "${urlExtractFromPage.host}${urlExtractFromPage.path}" != "${expectedUrl.host}${expectedUrl.path}"
        } catch (error: MalformedURLException) {
            false
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        urlToSpam = savedInstanceState?.getString("URL_TO_SPAM")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("URL_TO_SPAM", urlToSpam)
    }

    fun showNowLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    fun hideNowLoading() {
        pbLoading.visibility = View.INVISIBLE
    }


}
