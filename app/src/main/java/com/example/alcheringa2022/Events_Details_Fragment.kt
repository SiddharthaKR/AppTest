package com.example.alcheringa2022

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.alcheringa2022.Database.ScheduleDatabase
import com.example.alcheringa2022.Model.addNewItem
import com.example.alcheringa2022.Model.removeAnItem
import com.example.alcheringa2022.Model.viewModelHome
import com.example.alcheringa2022.databinding.ActivityEventDetailsBinding
import com.example.alcheringa2022.ui.theme.clash
import com.example.alcheringa2022.ui.theme.hk_grotesk
import com.example.alcheringa2022.ui.theme.orangeText
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import java.util.*

class Events_Details_Fragment : Fragment() {
    private lateinit var fgman: FragmentManager
    lateinit var args:Bundle
    val viewModelHome: viewModelHome by activityViewModels()
    lateinit var binding:ActivityEventDetailsBinding
    lateinit var eventfordes: eventWithLive
    lateinit var similarlist:MutableList<eventWithLive>
    private lateinit var  scheduleDatabase: ScheduleDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args=requireArguments()
        eventfordes= viewModelHome.allEventsWithLive.filter { data-> data.eventdetail.artist== args.getString("Artist") }[0]
        fgman=parentFragmentManager
        similarlist= mutableListOf<eventWithLive>()
        similarlist.addAll(
        viewModelHome.allEventsWithLive.filter{ data-> data.eventdetail.category.replace("\\s".toRegex(), "").uppercase()== eventfordes.eventdetail.category.replace("\\s".toRegex(), "").uppercase()})
        similarlist.remove(eventfordes)
        scheduleDatabase= ScheduleDatabase(context)


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ActivityEventDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dotsProgress.visibility=View.GONE
        binding.artistname.text=eventfordes.eventdetail.artist.uppercase()
        binding.backbtn2.setOnClickListener{requireActivity().onBackPressed()}

