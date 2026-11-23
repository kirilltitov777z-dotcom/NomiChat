package com.narezany.nomichat.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.narezany.nomichat.R

data class Message(
    val id: String,
    val text: String = "",
    val outgoing: Boolean,
    val time: String,
    val giftId: String? = null,
    val giftName: String = "",
    val giftCount: Int = 0,
    val read: Boolean = false,
)

data class Chat(
    val id: String,
    val name: String,
    val initials: String,
    val color: Long,
    val online: Boolean = false,
    val pinned: Boolean = false,
    val muted: Boolean = false,
    val unread: Int = 0,
    val isGroup: Boolean = false,
    val status: String = "",
    val messages: List<Message> = emptyList(),
)

data class Gift(
    val id: String,
    val name: String,
    val price: Int,
    val imageRes: Int,
    val color: Long,
)

val GIFTS = listOf(
    Gift("rose", "Роза", 50, R.drawable.gift_rose, 0xFFFBE4EA),
    Gift("cake", "Торт", 120, R.drawable.gift_cake, 0xFFE9F0FF),
    Gift("diamond", "Алмаз", 500, R.drawable.gift_diamond, 0xFFE2F5FF),
    Gift("crown", "Корона", 999, R.drawable.gift_crown, 0xFFFFF4D6),
    Gift("teddy", "Мишка", 180, R.drawable.gift_teddy, 0xFFF6ECE2),
    Gift("rocket", "Ракета", 350, R.drawable.gift_rocket, 0xFFE6F2FF),
    Gift("icecream", "Мороженое", 80, R.drawable.gift_icecream, 0xFFFFEFE2),
    Gift("star", "Звезда", 250, R.drawable.gift_star, 0xFFFFF7D6),
)

fun Gift.imageFor(id: String?): Int = GIFTS.firstOrNull { it.id == id }?.imageRes ?: R.drawable.gift_star

class ChatStore {
    var chats by mutableStateOf(sampleChats())
        private set

    var balance by mutableStateOf(1250)
        private set
    var giftTarget by mutableStateOf<String?>(null)

    fun chat(id: String): Chat? = chats.firstOrNull { it.id == id }

    fun openChat(id: String) {
        chats = chats.map { if (it.id == id) it.copy(unread = 0) else it }
    }

    fun sendText(chatId: String, text: String) {
        val clean = text.trim()
        if (clean.isEmpty()) return
        val msg = Message(
            id = "m${System.currentTimeMillis()}",
            text = clean,
            outgoing = true,
            time = nowTime(),
            read = false,
        )
        update(chatId) { it.copy(messages = it.messages + msg) }
    }

    fun sendGift(chatId: String, gift: Gift, count: Int = 1): Boolean {
        val cost = gift.price * count
        if (balance < cost) return false
        balance -= cost
        val msg = Message(
            id = "g${System.currentTimeMillis()}",
            outgoing = true,
            time = nowTime(),
            giftId = gift.id,
            giftName = gift.name,
            giftCount = count,
            read = false,
        )
        update(chatId) { it.copy(messages = it.messages + msg) }
        return true
    }

    fun receive(chatId: String, text: String) {
        val msg = Message(
            id = "r${System.currentTimeMillis()}",
            text = text,
            outgoing = false,
            time = nowTime(),
        )
        update(chatId) { it.copy(messages = it.messages + msg, unread = it.unread + 1) }
    }

    private fun update(id: String, block: (Chat) -> Chat) {
        chats = chats.map { if (it.id == id) block(it) else it }
    }
}

private fun nowTime(): String {
    val c = java.util.Calendar.getInstance()
    return "%02d:%02d".format(c.get(java.util.Calendar.HOUR_OF_DAY), c.get(java.util.Calendar.MINUTE))
}

private fun sampleChats(): List<Chat> = listOf(
    Chat(
        id = "anya", name = "Аня Соколова", initials = "АС", color = 0xFFE8849A, online = true,
        pinned = true, unread = 2, status = "в сети",
        messages = listOf(
            Message("a1", "Привет! Ты видел новые подарки?", false, "12:41"),
            Message("a2", "Да, алмаз просто огонь 🔥", true, "12:42", read = true),
            Message("a3", "Давай я тебе завтра подарю розу 🌹", false, "12:44"),
            Message("a4", "Ого, спасибо! Я буду ждать", true, "12:45", read = true),
            Message("a5", "Кстати, тут классный мессенджер нашел", false, "12:47"),
        ),
    ),
    Chat(
        id = "team", name = "Команда Nomi", initials = "KN", color = 0xFF5B9BE8,
        isGroup = true, unread = 5, status = "14 участников, 3 в сети",
        messages = listOf(
            Message("t1", "Всем привет! Обновление готово", false, "11:20"),
            Message("t2", "Супер, проверяю", true, "11:22", read = true),
            Message("t3", "Дизайн подарков теперь просто топ", false, "11:30"),
            Message("t4", "Согласен, Especially корона 👑", true, "11:31", read = true),
        ),
    ),
    Chat(
        id = "mark", name = "Марк", initials = "М", color = 0xFF7D6FE4, online = true,
        status = "был(а) недавно",
        messages = listOf(
            Message("m1", "Играем сегодня вечером?", false, "10:05"),
            Message("m2", "Да, как всегда в 8", true, "10:07", read = true),
            Message("m3", "Договорились 👌", false, "10:08"),
        ),
    ),
    Chat(
        id = "liza", name = "Лиза", initials = "Л", color = 0xFFF0A44E, unread = 1,
        messages = listOf(
            Message("l1", "Скинь, пожалуйста, ссылку", false, "Вчера"),
            Message("l2", "Вот она, держи", true, "Вчера", read = true),
            Message("l3", "Спасибо huge!", false, "09:12"),
        ),
    ),
    Chat(
        id = "mom", name = "Мама ❤️", initials = "М", color = 0xFFE56B7C, pinned = true,
        messages = listOf(
            Message("mo1", "Сынок, как дела?", false, "Вчера"),
            Message("mo2", "Нормально, не волнуйся", true, "Вчера", read = true),
            Message("mo3", "Позвони мне вечером", false, "Вчера"),
        ),
    ),
    Chat(
        id = "design", name = "Дизайн-студия", initials = "ДС", color = 0xFF43B6A0,
        isGroup = true, muted = true, status = "8 участников",
        messages = listOf(
            Message("d1", "Новые иконки готовы", false, "Пн"),
            Message("d2", "Красиво! Забираю", true, "Пн", read = true),
        ),
    ),
    Chat(
        id = "artem", name = "Артём", initials = "А", color = 0xFF6A8CC7,
        messages = listOf(
            Message("ar1", "Погнали в тир?", false, "Вс"),
            Message("ar2", "Я сегодня не смогу", true, "Вс", read = true),
        ),
    ),
    Chat(
        id = "news", name = "Новости Nomi", initials = "N", color = 0xFF3381D8,
        isGroup = true, muted = true, status = "1 204 подписчика",
        messages = listOf(
            Message("n1", "Обновление 1.0: подарки, чаты, новый дизайн 🎉", false, "12:00"),
        ),
    ),
    Chat(
        id = "katya", name = "Катя", initials = "К", color = 0xFFBA6FD6,
        messages = listOf(
            Message("k1", "Привет! Как тебе подарок?", false, "Сб"),
            Message("k2", "Очень красивый, спасибо!", true, "Сб", read = true),
        ),
    ),
)
