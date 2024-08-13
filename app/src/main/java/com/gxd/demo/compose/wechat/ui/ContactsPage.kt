package com.gxd.demo.compose.wechat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gxd.demo.compose.R
import com.gxd.demo.compose.ui.theme.MyTheme
import com.gxd.demo.compose.wechat.HomeViewModel
import com.gxd.demo.compose.wechat.data.User

@Composable
fun ContactsPage() {
    val viewModel: HomeViewModel = viewModel()
    val itemList = remember {
        listOf(
            User("contact_add", "新的朋友", R.drawable.ic_contact_add),
            User("contact_chat", "仅聊天", R.drawable.ic_contact_chat),
            User("contact_group", "群聊", R.drawable.ic_contact_group),
            User("contact_tag", "标签", R.drawable.ic_contact_tag),
            User("contact_official", "公众号", R.drawable.ic_contact_official),
        )
    }
    Column(Modifier.fillMaxSize()) {
        TopBar("通讯录")
        Box(Modifier.background(MyTheme.colorScheme.background).fillMaxSize()) {
            LazyColumn(Modifier.background(MyTheme.colorScheme.listItem).fillMaxWidth()) {
                itemsIndexed(itemList) { index, contact ->
                    ContactListItem(contact)
                    if (index >= itemList.lastIndex) return@itemsIndexed
                    HorizontalDivider(Modifier.padding(start = 56.dp), color = MyTheme.colorScheme.divider, thickness = 0.8f.dp)
                }
                item {
                    Text(
                        "朋友",
                        Modifier.background(MyTheme.colorScheme.background).fillMaxWidth().padding(12.dp, 8.dp),
                        fontSize = 14.sp,
                        color = MyTheme.colorScheme.onBackground
                    )
                }
                itemsIndexed(viewModel.contactList) { index, contact ->
                    ContactListItem(contact)
                    if (index >= itemList.lastIndex) return@itemsIndexed
                    HorizontalDivider(Modifier.padding(start = 56.dp), color = MyTheme.colorScheme.divider, thickness = 0.8f.dp)
                }
            }
        }
    }
}

@Composable
fun ContactListItem(contact: User) {
    Row(Modifier.fillMaxWidth()) {
        Image(
            painterResource(contact.avatar), "avatar",
            Modifier.padding(12.dp, 8.dp, 8.dp, 8.dp).size(36.dp).clip(RoundedCornerShape(4.dp))
        )
        Text(
            contact.name,
            Modifier.weight(1f).align(Alignment.CenterVertically),
            fontSize = 17.sp,
            color = MyTheme.colorScheme.textPrimary
        )
    }
}

@Preview
@Composable
private fun ContactsPagePreview() {
    ContactsPage()
}