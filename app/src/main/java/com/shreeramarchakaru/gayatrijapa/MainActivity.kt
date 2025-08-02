package com.shreeramarchakaru.gayatrijapa

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.shreeramarchakaru.gayatrijapa.databinding.ActivityMainBinding
import com.shreeramarchakaru.gayatrijapa.ui.BaseActivity
import com.shreeramarchakaru.gayatrijapa.utils.common.LanguageSelectionDialogFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNavigation()
    }

/*
    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }
*/


    private fun setupNavigation() {
        // THIS IS THE FIX
        // 1. Find the NavHostFragment from the layout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment // Use the ID from your XML

        // 2. Get the NavController from the NavHostFragment
        val navController = navHostFragment.navController

        // Now you can use the navController for things like setting up the Toolbar
        // For example:
        binding.bottomNavigation.setupWithNavController(navController)


        // Handle navigation to fragments that require arguments
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.sankalpFragment -> {
                    // Navigate directly to sankalpFragment with default argument
                    navController.navigate(R.id.sankalpFragment)
                    true
                }
                R.id.japaSelectionFragment -> {
                    navController.navigate(R.id.japaSelectionFragment)
                    true
                }
                R.id.historyFragment -> {
                    navController.navigate(R.id.historyFragment)
                    true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_language -> {
                showLanguageDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLanguageDialog() {
        val dialog = LanguageSelectionDialogFragment { languageCode ->
            Toast.makeText(this, getString(R.string.language_changed), Toast.LENGTH_SHORT).show()
        }
        dialog.show(supportFragmentManager, "LanguageDialog")
    }
}