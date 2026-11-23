package com.narezany.nomichat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narezany.nomichat.R
import com.narezany.nomichat.data.Chat
import com.narezany.nomichat.data.ChatStore
import com.narezany.nomichat.data.GIFTS
import com.narezany.nomichat.data.Message

@Composable
fun ConversationScreen(
    chat: Chat,
    store: ChatStore,
    onBack: () -> Unit,
    onOpenGifts: () -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(Color(0xFFF7F8FA))) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 6.dp, end = 8.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = NomiBlue)
            }
            Avatar(chat.initials, chat.color, size = 40.dp, online = chat.online)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(chat.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextMain, maxLines = 1)
                val status = if (chat.online) "в сети" else chat.status.ifEmpty { "был(а) недавно" }
                Text(status, fontSize = 12.sp, color = if (chat.online) Color(0xFF37B24D) else TextGray, maxLines = 1)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Phone, contentDescription = "Звонок", tint = NomiBlue)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = "Меню", tint = NomiBlue)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            reverseLayout = true,
        ) {
            items(chat.messages.reversed(), key = { it.id }) { msg ->
                MessageBubble(msg)
            }
            item {
                Spacer(Modifier.height(18.dp))
                Text(
                    "Сегодня",
                    fontSize = 11.sp,
                    color = TextGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                placeholder = { Text("Сообщение", fontSize = 14.sp, color = TextGray) },
                singleLine = true,
                shape = RoundedCornerShape(26.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F3F5),
                    unfocusedContainerColor = Color(0xFFF1F3F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = NomiBlue,
                ),
            )
            Spacer(Modifier.width(4.dp))
            IconButton(onClick = onOpenGifts) {
                Image(
                    painter = painterResource(R.drawable.gift_rose),
                    contentDescription = "Подарки",
                    modifier = Modifier.size(30.dp),
                )
            }
            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        store.sendText(chat.id, text)
                        text = ""
                    }
                },
            ) {
                Icon(Icons.Default.Send, contentDescription = "Отправить", tint = NomiBlue)
            }
        }
    }
}

@Composable
private fun MessageBubble(msg: Message) {
    val outgoing = msg.outgoing
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp),
        horizontalArrangement = if (outgoing) Arrangement.End else Arrangement.Start,
    ) {
        Column(horizontalAlignment = if (outgoing) Alignment.End else Alignment.Start) {
            if (msg.giftId != null) {
                val gift = GIFTS.firstOrNull { it.id == msg.giftId }
                Column(
                    Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(gift?.color ?: 0xFFFFF4D6))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(gift?.imageRes ?: R.drawable.gift_star),
                        contentDescription = gift?.name,
                        modifier = Modifier.size(104.dp),
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "${gift?.name ?: "Подарок"}${if (msg.giftCount > 1) " ×${msg.giftCount}" else ""}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMain,
                    )
                }
            } else {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (outgoing) OutBubble else Color.White)
                        .padding(horizontal = 14.dp, vertical = 9.dp),
                ) {
                    Text(
                        msg.text,
                        fontSize = 15.sp,
                        color = TextMain,
                        modifier = Modifier.widthIn(max = 260.dp),
                    )
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(
                msg.time + if (outgoing) "  ✓✓" else "",
                fontSize = 10.sp,
                color = if (outgoing) NomiBlue else TextGray,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}
