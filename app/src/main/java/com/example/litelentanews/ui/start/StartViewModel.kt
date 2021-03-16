package com.example.litelentanews.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litelentanews.data.api.FontankaNews_DataSource
import com.example.litelentanews.data.api.LentaNews_DataSource
import com.example.litelentanews.data.api.Nplus1News_DataSource
import com.example.litelentanews.domain.usecase.DeleteAllOldNewsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.litelentanews.util.Result
import kotlinx.coroutines.delay

class StartViewModel @Inject constructor(
    val deleteAllOldNewsUseCase: DeleteAllOldNewsUseCase
): ViewModel() {

    private val _status = MutableLiveData<Result<*>>()
    val status:LiveData<Result<*>> = _status

    val mapDataSource = mapOf(
            Pair(FontankaNews_DataSource.nameNewsCompany,FontankaNews_DataSource::class.java),
            Pair(LentaNews_DataSource.nameNewsCompany,LentaNews_DataSource::class.java),
            Pair(Nplus1News_DataSource.nameNewsCompany,Nplus1News_DataSource::class.java)
    )

    init{
        deleteOldNews()
    }

    private fun deleteOldNews(){
        viewModelScope.launch {
            _status.value = Result.Load()
            _status.value = deleteAllOldNewsUseCase()
        }
    }
}
