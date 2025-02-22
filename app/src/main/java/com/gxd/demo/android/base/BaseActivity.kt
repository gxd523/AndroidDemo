package com.gxd.demo.android.base

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity

abstract class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ggg", "${this.javaClass.simpleName}...onCreate.................${this.hashCode()}")
        super.onCreate(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d("ggg", "${this.javaClass.simpleName}...onCreate2................${this.hashCode()}")
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onRestart() {
        Log.d("ggg", "${this.javaClass.simpleName}...onRestart................${this.hashCode()}")
        super.onRestart()
    }

    override fun onStart() {
        Log.d("ggg", "${this.javaClass.simpleName}...onStart..................${this.hashCode()}")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d("ggg", "${this.javaClass.simpleName}...onRestoreInstanceState....${this.hashCode()}")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d("ggg", "${this.javaClass.simpleName}...onRestoreInstanceState2...${this.hashCode()}")
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onNewIntent(intent: Intent) {
        Log.d("ggg", "${this.javaClass.simpleName}...onNewIntent..............${this.hashCode()}")
        super.onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        Log.d("ggg", "${this.javaClass.simpleName}...onNewIntent2.............${this.hashCode()}")
        super.onNewIntent(intent, caller)
    }

    override fun onResume() {
        Log.d("ggg", "${this.javaClass.simpleName}...onResume.................${this.hashCode()}")
        super.onResume()
    }

    override fun onPause() {
        Log.d("ggg", "${this.javaClass.simpleName}...onPause..................${this.hashCode()}")
        super.onPause()
    }

    override fun onStop() {
        Log.d("ggg", "${this.javaClass.simpleName}...onStop...................${this.hashCode()}")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("ggg", "${this.javaClass.simpleName}...onSaveInstanceState......${this.hashCode()}")
        super.onSaveInstanceState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Log.d("ggg", "${this.javaClass.simpleName}...onSaveInstanceState2.....${this.hashCode()}")
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onDestroy() {
        Log.d("ggg", "${this.javaClass.simpleName}...onDestroy................${this.hashCode()}")
        super.onDestroy()
    }
}