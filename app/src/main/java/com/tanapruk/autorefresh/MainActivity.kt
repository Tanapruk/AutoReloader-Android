package com.tanapruk.autorefresh

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.webkit.URLUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var urlToSpam: String? = ""
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        urlToSpam = sharedPreferences.getString("URL_TO_SPAM", "")
        etWebsite.setText(urlToSpam)
        setButtonOnClick()
    }

    private fun setButtonOnClick() {
        btnGoToWebsite.setOnClickListener {
            val url = etWebsite.text.toString()
            if (Patterns.WEB_URL.matcher(url).matches()) {
                urlToSpam = url
                sharedPreferences.edit().putString("URL_TO_SPAM", urlToSpam).apply()
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("URL_TO_SPAM", url)
                startActivity(intent)
            } else {
                etWebsite.error = "URL is in valid"
            }
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
}
