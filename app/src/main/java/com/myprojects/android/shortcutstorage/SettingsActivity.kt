package com.myprojects.android.shortcutstorage

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.myprojects.android.shortcutstorage.databinding.ActivitySettingsBinding
import hu.bme.aut.android.shortcutstorage.model.Values
import hu.bme.aut.android.shortcutstorage.ui.storage.StorageViewModel
import hu.bme.aut.android.shortcutstorage.ui.work.WorkViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        sharedPreferences = getSharedPreferences(Values.PREF_NAME, MODE_PRIVATE)
        editor = sharedPreferences.edit()

        when (sharedPreferences.getString(Values.KEY_THEME, Values.THEME_RED)!!) {
            Values.THEME_RED -> { super.setTheme(R.style.Red_AppTheme) }
            Values.THEME_GREEN -> { super.setTheme(R.style.Green_AppTheme) }
            Values.THEME_BLUE -> { super.setTheme(R.style.Blue_AppTheme) }
        }
        setContentView(binding.root)

        setSupportActionBar(binding.tbSettings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // Theme selectors
        binding.btnRedTheme.setOnClickListener { saveTheme(Values.THEME_RED) }
        binding.btnGreenTheme.setOnClickListener { saveTheme(Values.THEME_GREEN) }
        binding.btnBlueTheme.setOnClickListener { saveTheme(Values.THEME_BLUE) }

        // Work sort
        binding.rgWorkSortTypes.check(
            when(sharedPreferences.getString(Values.KEY_WORK_SORT, Values.WORK_SORT_BY_NAME)) {
                Values.WORK_SORT_BY_NAME -> R.id.rbWorkSortByName
                Values.WORK_SORT_BY_ADDRESS -> R.id.rbWorkSortByAddress
                Values.WORK_SORT_BY_DATE -> R.id.rbWorkSortByDate
                else -> R.id.rbWorkSortByName
            }
        )

        binding.rgWorkSortTypes.setOnCheckedChangeListener {_, id ->
            saveWorkSort(
                when(id) {
                    R.id.rbWorkSortByName -> Values.WORK_SORT_BY_NAME
                    R.id.rbWorkSortByAddress -> Values.WORK_SORT_BY_ADDRESS
                    R.id.rbWorkSortByDate -> Values.WORK_SORT_BY_DATE
                    else -> ""
                }
            )
        }

        // Storage sort
        binding.rgStorageSortTypes.check(
            when(sharedPreferences.getString(Values.KEY_STORAGE_SORT, Values.STORAGE_SORT_BY_NAME)) {
                Values.STORAGE_SORT_BY_NAME -> R.id.rbStorageSortByName
                Values.STORAGE_SORT_BY_AMOUNT -> R.id.rbStorageSortByAmount
                Values.STORAGE_SORT_BY_UNIT -> R.id.rbStorageSortByUnit
                else -> R.id.rbStorageSortByName
            }
        )

        binding.rgStorageSortTypes.setOnCheckedChangeListener {_, id ->
            saveStorageSort(
                when(id) {
                    R.id.rbStorageSortByName -> Values.STORAGE_SORT_BY_NAME
                    R.id.rbStorageSortByAmount -> Values.STORAGE_SORT_BY_AMOUNT
                    R.id.rbStorageSortByUnit -> Values.STORAGE_SORT_BY_UNIT
                    else -> ""
                }
            )
        }

        binding.btnDeleteAllWorks.setOnClickListener {
            val workViewModel = ViewModelProvider(this)[WorkViewModel::class.java]
            AlertDialog.Builder(this)
                .setPositiveButton("Yes") {_, _ ->
                    workViewModel.deleteAllWork()
                }
                .setNegativeButton("No") {_, _ -> }
                .setTitle("Delete All Works")
                .setMessage("Do you want to delete all work?")
                .create().show()
        }

        binding.btnDeleteStorage.setOnClickListener {
            val storageViewModel = ViewModelProvider(this)[StorageViewModel::class.java]
            AlertDialog.Builder(this)
                .setPositiveButton("Yes") {_, _ ->
                    storageViewModel.deleteAllItem()
                }
                .setNegativeButton("No") {_, _ -> }
                .setTitle("Delete All Storage Item")
                .setMessage("Do you want to delete all storage item?")
                .create().show()
        }
    }

    private fun saveTheme(theme: String) {
        editor.putString(Values.KEY_THEME, theme).apply()
        this.recreate()
    }

    private fun saveWorkSort(sort: String) {
        editor.putString(Values.KEY_WORK_SORT, sort).apply()
    }

    private fun saveStorageSort(sort: String) {
        editor.putString(Values.KEY_STORAGE_SORT, sort).apply()
    }
}