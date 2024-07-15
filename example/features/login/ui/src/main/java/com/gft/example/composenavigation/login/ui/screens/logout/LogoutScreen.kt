package com.gft.example.composenavigation.login.ui.screens.logout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
fun LogoutDialog(
    modifier: Modifier = Modifier,
    onLogoutClicked: () -> Unit,
    onCancelClicked: () -> Unit,
) {
    Box(modifier = modifier.width(280.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Would you like to logout?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
                Divider(color = MaterialTheme.colorScheme.onSurface)
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onCancelClicked() }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onLogoutClicked() }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "OK",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
internal fun LogoutDialogPreview() {
    ComposeMultimoduleNavigationTheme {
        LogoutDialog(
            onLogoutClicked = {},
            onCancelClicked = {}
        )
    }
}
