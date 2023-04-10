package com.nassafy.aro.ui.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.MeteorCountry
import com.nassafy.aro.data.dto.UserWholeData
import com.nassafy.aro.databinding.FragmentMyPageBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.custom.NanumSqaureFont
import com.nassafy.aro.ui.view.custom.ServiceNotSelectedDisplayLayout
import com.nassafy.aro.ui.view.dialog.OkDialog
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.ui.view.main.MainActivityViewModel
import com.nassafy.aro.ui.view.main.mypage.viewmodel.MyPageFragmentViewModel
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val myPageFragmentViewModel: MyPageFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val main = requireActivity() as MainActivity
        main.closeDrawer()
    } // End of onCreate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        CoroutineScope(Dispatchers.IO).launch {
            myPageFragmentViewModel.getSelectedServiceList()
        } // End of CoroutineScope
        initView()
    } // End of onViewCreated


    private fun initObserve() {

        myPageFragmentViewModel.nicknameLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    mainActivityViewModel.nickname = binding.userNicknameEdittext.text.toString()

                    mainActivityViewModel.setUserWholeData(
                        UserWholeData(
                            binding.userNicknameEdittext.text.toString(),
                            mainActivityViewModel.email,
                            mainActivityViewModel.alarmOption,
                            mainActivityViewModel.auroraDisplayOption,
                            mainActivityViewModel.cloudDisplayOption,
                            mainActivityViewModel.auroraServiceEnabled,
                            mainActivityViewModel.meteorShowerServiceEnabled,
                        )
                    )

                    binding.nicknameChangeSaveAlertImagebutton.isVisible = true
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        binding.progressBar.isVisible = false
                        binding.nicknameChangeSaveAlertImagebutton.isGone = true
                    }
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("닉네임 재설정에 실패했습니다.")
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            } // End of when
        } // End of nicknameLiveData.observe

        myPageFragmentViewModel.getSelectedServiceNetworResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    myPageFragmentViewModel.auroraService = it.data!!.auroraService
                    myPageFragmentViewModel.meteorService = it.data!!.meteorService
                    when (it.data!!.auroraService || it.data!!.meteorService) {
                        true -> {
                            binding.serviceSelectedGroup.isVisible = true
                            binding.serviceNotSelectedGroup.isVisible = false
                            initComposeView()
                        }
                        false -> {
                            binding.serviceSelectedGroup.isVisible = false
                            binding.serviceNotSelectedGroup.isVisible = true
                        }
                    } // End of when
                    binding.progressBar.isVisible = false
                } // End of NetworkResult.Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("선택한 서비스를 불러오는데 실패했습니다.")
                } // End of NetworkResult.Error
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                } // End of NetworkResult.Loading
            } // End of when
        } // End of getSelectedServiceNetworResultLiveData.observe

        myPageFragmentViewModel.favoriteListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    myPageFragmentViewModel.favoriteAuroraPlaceList.clear()
                    myPageFragmentViewModel.favoriteAuroraPlaceList.addAll(
                        it.data?.attractionInterestOrNotDTOList ?: emptyList()
                    )
                    binding.progressBar.isVisible = false
