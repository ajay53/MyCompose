package com.goazzi.mycompose.ui.pixabay

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.goazzi.mycompose.R
import com.goazzi.mycompose.model.pixabay.HitMedia
import com.goazzi.mycompose.ui.theme.onErrorLight
import com.goazzi.mycompose.util.MediaSearchTypeEnum
import com.goazzi.mycompose.viewmodel.ApiState
import com.goazzi.mycompose.viewmodel.MainViewModel

private const val TAG = "PixabayStateful"

@Composable
fun PixabayStateful(
    viewModel: MainViewModel,
    onMediaClick: () -> Unit
) {
    /*
    Button(onClick = {
        viewModel.searchMedia(params = mapOf("q" to "yellow+flowers", "image_type" to "photo"))
    }) {
        Text(text = "Search Pixabay")
    }*/

    val mediaSearchType by viewModel.mediaSearchType.collectAsStateWithLifecycle()


    val pixabayAPIState by viewModel.mediaAPIState.collectAsStateWithLifecycle()




    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar { query ->

            val searchText = query.replace(" ", "+")

            when (mediaSearchType) {
                MediaSearchTypeEnum.IMAGE -> {
                    viewModel.searchMedia(
                        params = mapOf(
//                            "q" to searchText,
                            "q" to "sports+cars", "image_type" to "photo"
                        )
                    )
                }

                MediaSearchTypeEnum.VIDEO -> {
                    viewModel.searchMedia(
                        apiPath = "videos",
//                        params = mapOf("q" to searchText, "image_type" to "animation")
                        params = mapOf("q" to "sports+cars", "image_type" to "animation")
                    )
                }
            }

//            viewModel.fetchPixabayMedia(query)
        }

        when (val currentState = pixabayAPIState) {

            is ApiState.Error -> {

            }

            ApiState.Idle -> {}
            ApiState.Loading -> {}
            is ApiState.LoadingMore -> {

            }

            is ApiState.Success -> {

                val mediaList = currentState.data.hits

                mediaList?.let {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(
                            items = mediaList,
                            contentType = { _, item -> item } // Optional: helps Compose with item reuse
                        ) { index, media ->
                            MediaListItem(
                                item = media,
                                onMediaClick = onMediaClick
                            )
                            /*when (media) {
                                is HitMedia.Image -> ImageItem(media)
                                is HitMedia.Video -> VideoItem(media)
                            }*/
                        }
                    }
                }

//                TAG.d("PixabayStateful: mediaList: $mediaList")
                /*mediaList?.let { list ->

                    list.forEach { hitMedia->
                        when(hitMedia){
                            is HitMedia.Image -> {
                                TAG.d("PixabayStateful: media: Image")
                            }
                            is HitMedia.Video -> {
                                TAG.d("PixabayStateful: media: Video")
                            }
                        }

                    }
                }*/

            }
        }

    }
}

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var query by remember { mutableStateOf("") }

    TextField(
        value = query,
        onValueChange = { query = it },
        placeholder = { Text("Search for photos and videos...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(56.dp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search // ✅ Enables 'Search' button on keyboard
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearch(query) // ✅ Calls search function when Enter/Search key is pressed
            }
        ),
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon") // ✅ Adds Search icon
        }
    )
}

@Composable
fun MediaListItem(
    item: HitMedia,
//    itemType: HitMedia,
    onMediaClick: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val itemID = when (item) {
        is HitMedia.Image -> (item as HitMedia.Image).id!!
        is HitMedia.Video -> (item as HitMedia.Video).id!!
    }

    val thumbnailURL = when (item) {
        is HitMedia.Image -> (item as HitMedia.Image).previewURL
        is HitMedia.Video -> (item as HitMedia.Video).videos?.medium?.thumbnail
    }

    val placeHolder = when (item) {
        is HitMedia.Image -> R.drawable.ic_photo
        is HitMedia.Video -> R.drawable.ic_video
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(enabled = true, onClick = {
                viewModel.updateClickedMedia(item)
                onMediaClick()
            }),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(thumbnailURL)
//                    .data("https://via.placeholder.com/150")
                .crossfade(true)
                .placeholder(R.drawable.ic_photo)
                .build(),
            placeholder = painterResource(placeHolder),
            error = painterResource(placeHolder),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ImageItem(image: HitMedia.Image) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        AsyncImage(
            model = image.pageURL,
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun VideoItem(video: HitMedia.Video) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(video.pageURL!!))
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )
    }
}