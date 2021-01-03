package com.example.litelentanews.domain

import com.example.litelentanews.domain.repository.Repository
import com.example.litelentanews.util.Result
import javax.inject.Inject

class GetLentaRssUseCase @Inject constructor(
    val repo:Repository
    ){
    suspend fun getRssList():Result<*>{
        return repo.loadRssList()
    }
}