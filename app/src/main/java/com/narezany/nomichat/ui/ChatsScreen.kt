package com.narezany.nomichat.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narezany.nomichat.data.Chat
import com.narezany.nomichat.data.ChatStore

@Composable
fun ChatsScreen(store: ChatStore, onOpenChat: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var searching by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(Color.White)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 10.dp, top = 14.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text("Nomi", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = NomiBlue)
                Text("мессенджер для своих", fontSize = 12.sp, color = TextGray)
            }
            IconButton(onClick = { searching = !searching; if (!searching) query = "" }) {
                Icon(Icons.Default.Search, contentDescription = "Поиск", tint = NomiBlue)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, contentDescription = "Меню", tint = NomiBlue)
            }
        }

        if (searching) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                placeholder = { Text("Поиск по чатам", fontSize = 14.sp) },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = NomiBlue,
                    unfocusedIndicatorColor = LineColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
            )
        }

        val favorites = remember(store.chats) { store.chats.filter { it.pinned } }
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(54.dp)
                        .background(NomiSoftBg, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Новый чат", tint = NomiBlue)
                }
                Spacer(Modifier.height(4.dp))
                Text("Новый", fontSize = 11.sp, color = TextGray)
            }
            Spacer(Modifier.width(14.dp))
            favorites.forEach { chat ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onOpenChat(chat.id) },
                ) {
                    Avatar(chat.initials, chat.color, size = 54.dp, online = chat.online)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        chat.name.split(" ").first(),
                        fontSize = 11.sp,
                        color = TextGray,
                        maxLines = 1,
                    )
                }
                Spacer(Modifier.width(14.dp))
            }
        }

        val visible = store.chats.filter { chat ->
            query.isBlank() || chat.name.contains(query, true) ||
                chat.messages.any { it.text.contains(query, true) }
        }

        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Text(
                    "ЧАТЫ  ·  ${visible.size}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextGray,
                    modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 4.dp),
                )
            }
            items(visible, key = { it.id }) { chat ->
                ChatRow(chat, onOpenChat)
            }
            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun ChatRow(chat: Chat, onOpenChat: (String) -> Unit) {
    val last = chat.messages.lastOrNull()
    val preview = when {
        last == null -> "Нет сообщений"
        last.giftId != null -> "🎁 ${last.giftName}${if (last.giftCount > 1) " ×${last.giftCount}" else ""}"
        last.outgoing -> "Вы: ${last.text}"
        else -> last.text
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onOpenChat(chat.id) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(chat.initials, chat.color, online = chat.online)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (chat.pinned) {
                    Text("📌 ", fontSize = 12.sp)
                }
                Text(
                    chat.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMain,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                )
                Text(last?.time ?: "", fontSize = 11.sp, color = if (chat.unread > 0) NomiBlue else TextGray)
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    preview,
                    fontSize = 13.sp,
                    color = TextGray,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                )
                if (chat.unread > 0) {
                    Spacer(Modifier.width(6.dp))
                    UnreadBadge(chat.unread)
                }
            }
        }
    }
}
