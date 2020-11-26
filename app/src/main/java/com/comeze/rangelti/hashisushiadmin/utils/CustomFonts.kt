package com.comeze.rangelti.hashisushiadmin.utils

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity

class CustomFonts : AppCompatActivity() {

    //Altera fonte do txtLogo
    fun fontLogo(): Typeface {
        return Typeface.createFromAsset(assets, "RagingRedLotusBB.ttf")
    }
}