//                    myPageFragmentViewModel.favoriteMeteorPlaceList.addAll(it.data?.memteorInterestOrNotDTO!!)
                }// End of NetworkResult.Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("관심목록을 불러오는데 실패했습니다.")
                } // End of NetworkResult.Error
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                } // End of NetworkResult.Loading
            } // End of when
        } // End of favoriteListNetworkResultLiveData.observe

        myPageFragmentViewModel.deleteFavoriteNetworkResultLiveData.observe(this.viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    myPageFragmentViewModel.favoriteAuroraPlaceList.removeAll {
                        it.interestId.equals(
                            result.data!!
                        )
                    }
                    binding.progressBar.isVisible = false
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("관심지역 삭제에 실패했습니다.")
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            } // End of when
        } // End of deleteFavoriteNetworkResultLiveData.observe

        myPageFragmentViewModel.meteorCountryListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    binding.progressBar.isVisible = false
                    it.data?.let { meteorCountryList ->
                        meteorCountryList.firstOrNull { meteorCountry ->
                            meteorCountry.interest
                        }?.let { favoriteMeteorCountry ->
                            myPageFragmentViewModel.setFavoriteMeteorCountry(favoriteMeteorCountry)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    binding.progressBar.isVisible = false
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        } // End of meteorCountryListNetworkResultLiveData.observe

        myPageFragmentViewModel.clearFavoriteMeteorCountryNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    myPageFragmentViewModel.setFavoriteMeteorCountry(null)
                }
                is NetworkResult.Error -> {
                    binding.progressBar.isVisible = false
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        } // End of meteorCountryListNetworkResultLiveData.observe

    } // End of initObserve

    private fun initView() {
        binding.userNicknameEdittext.setText(mainActivityViewModel.nickname)
        binding.userEmailTextview.text = mainActivityViewModel.email
        binding.nicknameChangeImagebutton.apply {
            setOnClickListener {
                when (isSelected) {
                    true -> {// 재설정 로직
                        val afterChangedNickname = binding.userNicknameEdittext.text.toString()
                        when (mainActivityViewModel.nickname != afterChangedNickname) {
                            true -> {
                                when (afterChangedNickname.length in 3..10) {
                                    true -> {
                                        when (afterChangedNickname.contains(" ") || afterChangedNickname.contains(
                                            "\n"
                                        )) {
                                            true -> {
                                                val okDialog = OkDialog(
                                                    getString(R.string.my_page_nickname_modify_error_dialog_title_textview_text),
                                                    getString(R.string.nickname_validate_fail_blank_edittext_text),
                                                    getString(R.string.dialog_confirm_button_text)
                                                )
                                                okDialog.show(childFragmentManager, "OkDialog")
                                                binding.userNicknameEdittext.setText(
                                                    mainActivityViewModel.nickname
                                                )
                                            }
                                            false -> {
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    myPageFragmentViewModel.changeNickname(binding.userNicknameEdittext.text.toString())
                                                }
                                            }
                                        }
                                    }
                                    false -> {
                                        val okDialog = OkDialog(
                                            getString(R.string.my_page_nickname_modify_error_dialog_title_textview_text),
                                            getString(R.string.my_page_nickname_modify_error_dialog_inform_textview_text),
                                            getString(R.string.dialog_confirm_button_text)
                                        )
                                        okDialog.show(
                                            childFragmentManager, "OkDialog"
                                        )
                                        binding.userNicknameEdittext.setText(
                                            mainActivityViewModel.nickname
                                        )
                                    }
                                } // End of afterChangedNickname.length in 3..10
                            } // End of isSelected == true
                            false -> {}
                        }  // End of when(mainActivityViewModel.nickname != binding.userNicknameEdittext.text.toString())
                    } // End of isSelected == true
                    false -> {} // End of isSelected == false
                } // End of when(isSelected)
                isSelected = !isSelected
                binding.userNicknameEdittext.isEnabled = isSelected
            }
        } // End of initView
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        } // End of cancelButton.setOnClickListener
        binding.serviceRegistButton.setOnClickListener {
            moveToServiceRegisterFragment()
        } // End of serviceRegistButton.setOnClickListener
        binding.myFavoriteAddImageButton.setOnClickListener {
            moveToFavoriteRegisterFragment()
        } // End of myFavoriteAddImageButton.setOnClickListener
        binding.serviceModifyButton.setOnClickListener {
            moveToServiceRegisterFragment()
        } // End of serviceModifyButton.setOnClickListener
    } // End of initVIew

    fun moveToServiceRegisterFragment() {
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageServiceRegisterFragment(
            auroraService = myPageFragmentViewModel.auroraService,
            meteorService = myPageFragmentViewModel.meteorService,
        )
        findNavController().navigate(action)
    } // End of moveToServiceRegisterFragment

    fun moveToFavoriteRegisterFragment() {
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageFavoriteRegisterFragment(
            auroraService = myPageFragmentViewModel.auroraService,
            meteorService = myPageFragmentViewModel.meteorService,
        )
        findNavController().navigate(action)
    } // End of moveToFavoriteRegisterFragment

    @OptIn(ExperimentalPagerApi::class)
    fun initComposeView() {
        CoroutineScope(Dispatchers.IO).launch {
            myPageFragmentViewModel.getFavoriteList()
            myPageFragmentViewModel.getMeteorCountryList()
        } // End of CoroutineScope
        binding.myFavoriteComposeview.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val auroraFavoriteList =
                    remember { myPageFragmentViewModel.favoriteAuroraPlaceList }
                val favoriteMeteorCountry: MeteorCountry? by myPageFragmentViewModel.favoriteMeteorCountry.observeAsState(
                    null
                )
                // In Compose world

                val favoriteListNetworkResultLiveData by myPageFragmentViewModel.favoriteListNetworkResultLiveData.observeAsState()

                LaunchedEffect(myPageFragmentViewModel.favoriteAuroraPlaceList) {
                    auroraFavoriteList.clear()
                    auroraFavoriteList.addAll(myPageFragmentViewModel.favoriteAuroraPlaceList)
                }

                MaterialTheme {
                    HorizontalPager(count = 2, modifier = Modifier.height(this.height.dp)) { page ->
                        when (page) {
                            0 -> {
                                when (favoriteListNetworkResultLiveData) {
                                    null -> {}
                                    is NetworkResult.Loading -> {}
                                    else -> {
                                        when (myPageFragmentViewModel.auroraService) {
                                            true -> {
                                                MyAuroraFavorite(
                                                    auroraFavoriteList = auroraFavoriteList,
                                                    myPageFragmentViewModel = myPageFragmentViewModel
                                                )
                                            } // End of true
                                            false -> {
                                                ServiceNotSelectedDisplayLayout(
                                                    getString(R.string.service_aurora_not_selected_textview_text),
                                                    stringResource(id = R.string.my_page_not_select_service_inform_textview_text)
                                                )
                                            } // End of false
                                        } // End of when
                                    } // End of else
                                } // End of when(temp)
                            } // End of when(page) : page -> 0
                            1 -> {
                                when (myPageFragmentViewModel.meteorService) {
                                    true -> {
                                        when (favoriteMeteorCountry) {
                                            null -> {
                                                ServiceNotSelectedDisplayLayout(
                                                    getString(R.string.my_page_not_select_meteor_favorite_country_text),
                                                    getString(R.string.my_page_not_select_meteor_favorite_country_inform_text)
                                                )
                                            }
                                            else -> {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth(0.9f)
                                                        .fillMaxHeight()
                                                ) {
                                                    Card(
                                                        shape = RoundedCornerShape(4.dp),
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .height(100.dp),
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = Color.Transparent,
                                                        )
                                                    ) {
                                                        Column(
                                                            Modifier
                                                                .fillMaxWidth(1f),
                                                        ) {
                                                            Row(
                                                                modifier = Modifier.fillMaxHeight(
                                                                    0.95f
                                                                ),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Box(
                                                                    modifier = Modifier.weight(2f),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Text(
                                                                        text = favoriteMeteorCountry!!.countryEmoji,
                                                                        style = TextStyle(
                                                                            fontFamily = NanumSqaureFont,
                                                                            fontWeight = FontWeight.Normal,
                                                                            fontSize = 24.sp
                                                                        )
                                                                    )
                                                                }
                                                                Box(
                                                                    modifier = Modifier
                                                                        .weight(7f),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Text(
                                                                        text = favoriteMeteorCountry!!.countryName,
                                                                        style = TextStyle(
                                                                            fontFamily = NanumSqaureFont,
                                                                            fontWeight = FontWeight.Bold,
                                                                            fontSize = 20.sp
                                                                        ),
                                                                        color = Color.White
                                                                    ) // End of Text
                                                                } // End of Box
                                                                IconButton(onClick = {
                                                                    myPageFragmentViewModel.clearFavoriteMeteorCountry()
                                                                }) {
                                                                    Icon(
                                                                        imageVector = Icons.Outlined.Close,
                                                                        contentDescription = "checked",
                                                                        tint = colorResource(id = R.color.light_dark_gray),
                                                                    ) // End of Icon
                                                                } // End o f IconButton
                                                            } // End of Row
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            Divider(
                                                                modifier = Modifier
                                                                    .fillMaxWidth(1f)
                                                                    .height(2.dp),
                                                                color = colorResource(id = R.color.main_app_color),
                                                            )
                                                        } // End of Column
                                                    } // End of Card
                                                }
                                            }
                                        }
                                    } // End of true
                                    false -> {
                                        ServiceNotSelectedDisplayLayout(
                                            getString(R.string.service_meteor_not_selected_textview_text),
                                            stringResource(id = R.string.my_page_not_select_service_inform_textview_text)
                                        )
                                    } // End of false
                                } // End of when
                            } // End of when(page) : page -> 1
                        } // End of when(page)
                    } // End of HorizontalPager
                } // End of MaterialTheme
            } // End of setContent
        } // ENd of binding.myFavoriteComposeview.apply
    } // End of initComposeView
} // End of MyPageFragment