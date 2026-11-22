package com.yuta.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yuta.ai.data.ChatViewModel
import com.yuta.ai.ui.theme.*

private data class Tool(val title: String, val subtitle: String, val icon: ImageVector, val action: () -> Unit)

@Composable
fun ToolsScreen(
    viewModel: ChatViewModel,
    onOpenChat: () -> Unit,
    onOpenCreate: () -> Unit,
    onOpenVoice: () -> Unit,
) {
    val tools = listOf(
        Tool("Chat", "Kisi bhi topic par baat karo", Icons.Outlined.ChatBubbleOutline) { onOpenChat() },
        Tool("Generate Image", "Unlimited free AI images", Icons.Filled.AutoAwesome) { onOpenCreate() },
        Tool("Voice Chat", "Bolkar baat karo, suno jawaab", Icons.Filled.Mic) { onOpenVoice() },
        Tool("Write Content", "Caption, post, essay — kuch bhi likhwao", Icons.Filled.Edit) { onOpenChat() },
        Tool("Summarize", "Lamba text ek click mein chhota karo", Icons.Filled.Summarize) { onOpenChat() },
        Tool("Translate", "Kisi bhi bhasha mein translate karo", Icons.Filled.Translate) { onOpenChat() },
    )

    Scaffold(containerColor = YutaBackground, topBar = { ToolsTopBar() }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(tools) { tool -> ToolRow(tool) }
        }
    }
}

@Composable
private fun ToolsTopBar() {
    Row(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text("Tools", style = MaterialTheme.typography.headlineMedium, color = YutaTextPrimary, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ToolRow(tool: Tool) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(YutaSurfaceCard)
            .clickable { tool.action() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(YutaBrandGradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(tool.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(tool.title, style = MaterialTheme.typography.titleMedium, color = YutaTextPrimary, fontWeight = FontWeight.SemiBold)
            Text(tool.subtitle, style = MaterialTheme.typography.bodyMedium, color = YutaTextSecondary)
        }
    }
}
