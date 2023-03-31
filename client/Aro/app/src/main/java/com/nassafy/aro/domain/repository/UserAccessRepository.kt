package com.nassafy.aro.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.TokenResponse
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.SnsLoginResposne
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.domain.api.UserAccessApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.setNetworkResult
import javax.inject.Inject

class UserAccessRepository @Inject constructor(private val userAccessApi: UserAccessApi) { // End of UserAccessRepository

    private val _loginToken = MutableLiveData<NetworkResult<TokenResponse>>()
    val loginToken: LiveData<NetworkResult<TokenResponse>> get() = _loginToken

    private val _isEmailValidated = MutableLiveData<NetworkResult<Boolean>>()
    val isEmailValidated: LiveData<NetworkResult<Boolean>> get() = _isEmailValidated

    private val _isEmailAuthCodeValidated = MutableLiveData<Boolean>()
    val isEmailAuthCodeValidated get() = _isEmailAuthCodeValidated

    private val _countryListLiveData = MutableLiveData<NetworkResult<List<String>>>()
    val countryListLiveData get() = _countryListLiveData

    private val _placeListLiveData = MutableLiveData<NetworkResult<List<PlaceItem>>>()
    val placeListLiveData get() = _placeListLiveData

    private val _selectedAuraraPlaceListLiveData = MutableLiveData<MutableList<PlaceItem>>()
    val selectedAuraraPlaceListLiveData get() = _selectedAuraraPlaceListLiveData

    private val _selectedMeteorPlaceListLiveData = MutableLiveData<MutableList<PlaceItem>>()
    val selectedMeteorPlaceListLiveData get() = _selectedMeteorPlaceListLiveData

    private val _userJoinNetworkResultLiveData = MutableLiveData<NetworkResult<TokenResponse>>()
    val userJoinNetworkResultLiveData:LiveData<NetworkResult<TokenResponse>> get() = _userJoinNetworkResultLiveData

    private val _userSnsLoginNetworkResultLiveData = MutableLiveData<NetworkResult<SnsLoginResposne>>()
    val userSnsLoginNetworkResultLiveData: LiveData<NetworkResult<SnsLoginResposne>> get() = _userSnsLoginNetworkResultLiveData

    init {
        _placeListLiveData.value = NetworkResult.Loading()
        _selectedAuraraPlaceListLiveData.value = mutableListOf()
        _selectedMeteorPlaceListLiveData.value = mutableListOf()
    }

    suspend fun loginByIdPassword(providerType: String, email: String, password: String?) {
        val response = userAccessApi.login(JsonObject().apply {
            addProperty("providerType", providerType)
            addProperty("email", email)
            addProperty("password", password)
        })
        _loginToken.postValue(NetworkResult.Loading())

        try {
            when {
                response.isSuccessful -> {
                    _loginToken.postValue(
                        NetworkResult.Success(
                            response.body()!!
                        )
                    ) // End of postValue
                } // End of response.isSuccessful
                response.errorBody() != null -> {
                    _loginToken.postValue(NetworkResult.Error(response.errorBody()!!.string()))
                } // End of response.errorBody
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        }
    } // End of loginByIdPassword


    suspend fun validateEmialAuthenticationCode(email: String, code: String) {
        val response = userAccessApi.validateEmailAuthenticationCode(JsonObject().apply {
            addProperty("email", email)
            addProperty("code", code)
        })
        Log.d("ssafy", response.toString())
        try {
            when {
                response.isSuccessful -> {
                    Log.d("ssafy", "validate Code!")
                    _isEmailAuthCodeValidated.postValue(true)
                }
                response.errorBody() != null -> _isEmailAuthCodeValidated.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    } // End of validateEmialAuthenticationCode

    suspend fun validateEmail(email: String) {
        val response = userAccessApi.validateEmail(JsonObject().apply {
            addProperty("email", email)
        })
        Log.d("ssafy_pcs/validate", response.toString())
        _isEmailValidated.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> _isEmailValidated.postValue(NetworkResult.Success(true))
                response.errorBody() != null -> _isEmailValidated.postValue(NetworkResult.Error(response.errorBody().toString()))
            }
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    } // End of validateEmail

    suspend fun getCountryList() {
        val response = userAccessApi.getCountryList()
        _countryListLiveData.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> {
                    _countryListLiveData.postValue(NetworkResult.Success(
                        response.body()!!
                    ))
                }
                response.errorBody() != null -> _countryListLiveData.postValue(NetworkResult.Error(response.errorBody().toString()))
            }
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    } // End of getNations

    suspend fun getPlaceList(nation: String) {
        val response = userAccessApi.getPlaceList(nation)
        _placeListLiveData.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> {
                    _placeListLiveData.postValue(NetworkResult.Success(
                        response.body()!!
                    ))
                }
                response.errorBody() != null -> _countryListLiveData.postValue(NetworkResult.Error(response.errorBody().toString()))
            }
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    }

    suspend fun join(user: JsonObject) {
        Log.d("ssafy_pcs/join/request", user.toString())
        val response = userAccessApi.join(user)
        Log.d("ssafy_pcs/join/response", response.toString())
        _userJoinNetworkResultLiveData.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> {
                    _userJoinNetworkResultLiveData.postValue(NetworkResult.Success(response.body()!!))
                }
                response.errorBody() != null -> _userJoinNetworkResultLiveData.postValue(NetworkResult.Error(response.errorBody().toString()))
            }
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    }

    suspend fun selectAuroraPlace(place: PlaceItem) {
        val temp = mutableListOf<PlaceItem>()
        temp.addAll(_selectedAuraraPlaceListLiveData.value!!)
        temp.add(place)
        _selectedAuraraPlaceListLiveData.value = temp
    }

    fun unSelectAuroraPlace(place: PlaceItem) {
        val temp = mutableListOf<PlaceItem>()
        temp.addAll(_selectedAuraraPlaceListLiveData.value!!)
        temp.remove(place)
        _selectedAuraraPlaceListLiveData.value = temp
    }

    fun clearSelectedAuroraPlaceList() {
        val temp = mutableListOf<PlaceItem>()
        _selectedAuraraPlaceListLiveData.value = temp
    }

    fun clearSelectedMeteorPlaceList() {
        val temp = mutableListOf<PlaceItem>()
        _selectedMeteorPlaceListLiveData.value = temp
    }

    suspend fun snsLogin(providerType: String, accessToken: String) {
        val response = userAccessApi.snsLogin(JsonObject().apply {
            addProperty("providerType", providerType)
            addProperty("accessToken", accessToken)
        })
        _userSnsLoginNetworkResultLiveData.setNetworkResult(response)
    }

}