package com.myprojects.android.shortcutstorage

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myprojects.android.shortcutstorage.databinding.ActivityHomeBinding
import com.myprojects.android.shortcutstorage.model.Values
import com.myprojects.android.shortcutstorage.ui.storage.StorageViewModel
import com.myprojects.android.shortcutstorage.ui.work.WorkViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var storageViewModel: StorageViewModel
    private lateinit var workViewModel: WorkViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        initColors()
        setContentView(binding.root)

        initStorageViewModel()
        initWorkViewModel()

        initNavigation()
    }

    private fun initColors() {
        sharedPreferences = getSharedPreferences(Values.PREF_NAME, MODE_PRIVATE)

        when (sharedPreferences.getString(Values.KEY_THEME, Values.THEME_RED)!!) {
            Values.THEME_RED -> { super.setTheme(R.style.Red_AppTheme) }
            Values.THEME_GREEN -> { super.setTheme(R.style.Green_AppTheme) }
            Values.THEME_BLUE -> { super.setTheme(R.style.Blue_AppTheme) }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fgContainer)
        return navController.navigateUp(appBarConfiguration) || super .onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.miSettings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return true
    }

    private fun initWorkViewModel() {
        workViewModel = ViewModelProvider(this)[WorkViewModel::class.java]
        workViewModel.updateSortType(
            sharedPreferences.getString(
                Values.KEY_WORK_SORT,
                Values.WORK_SORT_BY_NAME
            )!!
        )
    }

    private fun initStorageViewModel() {
        storageViewModel = ViewModelProvider(this)[StorageViewModel::class.java]
        storageViewModel.updateSortType(
            sharedPreferences.getString(
                Values.KEY_STORAGE_SORT,
                Values.STORAGE_SORT_BY_NAME
            )!!
        )
    }

    private fun initNavigation() {
        setSupportActionBar(binding.tbHome)
        val navView: BottomNavigationView = binding.bottomNavigationView

        val navController = findNavController(R.id.fgContainer)
        navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_work, R.id.navigation_storage
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}
