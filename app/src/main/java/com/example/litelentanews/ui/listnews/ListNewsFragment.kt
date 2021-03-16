package com.example.litelentanews.ui.listnews

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Base64
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.litelentanews.R
import com.example.litelentanews.data.db.NewsEntity
import com.example.litelentanews.databinding.ListNewsFragmentBinding
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.ui.start.StartFragment
import com.example.litelentanews.util.Result
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.*
import javax.inject.Inject

class ListNewsFragment: DaggerFragment(){

    val args: ListNewsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel:NewsViewModel by viewModels{viewModelFactory}
    private lateinit var binding: ListNewsFragmentBinding
    private var commonList = emptyList<NewsItemModel>()
    private lateinit var listAdapter:ListNewsAdapter

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
        viewModel.addSource(args.categoryName, args.className)
        binding.webFullNews.settings.apply {
            useWideViewPort = true
            loadWithOverviewMode = true
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.webFullNews)

        listAdapter = ListNewsAdapter(
                bottomSheetBehavior,
                {
                    url: String, viewItem: View, callback: ActionMode.Callback ->
                    viewModel.viewModelScope.launch {
                        val result = viewModel.getDescriptionNewsItem(url)
                        if(result != "")
                        {
                            binding.webFullNews.apply{
                                //clearView()
                                val encodedHtml = Base64.encodeToString(result.toByteArray(), Base64.NO_PADDING)
                                loadData(encodedHtml, "text/html", "base64")
                            }

                            view.startActionMode(callback)
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }
                },
                {
                    guid:String, value:Boolean-> viewModel.changeFavorite(guid, value)
                }
        )
        binding.recyclerListnews.apply {
            adapter = listAdapter
            ItemTouchHelper(ItemTouchHelperCallBack(adapter as ListNewsAdapter)).attachToRecyclerView(this)
        }

        viewModel.status.observe(viewLifecycleOwner,
                {
                    binding.swiperefresh.setRefreshing(false)
                    when (it) {
                        is Result.Success<*> -> Unit
                        is Result.Error<*> -> {
                            Snackbar.make(view,it.message,Snackbar.LENGTH_SHORT).show()
                        }
                        is Result.Load -> Unit
                    }
                }
        )

        viewModel.queryNewsList.observe(viewLifecycleOwner,{list->
            if(list.isEmpty()){
                AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.dialog_list_empty))
                        .setPositiveButton(getString(R.string.dialogbutton_load_news)) { _, _ ->
                            viewModel.uploadNews()
                        }
                        .create().show()
            }else{
                listAdapter.submitList(list as MutableList<NewsItemModel>)
                binding.recyclerListnews.apply {
                    if(itemDecorationCount>1)removeItemDecorationAt(1)
                    addItemDecoration(CustomItemDecoration(context,list))
                }
            }
        })

        binding.swiperefresh.setOnRefreshListener {
            viewModel.uploadNews()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.news_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if(args.categoryName.isEmpty()){
            menu.findItem(R.id.news_or_categories).apply {
                activity?.getPreferences(Context.MODE_PRIVATE).let { sharedPreferences ->
                    title = if (sharedPreferences?.getBoolean(StartFragment.sharPrefKey, true)
                                    ?: true) {
                        "View as all news"
                    } else "View as categories"
                }
                setVisible(true)
            }
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.news_or_categories ->{
                activity?.getPreferences(Context.MODE_PRIVATE).let {
                    it?.let{
                        it.edit().putBoolean(
                                StartFragment.sharPrefKey,
                                !it.getBoolean(StartFragment.sharPrefKey,true)
                        ).apply()
                        setDestination()
                    }
                    true
                }
            }
            R.id.uploadList -> {
                viewModel.uploadNews()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openCategoryNewsList(){
        val action = ListNewsFragmentDirections.actionListNewsFragmentAllListToListNewsCategoriesFragment(args.className)
        findNavController().navigate(action)
    }


    private fun setDestination(){
        openCategoryNewsList()
    }
}