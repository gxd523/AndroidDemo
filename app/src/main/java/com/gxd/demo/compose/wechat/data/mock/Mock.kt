package com.gxd.demo.compose.wechat.data.mock

import androidx.compose.runtime.mutableStateListOf
import com.gxd.demo.compose.R
import com.gxd.demo.compose.wechat.data.Chat
import com.gxd.demo.compose.wechat.data.ChatMessage
import com.gxd.demo.compose.wechat.data.User

object Mock {
    val chatList by lazy {
        listOf(
            Chat(
                friend = User("gaolaoshi", "高老师", R.drawable.avatar_gaolaoshi),
                mutableStateListOf(*(gaoMessageList.accumulateTimes(5) { acc, list ->
                    acc + list
                }).toTypedArray())
            ),
            Chat(
                friend = User("diuwuxian", "丢物线", R.drawable.avatar_diuwuxian),
                mutableStateListOf(
                    ChatMessage(User("diuwuxian", "丢物线", R.drawable.avatar_diuwuxian), "哈哈哈", "13:48"),
                    ChatMessage(User.Me, "哈哈昂", "13:48"),
                    ChatMessage(User("diuwuxian", "丢物线", R.drawable.avatar_diuwuxian), "你笑个屁呀", "13:48").apply { read = false }
                )
            )
        )
    }
    val contactList by lazy {
        listOf(
            User("gaolaoshi", "高老师", R.drawable.avatar_gaolaoshi),
            User("diuwuxian", "丢物线", R.drawable.avatar_diuwuxian)
        )
    }

    private val gaoMessageList by lazy {
        listOf(
            ChatMessage(User("gaolaoshi", "高老师", R.drawable.avatar_gaolaoshi), "锄禾日当午", "14:20"),
            ChatMessage(User.Me, "汗滴禾下土", "14:20"),
            ChatMessage(User("gaolaoshi", "高老师", R.drawable.avatar_gaolaoshi), "谁知盘中餐", "14:20"),
            ChatMessage(User.Me, "粒粒皆辛苦", "14:20"),
            ChatMessage(
                User("gaolaoshi", "高老师", R.drawable.avatar_gaolaoshi),
                "唧唧复唧唧，木兰当户织。不闻机杼声，惟闻女叹息。",
                "14:20"
            ),
            ChatMessage(User.Me, "双兔傍地走，安能辨我是雄雌？", "14:20"),
            ChatMessage(User("gaolaoshi", "高老师", R.drawable.avatar_gaolaoshi), "床前明月光，疑是地上霜。", "14:20"),
            ChatMessage(User.Me, "吃饭吧？", "14:20")
        )
    }
}

fun <T> T.accumulateTimes(times: Int, block: (acc: T, list: T) -> T): T =
    if (times > 1) block(this.accumulateTimes(times - 1, block), this) else block(this, this)