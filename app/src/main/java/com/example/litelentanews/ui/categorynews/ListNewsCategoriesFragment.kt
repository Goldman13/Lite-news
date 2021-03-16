package com.example.litelentanews.ui.categorynews

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.litelentanews.R
import com.example.litelentanews.databinding.FragmentListNewsCategoriesBinding
import com.example.litelentanews.ui.listnews.ListNewsCategoryAdapter
import com.example.litelentanews.ui.start.StartFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ListNewsCategoriesFragment : DaggerFragment() {

    val args: ListNewsCategoriesFragmentArgs by navArgs()

    val recAdapter = ListNewsCategoryAdapter(){
        openCategoryNewsList(it)
    }
    lateinit var binding: FragmentListNewsCategoriesBinding
    @Inject
    lateinit var factory:ViewModelProvider.Factory
    val viewModel by viewModels<ListNewsCategoriesViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListNewsCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val divider = DividerItemDecoration(context,DividerItemDecoration.VERTICAL)

        binding.recyclerListCategoryNews.apply {
            adapter = recAdapter
            addItemDecoration(divider)
        }

        viewModel.name = args.dataClass

        viewModel.list.observe(viewLifecycleOwner){ list ->
            if(list.isEmpty()){
                    AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.dialog_list_empty))
                            .setPositiveButton(getString(R.string.dialogbutton_load_news)) { _, _ ->
                                viewModel.uploadNews()
                            }
                            .create().show()
            }else{
                recAdapter.list = list
                recAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.news_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.news_or_categories).apply {
            setVisible(true)
            activity?.getPreferences(Context.MODE_PRIVATE).let { sharedPreferences ->
                     title = if(sharedPreferences?.getBoolean(StartFragment.sharPrefKey,true)?:true){
                         "View as all news"
                     }else "View as categories"
            }
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.news_or_categories ->{
                activity?.getPreferences(Context.MODE_PRIVATE).let {
                    it?.let{
                        val result = it.getBoolean(StartFragment.sharPrefKey,true)
                        it.edit().putBoolean(
                                StartFragment.sharPrefKey,
                                !result
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

    private fun openCategoryNewsList(category:String){
        val action = if(category.isEmpty())
            ListNewsCategoriesFragmentDirections.actionListNewsCategoriesFragmentToListNewsFragmentVariant(category, args.dataClass)
        else
            ListNewsCategoriesFragmentDirections.actionListNewsCategoriesFragmentToListNewsFragment(category, args.dataClass)
        findNavController().navigate(action)
    }

    private fun setDestination(){
        openCategoryNewsList("")
    }
}