package com.nassafy.aro.ui.view.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.*
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.MeteorCountry
import com.nassafy.aro.databinding.FragmentAroCountryPlaceSelectBinding
import com.nassafy.aro.ui.adapter.CountrySpinnerAdapter
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.custom.*
import com.nassafy.aro.ui.view.main.MainActivityViewModel
import com.nassafy.aro.ui.view.main.mypage.viewmodel.MyPageFavoriteRegisterFragmentViewModel
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

private const val TAG = "ssafy_pcs"

@AndroidEntryPoint
class MyPageFavoriteRegisterFragment :
    BaseFragment<FragmentAroCountryPlaceSelectBinding>(FragmentAroCountryPlaceSelectBinding::inflate) {

    lateinit var adapter: CountrySpinnerAdapter
    private var spinnerList = arrayListOf<String>()
    private val myPageFavoriteRegisterFragmentViewModel: MyPageFavoriteRegisterFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MyPageFavoriteRegisterFragmentArgs by navArgs()
        myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected = args.auroraService
        myPageFavoriteRegisterFragmentViewModel.isMeteorServiceSelected = args.meteorService

        when (myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected) {
            true -> {
                initAuroraFavoriteObserve()
                CoroutineScope(Dispatchers.IO).launch {
                    val result: Deferred<Int> = async {
                        myPageFavoriteRegisterFragmentViewModel.getCountryList()
                        0
                    }
                    result.await()
                } // End of CoroutineScope
                initSpinner(spinnerList)
            }
            false -> {
                binding.selectCountryPlaceSpinner.isInvisible = true
            }
        }
        when (myPageFavoriteRegisterFragmentViewModel.isMeteorServiceSelected) {
            true -> {
                initMeteorFavoriteObserve()
                CoroutineScope(Dispatchers.IO).launch {
                    myPageFavoriteRegisterFragmentViewModel.getMeteorCountryList()
                } // End of CoroutineScope
            }
            false -> {}
        }
        initEssentialObserve()
        initView()
    } // End of onVIewCreated

    private fun initMeteorFavoriteObserve() {
        myPageFavoriteRegisterFragmentViewModel.meteorCountryListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        myPageFavoriteRegisterFragmentViewModel.meteorCountryList.clear()
                        myPageFavoriteRegisterFragmentViewModel.meteorCountryList.addAll(it)
                        it.firstOrNull { it.interest == true }?.let { selectedMeteorCountry ->
                            myPageFavoriteRegisterFragmentViewModel.selectMeteorCountry(
                                selectedMeteorCountry
                            )
                        }
                    }
                } // End of Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                } // End of Error
                is NetworkResult.Loading -> {
                    Log.d(
                        "ssafy_pcs", "로딩 중.."
                    )
                } // End of Loading
            }
        }
    }

    private fun initEssentialObserve() {
        myPageFavoriteRegisterFragmentViewModel.setSelectServiceNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {

                    mainActivityViewModel.auroraServiceEnabled =
                        myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected
                    mainActivityViewModel.meteorShowerServiceEnabled =
                        myPageFavoriteRegisterFragmentViewModel.isMeteorServiceSelected

                    val gson = GsonBuilder().create()
                    val requestBody = JsonObject()
                    CoroutineScope(Dispatchers.IO).launch {
                        myPageFavoriteRegisterFragmentViewModel.apply {
                            requestBody.add(
                                "attractionIds",
                                gson.toJsonTree(favoriteAuroraPlaceList.map { it.placeId })
                                    .getAsJsonArray()
                            )
                            postFavoriteList(requestBody)
                        }
                    } // End of CoroutineScope
                } // End of Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                } // End of Error
                is NetworkResult.Loading -> {
                    Log.d(
                        "ssafy_pcs", "로딩 중.."
                    )
                } // End of Loading
            } // End of when
        } // End of setSelectServiceNetworkResultLiveData.observe

        myPageFavoriteRegisterFragmentViewModel.postFavoriteListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        myPageFavoriteRegisterFragmentViewModel.postFavoriteMeteorCounry()
                    }
                } // End of Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                } // End of Error
                is NetworkResult.Loading -> {
                    Log.d("ssafy_pcs", "로딩 중..")
                } // End of Loading
            } // End of when
        } // End of postFavoriteListNetworkResultLiveData.observe

        myPageFavoriteRegisterFragmentViewModel.postFavoriteMeteorCountryNetworkResultLiveData.observe(
            this.viewLifecycleOwner
        ) {
            when (it) {
                is NetworkResult.Success -> {
                    requireView().showSnackBarMessage(getString(R.string.my_page_my_favorite_modify_success_text))
                    findNavController().navigate(R.id.action_myPageFavoriteRegisterFragment_to_myPageFragment)
                } // End of Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                } // End of Error
                is NetworkResult.Loading -> {
                    Log.d("ssafy_pcs", "로딩 중..")
                } // End of Loading
            } // End of when
        } // End of postFavoriteListNetworkResultLiveData.observe

    } // End of initEssentialObserve

    private fun initAuroraFavoriteObserve() {
        myPageFavoriteRegisterFragmentViewModel.countryListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(800)
                        binding.loadingLayout.isGone = true
                    }
                    spinnerList.clear()
                    spinnerList.addAll(it.data!!)
                    adapter.notifyDataSetChanged()
                    binding.selectCountryPlaceSpinner.adapter = adapter
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }
                is NetworkResult.Loading -> {
                    binding.loadingLayout.isVisible = true
                    Log.d(
                        "ssafy_pcs", "로딩 중.."
                    )
                }
            }
        } // End of countryListLiveData.observe

        myPageFavoriteRegisterFragmentViewModel.favoriteListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    myPageFavoriteRegisterFragmentViewModel.favoriteAuroraPlaceList.clear()
                    myPageFavoriteRegisterFragmentViewModel.favoriteAuroraPlaceList.addAll(
                        it.data?.attractionInterestOrNotDTOList ?: emptyList()
                    ) // myPageFavoriteRegisterFragmentViewModel.favoriteAuroraPlaceList.addAll(it.data?.memteorInterestOrNotDTO!!)
                }// End of NetworkResult.Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("관심목록을 불러오는 것에 실패했습니다.")
                } // End of NetworkResult.Error
                is NetworkResult.Loading -> {

                } // End of NetworkResult.Loading
            } // End of when
        } // End of favoriteListNetworkResultLiveData.observe

        myPageFavoriteRegisterFragmentViewModel.placeListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    myPageFavoriteRegisterFragmentViewModel.placeList.clear()
                    myPageFavoriteRegisterFragmentViewModel.placeList.addAll(it.data!!)
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }
                is NetworkResult.Loading -> {
                    Log.d(
                        "ssafy_pcs", "로딩 중.."
                    )
                }
            } // End of when
        } // End of .placeListLiveData.observe(this.viewLifecycleOwner)

    } // End of initObserve


    private fun initView() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        } // End of cancelButton.setOnClickListener
        binding.nextButton.apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.complete_button
                )
            ) // End of setImageDrawable
            setOnClickListener {
                myPageFavoriteRegisterFragmentViewModel.apply {
                    CoroutineScope(Dispatchers.IO).launch {
                        myPageFavoriteRegisterFragmentViewModel.selectService(
                            myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected,
                            myPageFavoriteRegisterFragmentViewModel.isMeteorServiceSelected
                        ) // End of selectService
                    } // End of CoroutineScope
                } // End of myPageFavoriteRegisterFragmentViewModel.apply
            } // End of nextButton.setOnClickListener
            initComposeView()
        } // End of nextButton.apply
    } // End of InitView

    fun initSpinner(countryList: ArrayList<String>) {
        adapter =
            CountrySpinnerAdapter(
                requireContext(),
                R.layout.item_country_spinner,
                countryList
            )
        binding.selectCountryPlaceSpinner.adapter = adapter
        binding.selectCountryPlaceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.loadingLayout.isVisible = true
                    CoroutineScope(Dispatchers.Main).launch {
                        launch(Dispatchers.IO) {
                            myPageFavoriteRegisterFragmentViewModel.getPlaceList(
                                parent?.getItemAtPosition(position).toString()
                            )
                        }
                        delay(200)
                        binding.loadingLayout.isGone = true
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }// End of onItemSelectedListener
    } // End of initSpinner

    @OptIn(ExperimentalPagerApi::class)
    private fun initComposeView() {
        CoroutineScope(Dispatchers.IO).launch {
            myPageFavoriteRegisterFragmentViewModel.getFavoriteList()
        }
        binding.countryPlaceComposeview.apply {
            // Dispose of the Composition when the view's LifecycleOwner

            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val placeList = remember { myPageFavoriteRegisterFragmentViewModel.placeList }
                val meteorCountryList =
                    remember { myPageFavoriteRegisterFragmentViewModel.meteorCountryList }
                val selectedAuroraPlaceList =
                    remember { myPageFavoriteRegisterFragmentViewModel.favoriteAuroraPlaceList }
                val selectedCountry: MeteorCountry? by myPageFavoriteRegisterFragmentViewModel.favoriteMeteorCountry.observeAsState(
                    null
                )
                val pagerState = rememberPagerState()

                LaunchedEffect(pagerState) {
                    // Collect from the a snapshotFlow reading the currentPage
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        // Do something with each page change, for example:
                        // viewModel.sendPageSelectedEvent(page)
                        when (page == 1) {
                            true -> {
                                binding.selectCountryPlaceSpinner.isInvisible = true
                            }
                            false -> {
                                when (myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected) {
                                    true -> {
                                        binding.selectCountryPlaceSpinner.isVisible = true
                                    }
                                    false -> {
                                        binding.selectCountryPlaceSpinner.isInvisible = true
                                    }
                                }
                            }
                        }
                    }
                }

                // In Compose world
                MaterialTheme {


                    HorizontalPager(
                        count = 2,
                        Modifier.height(this.height.dp),
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> {
                                when (myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected) {
                                    true -> {
                                        Column(
                                            modifier = Modifier.fillMaxHeight(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            CountryPlaceChips(
                                                selectedAuroraPlaceList,
                                                myPageFavoriteRegisterFragmentViewModel
                                            )
                                            Divider(
                                                modifier = Modifier
                                                    .height(1.dp)
                                                    .padding(horizontal = 30.dp),
                                                color = Color.White
                                            ) // End of Divider
                                            CountryPlaceLazyColumn(
                                                placeList,
                                                selectedAuroraPlaceList,
                                                myPageFavoriteRegisterFragmentViewModel
                                            )
                                        } // End of Column
                                    }
                                    false -> {
                                        ServiceNotSelectedDisplayLayout(
                                            displayedText = getString(R.string.service_aurora_not_selected_textview_text),
                                            displayedInfromText = stringResource(id = R.string.my_page_not_select_service_inform_textview_text)
                                        )
                                    }
                                }
                            } // End of page -> 0
                            1 -> {
                                when (myPageFavoriteRegisterFragmentViewModel.isMeteorServiceSelected) {
                                    true -> {
                                        MeteorCountryLazyColumn(
                                            meteorCountryList = meteorCountryList,
                                            selectedCountry = selectedCountry,
                                            viewModel = myPageFavoriteRegisterFragmentViewModel
                                        )
                                    } // End of when (myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected) -> true
                                    false -> {
                                        ServiceNotSelectedDisplayLayout(
                                            displayedText = getString(
                                                R.string.service_meteor_not_selected_textview_text
                                            ),
                                            displayedInfromText = stringResource(id = R.string.my_page_not_select_service_inform_textview_text)
                                        )
                                    } // End of myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected -> false
                                } // End of when (myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected)
                            } // End of page -> 1
                        } // End of when (page)
                    } // End of HorizontalPager
                } // End of MaterialTheme
            } // End of setContent
        } // End of countryPlaceComposeview.apply
    } // End of initComposeView
} // End of MyPageFavoriteRegisterFragment
