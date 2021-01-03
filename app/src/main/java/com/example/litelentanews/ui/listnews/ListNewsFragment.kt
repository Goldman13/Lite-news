package com.example.litelentanews.ui.listnews

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ThemedSpinnerAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.litelentanews.R
import com.example.litelentanews.databinding.ListNewsFragmentBinding
import com.example.litelentanews.domain.model.LentaNewsItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ListNewsFragment: DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel:ListNewsViewModel by viewModels{viewModelFactory}
    private lateinit var binding: ListNewsFragmentBinding
    private var commonList = emptyList<LentaNewsItem>()
    private lateinit var adapter:ListNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = ListNewsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.webFullNews.settings.apply {
            useWideViewPort = true
            loadWithOverviewMode = true
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.webFullNews)

        adapter = ListNewsAdapter(emptyList(),bottomSheetBehavior){
            url: String, viewItem: View, callback: ActionMode.Callback ->
            viewModel.viewModelScope.launch {
                val result = viewModel.getDescriptionNewsItem(url)
                 if(result != "")
                 {
                     binding.webFullNews.apply{
                        val encodedHtml = Base64.encodeToString(result.toByteArray(), Base64.NO_PADDING)
                        loadData(encodedHtml, "text/html", "base64")
                    }
                     view.startActionMode(callback)
                     bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        binding.recyclerListnews.adapter = adapter

        viewModel.listNews.observe(viewLifecycleOwner,{
            adapter.list = it
            adapter.notifyDataSetChanged()
        })

        viewModel.status.observe(viewLifecycleOwner, {
            it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.lenta_news_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.uploadList ->{
                viewModel.loadRssList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}