        binding.cvevdetail.setContent{
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xff00010C))
                    .verticalScroll(rememberScrollState())) {
                Defaultimg(eventWithLive = eventfordes)
                Bottomview( eventWithLive = eventfordes)
                similarEvents(heading = "SIMILAR EVENTS",similarlist)
                Spacer(modifier = Modifier.height(0.dp))
            }
        }


    }



    @Composable
    fun Defaultimg(eventWithLive: eventWithLive) {

        Box(
            modifier = Modifier
                .height(473.dp)
                .fillMaxWidth()
        ) {
            GlideImage(
                imageModel = eventWithLive.eventdetail.imgurl,
                contentDescription = "artist",
                modifier= Modifier
                    .fillMaxWidth()
                    .height(473.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                shimmerParams = ShimmerParams(
                    baseColor = Color.Black,
                    highlightColor = orangeText,
                    durationMillis = 350,
                    dropOff = 0.65f,
                    tilt = 20f
                ),failure = {
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .height(473.dp), contentAlignment = Alignment.Center) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ), startY = with(LocalDensity.current){100.dp.toPx()}
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp), contentAlignment = Alignment.BottomStart
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = eventWithLive.eventdetail.artist,
                        color = Color.White,
                        fontWeight = FontWeight.W700,
                        fontSize = 78.sp,
                        fontFamily = FontFamily(Font(R.font.morganitemedium))
                    )
                    Spacer(modifier = Modifier.height(11.dp))
                    Text(
                        text = eventWithLive.eventdetail.category,
                        style = TextStyle(
                            color = colorResource(id = R.color.textGray),
                            fontFamily = clash,
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(11.dp))
                    if (eventWithLive.isLive.value){
                    Box(modifier = Modifier
                        .width(68.dp)
                        .height(21.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xffC80915)), contentAlignment = Alignment.Center){
                        Text(
                            text = "⬤ LIVE ",
                            color = Color.White,
                            fontFamily = hk_grotesk, fontWeight = FontWeight.W500,
                            fontSize = 12.sp
                        )
                    }
                    }


                }

            }


        }
    }
    @Composable
    fun Bottomview(eventWithLive:eventWithLive){
        var isadded=remember{ mutableStateOf(false)}
        isadded.value=viewModelHome.OwnEventsLiveState.any { data-> data.artist==eventWithLive.eventdetail.artist }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), Arrangement.Start)
            {
                Image(
                    painter = if (eventWithLive.eventdetail.mode.contains("ONLINE")) {
                        painterResource(id = R.drawable.online)
                    } else {
                        painterResource(id = R.drawable.onground)
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop

                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = eventWithLive.eventdetail.mode,
                    style = TextStyle(
                        color = colorResource(id = R.color.textGray),
                        fontFamily = hk_grotesk,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), Arrangement.Start)
            {
                Image(
                    painter = painterResource(id = R.drawable.schedule),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${eventWithLive.eventdetail.starttime.date} Feb, ${if(eventWithLive.eventdetail.starttime.hours>12)"${eventWithLive.eventdetail.starttime.hours-12}" else eventWithLive.eventdetail.starttime.hours}${if (eventWithLive.eventdetail.starttime.min!=0) ":${eventWithLive.eventdetail.starttime.min}" else ""} ${if (eventWithLive.eventdetail.starttime.hours>=12)"PM" else "AM"} ",
                    style = TextStyle(
                        color = colorResource(id = R.color.textGray),
                        fontFamily = clash,
                        fontWeight = FontWeight.W500,
                        fontSize = 20.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text =eventfordes.eventdetail.descriptionEvent ,
                fontFamily = hk_grotesk,
                fontWeight = FontWeight.W600,
                color = Color(0xffC7CCD1),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(36.dp))
            if (eventWithLive.isLive.value) {
                Button(
                    onClick = { /*TODO*/ },
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        orangeText
                    )
                ) {
                    Text(
                        text = "Join Event",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        fontFamily = clash,
                        color = Color.White
                    )

                }
            }
            if (!eventWithLive.isLive.value){
                Button(
                    onClick = { /*TODO*/ },
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                       Color(0xff4A4949)
                    )
                ) { val c=Calendar.getInstance()
                    if( (c.get(Calendar.YEAR)>2022) or
                        ((c.get(Calendar.YEAR)==2022) and
                                (c.get(Calendar.MONTH)> Calendar.FEBRUARY)) or
                        ((c.get(Calendar.YEAR)==2022) and
                                (c.get(Calendar.MONTH)== Calendar.FEBRUARY) and
                                (c.get(Calendar.DATE)> eventWithLive.eventdetail.starttime.date)) or
                        ((c.get(Calendar.YEAR)==2022) and
                                (c.get(Calendar.MONTH)== Calendar.FEBRUARY) and
                                (c.get(Calendar.DATE)== eventWithLive.eventdetail.starttime.date)and
                                ( ((eventWithLive.eventdetail.starttime.hours*60 + eventWithLive.eventdetail.durationInMin))
                                        <((c.get(Calendar.HOUR_OF_DAY)*60) + c.get(Calendar.MINUTE)) ))

                    ){ Text(text="Event Finished!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        fontFamily = clash,
                        color = Color(0xffA3A7AC)
                    )}
                    else if (c.get(Calendar.DATE)==eventWithLive.eventdetail.starttime.date){
                        Text(
                            text = "Event will be available on  ${if (eventWithLive.eventdetail.starttime.hours > 12)"${eventWithLive.eventdetail.starttime.hours - 12}" else eventWithLive.eventdetail.starttime.hours}${if (eventWithLive.eventdetail.starttime.min != 0) ":${eventWithLive.eventdetail.starttime.min}" else ""} ${if (eventWithLive.eventdetail.starttime.hours >= 12) "PM" else "AM"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            fontFamily = clash,
                            color = Color(0xffA3A7AC)
                        )
                    }
                    else{
                    Text(
                        text = "Event will be available on day ${eventWithLive.eventdetail.starttime.date-10}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        fontFamily = clash,
                        color = Color(0xffA3A7AC)
                    )

                    }
                }

            }
            Spacer(modifier = Modifier.height(12.dp))
            if (isadded.value) {
                Button(
                    onClick = {
                         isadded.value= false
                              viewModelHome.OwnEventsWithLive.removeAnItem(eventWithLive.eventdetail)
                        scheduleDatabase.DeleteItem(eventWithLive.eventdetail.artist)
                    },
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xff2B2B2B))
                ) {
                    Text(
                        text = "Remove from My Schedule",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        fontFamily = clash,
                        color = Color.White
                    )
                }
            }
            else{
                Button(
                    onClick = { isadded.value= true
                        viewModelHome.OwnEventsWithLive.addNewItem(eventWithLive.eventdetail)
                        scheduleDatabase.addEventsInSchedule(
                            eventWithLive.eventdetail,
                            context
                        )
                    },
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xff2B2B2B))
                ) {
                    Text(
                        text = "Add to My Schedule",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        fontFamily = clash,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))









        }
    }

    @Composable
    fun similarEvents(heading: String, similarlist: MutableList<eventWithLive>) {
        Box(modifier =Modifier.padding(horizontal = 20.dp, vertical = 12.dp) ){Text(text = heading.uppercase(
            Locale.getDefault()), fontWeight = FontWeight.W500, fontSize = 18.sp, fontFamily = clash, color = Color.White)}
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 20.dp)
        ) { items(similarlist)
        {dataEach -> context?.let { Event_card(eventdetail = dataEach, viewModelHome, it, fgman) } } }

        Spacer(modifier = Modifier.height(5.dp))
    }


}