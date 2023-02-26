package com.example.commutual.ui.screens.item

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.commutual.model.CategoryEnum
import com.example.commutual.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    post: Post,
) {
    ListItem(

        headlineText = {
            Text(
                post.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        overlineText = {
            Text(
                stringResource(post.category.categoryResourceId),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(8.dp, 4.dp)
            )
        },
        supportingText = {
            Column {

                Text(
                    post.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )


            }
        }
    )
//    Divider()
//    Card(
//        backgroundColor = MaterialTheme.colors.background,
//        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = post.title)
//                Text(text = post.description)
//            }
//        }
}

@Preview
@Composable
fun postItem() {
    PostItem(post = Post("fds", "userid", "title", "description", CategoryEnum.CODING))
}