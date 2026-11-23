package com.narezany.nomichat.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narezany.nomichat.data.ChatStore

@Composable
fun ProfileScreen(store: ChatStore) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA)),
    ) {
        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 18.dp, end = 18.dp, top = 22.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Профиль", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextMain,
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(14.dp))
                Avatar("АМ", 0xFF6C8CE0, size = 92.dp, online = true)
                Spacer(Modifier.height(12.dp))
                Text("Алекс Морозов", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = TextMain)
                Text("@alexm  ·  +7 999 123-45-67", fontSize = 13.sp, color = TextGray)
                Spacer(Modifier.height(14.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(NomiSoftBg)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("★", fontSize = 24.sp, color = Color(0xFFF5B822))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Монеты", fontSize = 12.sp, color = TextGray)
                        Text("${store.balance}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextMain)
                    }
                    Text("потратить →", fontSize = 12.sp, color = NomiBlue)
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
        item {
            SettingsItem(Icons.Default.Person, "Имя пользователя", "alexm")
            SettingsItem(Icons.Default.Phone, "Телефон", "+7 999 123-45-67")
            SettingsItem(Icons.Default.Email, "Описание", "Общаюсь и дарю подарки 🎁")
            SettingsItem(Icons.Default.Settings, "Уведомления", "Включены")
            SettingsItem(Icons.Default.Star, "Подарки", "8 доступно")
            SettingsItem(Icons.Default.Info, "О приложении", "Nomi 1.0")
        }
    }
}

@Composable
private fun SettingsItem(icon: ImageVector, title: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(NomiSoftBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = NomiBlue, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, color = TextMain)
            Text(value, fontSize = 12.sp, color = TextGray, maxLines = 1)
        }
        Text("›", fontSize = 24.sp, color = TextGray)
    }
}

@Composable
fun ContactsScreen(store: ChatStore, onOpenChat: (String) -> Unit) {
    val contacts = store.chats.filter { !it.isGroup }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        item {
            Text(
                "Контакты  ·  ${contacts.size}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
            )
        }
        items(contacts, key = { it.id }) { chat ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onOpenChat(chat.id) }
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(chat.initials, chat.color, online = chat.online)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(chat.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextMain)
                    Text(
                        if (chat.online) "в сети" else chat.status.ifEmpty { "был(а) недавно" },
                        fontSize = 12.sp,
                        color = if (chat.online) Color(0xFF37B24D) else TextGray,
                    )
                }
                Text("Написать", fontSize = 12.sp, color = NomiBlue,
                    modifier = Modifier.clickable { onOpenChat(chat.id) })
            }
        }
        item { Spacer(Modifier.height(20.dp)) }
    }
}
