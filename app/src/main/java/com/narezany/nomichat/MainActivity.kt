package com.narezany.nomichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.narezany.nomichat.data.ChatStore
import com.narezany.nomichat.ui.ChatsScreen
import com.narezany.nomichat.ui.ContactsScreen
import com.narezany.nomichat.ui.ConversationScreen
import com.narezany.nomichat.ui.GiftsScreen
import com.narezany.nomichat.ui.NomiTheme
import com.narezany.nomichat.ui.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NomiTheme {
                NomiApp()
            }
        }
    }
}

@Composable
fun NomiApp() {
    val store = remember { ChatStore() }
    var tab by remember { mutableStateOf(0) }
    var openChatId by remember { mutableStateOf<String?>(null) }
    val chat = openChatId?.let { store.chat(it) }

    if (chat != null) {
        ConversationScreen(
            chat = chat,
            store = store,
            onBack = { openChatId = null },
            onOpenGifts = {
                store.giftTarget = chat.id
                openChatId = null
                tab = 2
            },
        )
        return
    }

    val open: (String) -> Unit = { id ->
        store.openChat(id)
        openChatId = id
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Чаты") },
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Контакты") },
                )
                NavigationBarItem(
                    selected = tab == 2,
                    onClick = { tab = 2 },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    label = { Text("Подарки") },
                )
                NavigationBarItem(
                    selected = tab == 3,
                    onClick = { tab = 3 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Профиль") },
                )
            }
        },
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (tab) {
                0 -> ChatsScreen(store, open)
                1 -> ContactsScreen(store, open)
                2 -> GiftsScreen(store)
                3 -> ProfileScreen(store)
            }
        }
    }
}
