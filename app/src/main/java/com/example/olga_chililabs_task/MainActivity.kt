package com.example.olga_chililabs_task

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.android.volley.*
import com.example.olga_chililabs_task.databinding.ActivityMainBinding
import com.example.olga_chililabs_task.network.*
import com.example.olga_chililabs_task.ui.InfiniteListAdapter
import com.example.olga_chililabs_task.ui.RecyclerViewDecoration
import com.example.olga_chililabs_task.viewmodel.*
import com.facebook.drawee.backends.pipeline.*
import org.greenrobot.eventbus.*

//code based on https://discover.hubpages.com/technology/Working-With-Android-JetPack-Paging-Library

//main activity, initializes all the other files and calls setup functions
class MainActivity : AppCompatActivity() {
    private val TAG = "MAIN-ACTIVITY"
    private lateinit var mLayout: View
    private lateinit var binding: ActivityMainBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: InfiniteListAdapter
    private lateinit var mSearchBtn: TextView
    private lateinit var mViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mLayout = binding.root
        setContentView(mLayout)

        setupViewModel()
        setupRecyclerView()
        setupCallbacks()
        search(mViewModel.mLatestQuery)
    }

    private fun setupViewModel() {
        NetworkHandler.initialize(this)
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    private fun setupRecyclerView() {
        mAdapter = InfiniteListAdapter(this) {
            Log.e(TAG, "Clicked ${it.id}")
        }

        val manager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        val decoration = RecyclerViewDecoration(6, 6)
        mRecyclerView = findViewById(R.id.list)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = manager
        mRecyclerView.addItemDecoration(decoration)
    }

    private fun setupCallbacks() {
        mSearchBtn = findViewById(R.id.btSearch)
        mSearchBtn.setOnClickListener {
            hideKeyboard(this)
            search(binding.searchEditText.text.toString())
        }

        binding.searchEditText.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard(this)
                search(binding.searchEditText.text.toString())
                true
            } else
                false
        }
    }

    private fun search(query: String) {
        Handler().postDelayed({
            mViewModel.loadImageData(query)
            mViewModel.imageList.observe(this, Observer {
                Log.e(TAG, "${it.size}")
                mAdapter.submitList(it)
            })
        }, 2000)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun networkError(error: VolleyError) {
        Toast.makeText(
            this, "An error occurred while processing your request!" +
                    " Code: ${error.message}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}