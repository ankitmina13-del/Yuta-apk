package com.yuta.ai.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yuta.ai.R
import com.yuta.ai.data.ChatViewModel
import com.yuta.ai.ui.theme.*

@Composable
fun ProfileScreen(viewModel: ChatViewModel) {
    Scaffold(containerColor = YutaBackground) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(YutaSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Yuta",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(Modifier.height(16.dp))
            Text("Yuta AI", style = MaterialTheme.typography.headlineMedium, color = YutaTextPrimary, fontWeight = FontWeight.Bold)
            Text("Think beyond limits", style = MaterialTheme.typography.bodyMedium, color = YutaAccentVioletLight)
            Spacer(Modifier.height(24.dp))

            InfoCard(
                icon = Icons.Filled.AutoAwesome,
                title = "Hamesha free",
                subtitle = "Chat, image generation aur voice — sab kuch bina kisi payment ke."
            )
            Spacer(Modifier.height(12.dp))
            InfoCard(
                icon = Icons.Filled.Info,
                title = "Powered by Pollinations.ai",
                subtitle = "Free, keyless AI endpoints use hote hain — isiliye kabhi-kabhi thoda rate-limited ho sakta hai."
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(YutaSurfaceCard)
                    .clickable { viewModel.clearChat() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.DeleteOutline, contentDescription = null, tint = YutaError)
                Spacer(Modifier.width(12.dp))
                Text("Saari chat history clear karo", color = YutaError, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.weight(1f))
            Text(
                "Version 1.0",
                style = MaterialTheme.typography.labelMedium,
                color = YutaTextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun InfoCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(YutaSurfaceCard)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(YutaBrandGradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium, color = YutaTextPrimary, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = YutaTextSecondary)
        }
    }
}
