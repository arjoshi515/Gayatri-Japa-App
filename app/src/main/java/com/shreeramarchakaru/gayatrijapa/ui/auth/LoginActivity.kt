package com.shreeramarchakaru.gayatrijapa.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.shreeramarchakaru.gayatrijapa.MainActivity
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDatabase
import com.shreeramarchakaru.gayatrijapa.data.models.User
import com.shreeramarchakaru.gayatrijapa.databinding.ActivityLoginBinding
import com.shreeramarchakaru.gayatrijapa.ui.BaseActivity
import com.shreeramarchakaru.gayatrijapa.utils.TraceUtils
import com.shreeramarchakaru.gayatrijapa.utils.common.LanguageSelectionDialogFragment
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observeViewModel()
        setupClickListeners()

        testDatabaseConnection()
    }

    private fun observeViewModel() {
        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                TraceUtils.logE("LoginActivity", "Login successful, navigating to MainActivity")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                TraceUtils.logE("LoginActivity", "Login error: $message")
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val mobile = binding.etMobile.text.toString().trim()

            TraceUtils.logE("LoginActivity", "Login button clicked with name: $name, mobile: $mobile")
            viewModel.login(name, mobile)
        }

        binding.btnLanguage.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun showLanguageDialog() {
        val dialog = LanguageSelectionDialogFragment { languageCode ->
            Toast.makeText(this, getString(R.string.language_changed), Toast.LENGTH_SHORT).show()
        }
        dialog.show(supportFragmentManager, "LanguageDialog")
    }

    private fun testDatabaseConnection() {
        lifecycleScope.launch {
            try {
                val database = JapaDatabase.getDatabase(this@LoginActivity)
                val dao = database.japaDao()

                // Test database connection
                val settings = dao.getAllSettings()
                TraceUtils.logE("LoginActivity", "Database connection successful. Settings count: ${settings.size}")

                // Test user insertion
                val testUser = User(
                    name = "Test User",
                    mobileNumber = "1234567890",
                    isLoggedIn = false
                )

                val userId = dao.insertUser(testUser)
                TraceUtils.logE("LoginActivity", "Test user inserted with ID: $userId")

            } catch (e: Exception) {
                TraceUtils.logException(e)
                Toast.makeText(this@LoginActivity, "Database error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}