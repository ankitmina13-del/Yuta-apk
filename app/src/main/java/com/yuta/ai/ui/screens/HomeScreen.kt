package com.yuta.ai.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.rotate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.yuta.ai.R
import com.yuta.ai.data.ChatViewModel
import com.yuta.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ChatViewModel,
    onOpenChat: () -> Unit,
    onOpenCreate: () -> Unit,
    onOpenVoice: () -> Unit,
    onOpenTools: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenProfile: () -> Unit,
) {
    var askText by remember { mutableStateOf("") }

    fun sendAndOpenChat(text: String) {
        if (text.isNotBlank()) {
            viewModel.sendMessage(text.trim())
        }
        onOpenChat()
    }

    Scaffold(containerColor = YutaBackground) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item { HomeTopBar(onMenuClick = onOpenProfile) }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    YutaOrb()
                    Spacer(Modifier.height(20.dp))
                    Text(
                        "Hello, I'm Yuta 👋",
                        style = MaterialTheme.typography.titleLarge,
                        color = YutaTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Aaj kaise madad karu?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = YutaTextSecondary
                    )
                }
            }

            item {
                AskBar(
                    value = askText,
                    onValueChange = { askText = it },
                    onSend = {
                        sendAndOpenChat(askText)
                        askText = ""
                    }
                )
            }

            item {
                QuickActionsGrid(
                    onOpenCreate = onOpenCreate,
                    onOpenVoice = onOpenVoice,
                    onOpenTools = onOpenTools,
                    onWriteContent = {
                        sendAndOpenChat("Mere liye ek achha content likh do: ")
                    }
                )
            }

            item { RecentChatsHeader(onSeeAll = onOpenHistory) }

            val recentUserMessages = viewModel.messages.filter { it.role == "user" }.takeLast(3).reversed()
            if (recentUserMessages.isEmpty()) {
                item { EmptyRecentChatRow(onClick = onOpenChat) }
            } else {
                items(recentUserMessages, key = { it.id }) { msg ->
                    RecentChatRow(text = msg.text, onClick = onOpenChat)
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar(onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = YutaTextPrimary)
        }
        Spacer(Modifier.weight(1f))
        Text(
            "YUTA",
            style = MaterialTheme.typography.titleLarge,
            color = YutaTextPrimary,
            fontWeight = FontWeight.Black
        )
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(YutaSurfaceElevated)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = YutaAccentCyan, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text("Free", style = MaterialTheme.typography.labelMedium, color = YutaTextPrimary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun YutaOrb() {
    val infiniteTransition = rememberInfiniteTransition(label = "orb")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing), RepeatMode.Restart),
        label = "orbRotation"
    )

    Box(
        modifier = Modifier
            .size(170.dp)
            .clip(CircleShape)
            .background(
                androidx.compose.ui.graphics.Brush.radialGradient(
                    listOf(YutaAccentViolet.copy(alpha = 0.35f), Color.Transparent)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Yuta",
            modifier = Modifier
                .size(150.dp)
                .rotate(rotation),
            contentScale = ContentScale.Fit
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AskBar(value: String, onValueChange: (String) -> Unit, onSend: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = YutaTextMuted, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Ask anything…", color = YutaTextMuted)
                }
            },
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = YutaSurfaceElevated,
                unfocusedContainerColor = YutaSurfaceElevated,
                focusedBorderColor = YutaAccentViolet,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = YutaTextPrimary,
                unfocusedTextColor = YutaTextPrimary,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            maxLines = 3
        )
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(YutaBrandGradient)
                .clickable { onSend() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
        }
    }
}

private data class QuickAction(val label: String, val icon: ImageVector, val onClick: () -> Unit)

@Composable
private fun QuickActionsGrid(
    onOpenCreate: () -> Unit,
    onOpenVoice: () -> Unit,
    onOpenTools: () -> Unit,
    onWriteContent: () -> Unit,
) {
    val actions = listOf(
        QuickAction("Generate Image", Icons.Filled.AutoAwesome, onOpenCreate),
        QuickAction("Voice Chat", Icons.Filled.Mic, onOpenVoice),
        QuickAction("Write Content", Icons.Filled.Edit, onWriteContent),
        QuickAction("Explore More", Icons.Filled.Explore, onOpenTools),
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        for (row in actions.chunked(2)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { action ->
                    QuickActionCard(action, modifier = Modifier.weight(1f))
                }
                if (row.size < 2) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun QuickActionCard(action: QuickAction, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(YutaSurfaceCard)
            .clickable { action.onClick() }
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(YutaAccentViolet.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(action.icon, contentDescription = null, tint = YutaAccentVioletLight, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text(
            action.label,
            style = MaterialTheme.typography.labelLarge,
            color = YutaTextPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun RecentChatsHeader(onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Recent Chats", style = MaterialTheme.typography.titleMedium, color = YutaTextPrimary, fontWeight = FontWeight.Bold)
        Text(
            "See all",
            style = MaterialTheme.typography.labelLarge,
            color = YutaAccentVioletLight,
            modifier = Modifier.clickable { onSeeAll() }
        )
    }
}

@Composable
private fun RecentChatRow(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(YutaSurfaceCard)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(YutaBrandGradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = YutaTextPrimary,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun EmptyRecentChatRow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(YutaSurfaceCard)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.AutoStories, contentDescription = null, tint = YutaTextMuted, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text("Abhi koi chat nahi hai — kuch pucho!", style = MaterialTheme.typography.bodyMedium, color = YutaTextMuted)
    }
}
