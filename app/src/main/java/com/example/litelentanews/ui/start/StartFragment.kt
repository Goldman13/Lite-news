package com.example.litelentanews.ui.start

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.litelentanews.R
import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.databinding.FragmentStartBinding
import com.example.litelentanews.util.Result
import dagger.android.support.DaggerFragment
import java.lang.NullPointerException
import javax.inject.Inject

class StartFragment : DaggerFragment() {

    lateinit var binding:FragmentStartBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val viewModel by viewModels<StartViewModel> { factory }

    companion object{
        const val sharPrefKey =  "isCategoriesOrAllNews"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentStartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.startRecycler.adapter = StartAdapter(viewModel.mapDataSource.keys.toList()){
            val result = activity
                ?.getPreferences(Context.MODE_PRIVATE)
                ?.getBoolean(sharPrefKey,true)
                ?:true

            val className = viewModel.mapDataSource
                .get(it)
                ?.canonicalName
                ?:throw NullPointerException()

            if(result){
                findNavController().navigate(
                    StartFragmentDirections.actionStartFragmentToListNewsCategoriesFragment(className)
                )
            }else{
                findNavController().navigate(
                    StartFragmentDirections.actionStartFragmentToListNewsFragmentAllList("",className)
                )
            }
        }

        viewModel.status.observe(viewLifecycleOwner,{
            binding.progressBar.visibility = if(it is Result.Load) View.VISIBLE
            else View.GONE
        })

        binding.startRecycler.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            GridLayoutManager.VERTICAL,
            false
        )
    }
}