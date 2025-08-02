package com.shreeramarchakaru.gayatrijapa.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.shreeramarchakaru.gayatrijapa.MainActivity
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.databinding.ActivitySplashBinding
import com.shreeramarchakaru.gayatrijapa.ui.BaseActivity
import com.shreeramarchakaru.gayatrijapa.ui.auth.LoginActivity

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observeViewModel()

        // Delay for splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.checkUserLoginStatus()
        }, 2000)
    }

    private fun observeViewModel() {
        viewModel.navigationEvent.observe(this) { destination ->
            when (destination) {
                SplashViewModel.NavigationDestination.LOGIN -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                SplashViewModel.NavigationDestination.MAIN -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
            finish()
        }
    }
}