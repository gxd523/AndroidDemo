package com.gxd.demo.android.base

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import com.gxd.demo.android.util.ViewBindingUtil
import com.gxd.demo.android.util.callInflate

abstract class ViewBindingActivity<V : ViewBinding> : ComponentActivity() {
    private var _binding: V? = null
    val binding: V by lazy {
        val viewBindingClass = ViewBindingUtil.getViewBindingClass<V>(this@ViewBindingActivity)
        _binding = viewBindingClass?.callInflate(layoutInflater, attachToParent = false)

        if (_binding == null) {
            throw RuntimeException("Something wrong with ViewBinding init!")
        }
        _binding!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) window.isNavigationBarContrastEnforced = false
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        setContentView(binding.root)
    }
}