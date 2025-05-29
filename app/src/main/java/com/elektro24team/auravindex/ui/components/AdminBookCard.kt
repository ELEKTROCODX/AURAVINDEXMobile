package com.elektro24team.auravindex.ui.components

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.utils.functions.formatUtcToLocalWithDate
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.LoanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun AdminBookCard(
    navController: NavController,
    bookViewModel: BookViewModel,
    loanViewModel: LoanViewModel,
    localSettingViewModel: LocalSettingViewModel,
    bookId: String
) {
    val book = bookViewModel.book.observeAsState()
    val colors = androidx.compose.material3.MaterialTheme.colorScheme
    val bookLoans = loanViewModel.bookLoans.observeAsState()
    val localSettings by localSettingViewModel.settings.collectAsState()
    LaunchedEffect(Unit) {
        bookViewModel.loadBook(bookId, true)
        loanViewModel.loadBookLoans(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""), bookId)
    }
    ObserveTokenExpiration(bookViewModel, navController, localSettingViewModel)
    ObserveTokenExpiration(loanViewModel, navController, localSettingViewModel)
    ObserveInsufficientPermissions(bookViewModel, navController)
    ObserveInsufficientPermissions(loanViewModel, navController)
    ObserveError(bookViewModel)
    ObserveSuccess(bookViewModel)
    ObserveError(loanViewModel)
    ObserveSuccess(loanViewModel)

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .shadow(12.dp, shape = RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        val imageUrl = IMG_url.trimEnd('/') + "/" + book?.value?.book_img?.trimStart('/')

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = book?.value?.title ?: "Title",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF572365)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    imageModel = { imageUrl },
                    modifier = Modifier
                        .heightIn(max = 280.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    loading = { CircularProgressIndicator() },
                    failure = {
                        Image(
                            painter = painterResource(id = R.drawable.logo_app),
                            contentDescription = "Default img",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .shadow(8.dp)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Book Details",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF572365)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            @Composable
            fun detailRow(label: String, value: String?) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "$label:",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF572365))
                        )
                        Text(
                            text = value ?: "Not available",
                            style = TextStyle(fontSize = 16.sp, color = Color.Black),
                            textAlign = TextAlign.End
                        )
                    }
                    Divider(color = Color.LightGray, thickness = 1.dp)
                }
            }

            detailRow("Summary", book.value?.summary)
            detailRow("Authors", book.value?.authors?.joinToString(", ") { "${it.name} ${it.last_name}" })
            detailRow("Editorial", book.value?.editorial?.name)
            detailRow("Collection", book.value?.book_collection?.name)
            detailRow("Genres", book.value?.genres?.joinToString(", "))
            detailRow("Edition", book.value?.edition)
            detailRow("Language", book.value?.language)
            detailRow("Location", book.value?.location)
            detailRow("Status", book.value?.book_status?.book_status)
            if (book.value?.book_status?.book_status?.lowercase() == "lent") {
                detailRow("Lent to", "Some User")
            }
            detailRow("Classification", book.value?.classification)
            detailRow("ISBN", book.value?.isbn)

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { /* Acción Editar */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF572365)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { /* Acción Eliminar */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFDD7F12)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (book.value?.book_status?.book_status?.lowercase() == "lent") {
                bookLoans.value?.forEach { bookLoan ->
                    if (bookLoan.book._id == bookId && bookLoan.loan_status.loan_status.lowercase() != "finished") {
                        Column {

                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "Loan Details",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF572365)
                                    ),
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )
                                Divider(color = Color.LightGray, thickness = 1.dp)

                                bookLoans.value?.forEach { loan ->
                                    if (loan.book._id == bookId && loan.loan_status.loan_status.lowercase() != "finished") {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Loaned to:",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF572365)
                                                )
                                            )
                                            Text(
                                                text = "${loan.user.name} ${loan.user.last_name}",
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
                                                text = "Start Date:",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF572365)
                                                )
                                            )
                                            Text(
                                                text = formatUtcToLocalWithDate(bookLoan.createdAt),
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
                                                text = "End Date:",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF572365)
                                                )
                                            )
                                            Text(
                                                text = formatUtcToLocalWithDate(bookLoan.return_date),
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
                                                text = "Status:",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF572365)
                                                )
                                            )
                                            Text(
                                                text = loan.loan_status.loan_status,
                                                style = TextStyle(fontSize = 16.sp, color = Color.Black)
                                            )
                                        }
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                            }
                            Divider(color = Color.LightGray, thickness = 1.dp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        loanViewModel.approveLoan(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""), bookLoan._id)
                                    },
                                    modifier = Modifier
                                        .height(48.dp)
                                        .weight(1f),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = PurpleC),
                                    shape = RoundedCornerShape(12.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Approve",
                                        tint = Color.White,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Approve",
                                        color = Color.White,
                                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    )
                                }
                                Button(
                                    onClick = {
                                        loanViewModel.finishLoan(
                                            localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                                            bookLoan._id
                                        )
                                    },
                                    modifier = Modifier
                                        .height(48.dp)
                                        .weight(1f),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.error),
                                    shape = RoundedCornerShape(12.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.SettingsBackupRestore,
                                        contentDescription = "Finish",
                                        tint = Color.White,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Finish",
                                        color = Color.White,
                                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    )
                                }
                            }


                        }
                    }
                }
            }
        }
    }

}