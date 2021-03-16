package com.example.litelentanews.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.litelentanews.R
import com.example.litelentanews.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var navControler:NavController
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container) as NavHostFragment

        navControler = host.navController

        val appBarConfig = AppBarConfiguration(
                navControler.graph,
                binding.drawerMain
        )
        setupActionBarWithNavController(
                navControler,
                appBarConfig
        )
//
//        val navView = binding.navigation
//        navView.setupWithNavController(navControler)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
        android.R.id.home ->{
            navControler.navigateUp()
            true
        }
        else -> super.onOptionsItemSelected(item)
        }
    }
}