package com.nassafy.aro.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nassafy.aro.data.dto.KpResponse
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.domain.api.AuroraApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.di.HeaderInterceptorApi
import com.nassafy.aro.util.di.WithoutHeaderInterceptorApi
import com.nassafy.aro.util.setNetworkResult
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.log

private const val TAG = "AuroraRepository_sdr"
class AuroraRepository @Inject constructor(
    @WithoutHeaderInterceptorApi private val auroraApi: AuroraApi,
    @HeaderInterceptorApi private val headerAuroraApi: AuroraApi
) {
    private val _placeItemListLiveData = MutableLiveData<NetworkResult<List<PlaceItem>>>()
    val placeItemListLiveData : LiveData<NetworkResult<List<PlaceItem>>>
        get () = _placeItemListLiveData

    suspend fun getPlaceItemList() {
        val response = headerAuroraApi.getAllPlaces()
        _placeItemListLiveData.postValue(NetworkResult.Loading())
        when {
            response.isSuccessful && response.body() != null -> {
                _placeItemListLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            response.errorBody() != null -> {
                _placeItemListLiveData.postValue(NetworkResult.Error(response.errorBody().toString()))
            }
            else -> {
                _placeItemListLiveData.postValue(NetworkResult.Error(response.headers().toString()))
            }
        }
    } // End of getPlaceItemList

    private val _kpCurrentLiveData = MutableLiveData<NetworkResult<KpResponse>>()
    val kpCurrentLiveData : LiveData<NetworkResult<KpResponse>>
        get() = _kpCurrentLiveData

    suspend fun getCurrentKpIndex(dateString: String, hour: Int) {
        val response = headerAuroraApi.getCurrentKpIndex(dateString, hour)
        _kpCurrentLiveData.setNetworkResult(response)
    }

}