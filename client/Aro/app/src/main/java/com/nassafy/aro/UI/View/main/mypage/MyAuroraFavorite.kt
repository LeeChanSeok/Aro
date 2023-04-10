package com.nassafy.aro.ui.view.main.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.ui.view.custom.NanumSqaureFont
import com.nassafy.aro.ui.view.custom.ServiceNotSelectedDisplayLayout
import com.nassafy.aro.ui.view.main.mypage.viewmodel.MyPageFragmentViewModel

@Composable
fun MyAuroraFavorite(
    auroraFavoriteList: MutableList<PlaceItem>,
    myPageFragmentViewModel: MyPageFragmentViewModel
) {
    when (auroraFavoriteList.size > 0) {
        true -> {
            LazyColumn(
                Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight()
            ) {
                items(auroraFavoriteList) {
                    val imageLoader = ImageLoader.Builder(LocalContext.current)
                        .componentRegistry {
                            add(SvgDecoder(LocalContext.current))
                        }
                        .build()
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        )
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth(1f)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxHeight(0.95f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                                    val painter =
                                        rememberImagePainter(
                                            it.stamp,
                                            builder = { }) // End of rememberImagePainter
                                    Image(
                                        painter = painter,
                                        contentDescription = "SVG Image",
                                        modifier = Modifier
                                            .weight(2f),
                                        contentScale = ContentScale.FillWidth,
                                    ) // End of Image
                                } // End of CompositionLocalProvider
                                Column(
                                    modifier = Modifier
                                        .weight(7f)
                                        .padding(start = 20.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = it.placeName,
                                            style = TextStyle(
                                                fontFamily = NanumSqaureFont,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp
                                            ),
                                            color = Color.White
                                        ) // End of Text
                                    } // End of Box
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = it.description,
                                            style = TextStyle(
                                                fontFamily = NanumSqaureFont,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 12.sp
                                            ),
                                            color = Color.White
                                        )
                                    } // End of Box
                                } // End of Column
                                IconButton(onClick = {
                                    myPageFragmentViewModel.deleteFavorite(it.interestId)
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
                            ) // End of Divider
                        } // End of Column
                    } // End of Card
                } //End of items
            } // End of LazyColum
        } // End of when (auroraFavoriteList.size > 0) -> true
        false -> {
            ServiceNotSelectedDisplayLayout(
                stringResource(R.string.my_page_not_select_aurora_favorite_place_text),
                stringResource(id = R.string.my_page_not_select_aurora_favorite_place_inform_text)
            )
        } // End of when (auroraFavoriteList.size > 0) -> false
    } // End of when (auroraFavoriteList.size > 0)

}