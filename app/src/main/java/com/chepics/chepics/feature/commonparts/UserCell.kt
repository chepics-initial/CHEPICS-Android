package com.chepics.chepics.feature.commonparts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chepics.chepics.domainmodel.User

@Composable
fun UserCell(
    user: User,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(16.dp)) {
            UserIcon(url = user.profileImageUrl, scale = IconScale.USER)
            
            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = user.fullname,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )

                user.bio?.let {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = it,
                        maxLines = 2
                    )
                }
            }
        }
        
        HorizontalDivider()
    }
}