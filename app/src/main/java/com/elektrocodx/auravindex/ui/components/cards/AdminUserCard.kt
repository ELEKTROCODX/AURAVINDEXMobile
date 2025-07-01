package com.elektrocodx.auravindex.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektrocodx.auravindex.R
import com.elektrocodx.auravindex.ui.theme.PurpleC
import com.elektrocodx.auravindex.utils.constants.URLs.IMG_url
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun AdminUserCard(
    navController: NavController,
    userViewModel: UserViewModel,
    localSettingViewModel: LocalSettingViewModel,
    userId: String
) {
    val user = userViewModel.user.observeAsState()
    val colors = androidx.compose.material3.MaterialTheme.colorScheme
    val settings = localSettingViewModel.settings.collectAsState()
    LaunchedEffect(Unit) {
        userViewModel.getUserById(settings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""), userId)
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        val imageUrl = IMG_url.trimEnd('/') + "/" + user?.value?.user_img?.trimStart('/')
        Text(
            text = "${user?.value?.name} ${user?.value?.last_name}",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            GlideImage(
                imageModel = { imageUrl },
                modifier = Modifier
                    .widthIn(max=200.dp)
                    .heightIn(max=300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .align(Alignment.Center),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop
                ),
                loading = {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                },
                failure = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Default img",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .align(Alignment.Center)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "User Details",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF572365)),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Email: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = user.value?.email ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Biography: ")
                        withStyle(SpanStyle(fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Normal)) {
                            append(user.value?.biography?: "Not available")
                        }
                    },
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365), textAlign = TextAlign.Justify)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Gender: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = user.value?.gender?.name ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Birthdate: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = formatUtcToLocalWithDate(user.value?.birthdate),
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Role: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365)),
                )
                Text(
                    text = user.value?.role?.name ?: "Not available",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {  /* Acción para "Editar" */ },
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = PurpleC),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Edit",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
            }
            Button(
                onClick = { /* Acción para "Cancel"*/  },
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = colors.error),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Delete",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
            }
        }
    }
}

