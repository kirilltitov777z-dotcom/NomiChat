package com.narezany.nomichat.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narezany.nomichat.data.ChatStore
import com.narezany.nomichat.data.GIFTS
import com.narezany.nomichat.data.Gift

@Composable
fun GiftsScreen(store: ChatStore) {
    val context = LocalContext.current
    val target = store.giftTarget ?: store.chats.firstOrNull()?.id

    Column(Modifier.fillMaxSize().background(Color(0xFFF7F8FA))) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 14.dp),
        ) {
            Text("Подарки", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextMain)
            Text("Отправь немного тепла", fontSize = 13.sp, color = TextGray)
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                            listOf(Color(0xFF5AA9F5), Color(0xFF3380E0)),
                        ),
                    )
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("★", fontSize = 24.sp, color = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Твой баланс", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                    Text("${store.balance}  монет", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Text(
            "Кому отправить",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextGray,
            modifier = Modifier.padding(start = 18.dp, top = 14.dp, bottom = 6.dp),
        )
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            store.chats.forEach { chat ->
                val selected = target == chat.id
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable { store.giftTarget = chat.id },
                ) {
                    Box(
                        Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(if (selected) NomiBlue else Color.Transparent)
                            .border(2.dp, if (selected) NomiBlue else Color.Transparent, CircleShape)
                            .padding(3.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Avatar(chat.initials, chat.color, size = 50.dp)
                    }
                    Spacer(Modifier.height(3.dp))
                    Text(
                        chat.name.split(" ").first(),
                        fontSize = 11.sp,
                        color = if (selected) NomiBlue else TextGray,
                        maxLines = 1,
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(GIFTS, key = { it.id }) { gift ->
                GiftCard(gift) {
                    val id = store.giftTarget ?: store.chats.firstOrNull()?.id
                    if (id == null) {
                        Toast.makeText(context, "Нет чатов", Toast.LENGTH_SHORT).show()
                        return@GiftCard
                    }
                    if (store.sendGift(id, gift)) {
                        Toast.makeText(context, "${gift.name} отправлен!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Не хватает монет", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
private fun GiftCard(gift: Gift, onSend: () -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(gift.color)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(gift.imageRes),
                contentDescription = gift.name,
                modifier = Modifier.size(104.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(gift.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextMain)
        Spacer(Modifier.height(2.dp))
        Text(
            "${gift.price} монет",
            fontSize = 12.sp,
            color = TextGray,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onSend,
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NomiBlue),
        ) {
            Text("Отправить", fontSize = 12.sp)
        }
    }
}
