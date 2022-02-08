package com.example.alcheringa2022

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.alcheringa2022.Model.merchModel
import android.os.Bundle
import android.content.Intent
import android.util.Log

import android.view.View
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.parser.ColorParser
import com.example.alcheringa2022.Database.DBHandler
import com.example.alcheringa2022.ui.theme.hk_grotesk
import com.google.accompanist.pager.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlin.math.absoluteValue

class MerchDescriptionActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var smallBtn: Button
    lateinit var mediumBtn: Button
    lateinit var largeBtn: Button
    lateinit var xlargeBtn: Button
    var merchSize = "S"
    lateinit var buyNow: Button
    lateinit var addToCart: Button
    lateinit var dbHandler: DBHandler
    lateinit var merchModel: merchModel
    lateinit var name: TextView
    lateinit var type: TextView
    lateinit var price: TextView
    lateinit var description: TextView
    lateinit var cart: ImageView
    lateinit var cartCountIcon: TextView
    lateinit var loaderView: LoaderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merch_description)
        cart = findViewById(R.id.cart)
        val intent = intent
        merchModel = intent.extras!!.getParcelable("item")!!
        smallBtn = findViewById(R.id.small_size)
        mediumBtn = findViewById(R.id.media_size)
        largeBtn = findViewById(R.id.large_size)
        xlargeBtn = findViewById(R.id.xlarge_size)
        buyNow = findViewById(R.id.buy_now)
        addToCart = findViewById(R.id.add_to_cart)
        name = findViewById(R.id.merch_name)
        type = findViewById(R.id.merch_type)
        price = findViewById(R.id.merch_price)
        description = findViewById(R.id.merch_description)
        cartCountIcon = findViewById(R.id.cart_count)
        loaderView = findViewById(R.id.dots_progress)
        smallBtn.setOnClickListener(this)
        mediumBtn.setOnClickListener(this)
        largeBtn.setOnClickListener(this)
        xlargeBtn.setOnClickListener(this)
        buyNow.setOnClickListener(this)
        addToCart.setOnClickListener(this)
        cartCountIcon.setOnClickListener(this)
        cart.setOnClickListener(this)
        dbHandler = DBHandler(this)

        val cv1:ComposeView= findViewById(R.id.cv1)
        cv1.setContent { horizontalpager(merModel = merchModel, context = this) }

        val backBtn:ImageButton=findViewById(R.id.backbtn)
        backBtn.setOnClickListener{finish()}


        /// setting buttons
        settingButtons()
        populateDetails()
        //dbHandler.Delete_all();

        setCartCountIcon()

        loaderView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        setCartCountIcon()
    }

    private fun setCartCountIcon(){
        val cartCount = Utility.calculateCartQuantity( applicationContext)
        if(cartCount != 0){
            cartCountIcon.visibility = View.VISIBLE
            cartCountIcon.text = cartCount.toString()
        }else{
            cartCountIcon.visibility = View.GONE
        }
    }

    private fun populateDetails() {
        name.text = merchModel.name
        type.text = merchModel.material
        price.text = "₹ " + merchModel.price + "."
        description.text = merchModel.description


    }

    private fun settingButtons() {
        if (!merchModel.small) {
            smallBtn.background = AppCompatResources.getDrawable(applicationContext, R.drawable.not_avail_btn);
            smallBtn.setTextColor(android.graphics.Color.parseColor("#707683"));
            smallBtn.isClickable=false;
        }
        if (!merchModel.large) {
            largeBtn.background = AppCompatResources.getDrawable(applicationContext, R.drawable.not_avail_btn);

            largeBtn.setTextColor(android.graphics.Color.parseColor("#707683"));

            largeBtn.isClickable=false;
        }
        if (!merchModel.medium) {
            mediumBtn.background = AppCompatResources.getDrawable(applicationContext, R.drawable.not_avail_btn);

            mediumBtn.setTextColor(android.graphics.Color.parseColor("#707683"));

            mediumBtn.isClickable=false;
        }
        if (!merchModel.xlarge) {
            xlargeBtn.background = AppCompatResources.getDrawable(applicationContext, R.drawable.not_avail_btn);

            xlargeBtn.setTextColor(android.graphics.Color.parseColor("#707683"));

            xlargeBtn.isClickable=false;
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.small_size -> {
                deselectSize()
                selectSize(smallBtn)
                merchSize = "S"
            }
            R.id.media_size -> {
                deselectSize()
                selectSize(mediumBtn)
                merchSize = "M"
            }
            R.id.large_size -> {
                deselectSize()
                selectSize(largeBtn)
                merchSize = "L"
            }
            R.id.xlarge_size -> {
                deselectSize()
                selectSize(xlargeBtn)
                merchSize = "XL"
            }
            R.id.buy_now -> {
                dbHandler.addNewitemIncart(
                    merchModel.name,
                    merchModel.price,
                    merchSize,
                    "1",
                    merchModel.image_url,
                    merchModel.material,
                    applicationContext
                )
                startActivity(Intent(applicationContext, CartActivity::class.java))
                setCartCountIcon()
            }
            R.id.add_to_cart -> {
                dbHandler.addNewitemIncart(
                    merchModel.name,
                    merchModel.price,
                    merchSize,
                    "1",
                    merchModel.image_url,
                    merchModel.material,
                    applicationContext
                )
                Toast.makeText(applicationContext, merchModel.name + " added to cart", Toast.LENGTH_SHORT).show()
                setCartCountIcon()
                //startActivity(Intent(applicationContext, CartActivity::class.java))
            }
            R.id.cart -> startActivity(Intent(applicationContext, CartActivity::class.java))
            R.id.cart_count -> startActivity(Intent(applicationContext, CartActivity::class.java))
        }
    }

    private fun selectSize(btn: Button?) {
        btn!!.background = AppCompatResources.getDrawable(applicationContext, R.drawable.merch_size_btn_selected)
        btn.setTextColor(resources.getColor(R.color.Black))

    }

    private fun deselectSize() {
        smallBtn.background = AppCompatResources.getDrawable(applicationContext, R.drawable.merch_size_btn_deselect)
        mediumBtn.background = AppCompatResources.getDrawable(applicationContext, R.drawable.merch_size_btn_deselect)
        largeBtn.background = AppCompatResources.getDrawable(applicationContext, R.drawable.merch_size_btn_deselect)
        xlargeBtn.background = AppCompatResources.getDrawable(applicationContext, R.drawable.merch_size_btn_deselect)
        smallBtn.setTextColor(resources.getColor(R.color.White))
        mediumBtn.setTextColor(resources.getColor(R.color.White))
        largeBtn.setTextColor(resources.getColor(R.color.White))
        xlargeBtn.setTextColor(resources.getColor(R.color.White))
        Log.d("abcd", "deselect_size:")
        settingButtons();
    }


    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun horizontalpager(merModel: merchModel, context: Context) {
        val images = merModel.images
        val videoUrl = merModel.video_url

        Column {
            val pagerState = rememberPagerState()

            //val videoUrl = "https://cdn.videvo.net/videvo_files/video/free/2020-05/large_watermarked/3d_ocean_1590675653_preview.mp4"

            HorizontalPager(
                count = images.size + 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 20.dp)
            ){ page ->


                Card(
                    Modifier
                        .graphicsLayer {
                            // Calculate the absolute offset for the current page from the
                            // scroll position. We use the absolute value which allows us to mirror
                            // any effects for both directions
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            // We animate the scaleX + scaleY, between 85% and 100%
                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            // We animate the alpha, between 50% and 100%
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                ) {
                    if (page < images.size) {
                        Box {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = 5.dp
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color.LightGray)
                                        .padding(20.dp)
                                        .height(400.dp)
                                        .fillMaxWidth(), contentAlignment = Alignment.Center
                                ) {
                                    GlideImage(
                                        imageModel = images[page],
                                        contentDescription = "artist",
                                        modifier = Modifier
                                            .width(348.dp)
                                            .height(360.dp),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Crop,
                                        shimmerParams = ShimmerParams(
                                            baseColor = Color.Black,
                                            highlightColor = Color.LightGray,
                                            durationMillis = 350,
                                            dropOff = 0.65f,
                                            tilt = 20f
                                        ), failure = {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(473.dp), contentAlignment = Alignment.Center
                                            ) {
                                                Column(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .wrapContentHeight(),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Image(
                                                        modifier = Modifier
                                                            .width(60.dp)
                                                            .height(60.dp),
                                                        painter = painterResource(
                                                            id = R.drawable.ic_sad_svgrepo_com
                                                        ),
                                                        contentDescription = null
                                                    )
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                    Text(
                                                        text = "Image Request Failed",
                                                        style = TextStyle(
                                                            color = Color(0xFF747474),
                                                            fontFamily = hk_grotesk,
                                                            fontWeight = FontWeight.Normal,
                                                            fontSize = 12.sp
                                                        )
                                                    )
                                                }
                                            }

                                        }

                                    )
                                }
                            }
                        }
                    } else {
                        Box {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = 5.dp
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color.Black)
                                        .padding(8.dp)
                                        .height(400.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val mediaItem = MediaItem.fromUri(videoUrl)
                                    val exoPlayer = SimpleExoPlayer.Builder(context).build().apply {
                                        setMediaItem(mediaItem)
                                        playWhenReady = true
                                        prepare()
                                        play()
                                    }
                                    DisposableEffect(
                                        AndroidView(factory = { PlayerView(context).apply { player = exoPlayer } })
                                    )
                                    { onDispose { exoPlayer.release() } }

                                }
                            }
                        }
                    }
                }
            }
            HorizontalPagerIndicator(
                    pagerState = pagerState,
                    activeColor= colorResource(id = R.color.textGray),
                    inactiveColor = colorResource(id = R.color.darkGray),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
            )
        }

    }
}