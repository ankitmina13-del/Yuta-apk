package com.yuta.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yuta.ai.data.PollinationsApi
import com.yuta.ai.ui.theme.*
import java.util.UUID

private data class GeneratedImage(val id: String, val prompt: String, val url: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(onBack: (() -> Unit)? = null) {
    var prompt by remember { mutableStateOf("") }
    val images = remember { mutableStateListOf<GeneratedImage>() }
    val context = LocalContext.current

    Scaffold(containerColor = YutaBackground) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = YutaTextPrimary)
                    }
                }
            }
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text("Create", style = MaterialTheme.typography.headlineMedium, color = YutaTextPrimary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Unlimited free AI images, powered by Yuta", style = MaterialTheme.typography.bodyMedium, color = YutaTextSecondary)
            }

            if (images.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = YutaAccentViolet, modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("Likho kya banana hai...", color = YutaTextMuted, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(images.reversed(), key = { it.id }) { img ->
                        ImageCard(img, onShare = { shareImageUrl(context, img.url) })
                    }
                }
            }

            PromptBar(
                value = prompt,
                onValueChange = { prompt = it },
                onGenerate = {
                    if (prompt.isNotBlank()) {
                        images.add(
                            GeneratedImage(
                                id = UUID.randomUUID().toString(),
                                prompt = prompt,
                                url = PollinationsApi.imageUrl(prompt)
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun ImageCard(image: GeneratedImage, onShare: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(YutaSurfaceCard)
    ) {
        Box {
            AsyncImage(
                model = image.url,
                contentDescription = image.prompt,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable { onShare() }
                    .padding(8.dp)
            ) {
                Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
        Text(
            text = image.prompt,
            style = MaterialTheme.typography.labelMedium,
            color = YutaTextSecondary,
            maxLines = 2,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PromptBar(value: String, onValueChange: (String) -> Unit, onGenerate: () -> Unit) {
    Surface(color = YutaBackground) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("A neon tiger in the rain…", color = YutaTextMuted) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = YutaSurfaceElevated,
                    unfocusedContainerColor = YutaSurfaceElevated,
                    focusedBorderColor = YutaAccentCyan,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = YutaTextPrimary,
                    unfocusedTextColor = YutaTextPrimary,
                ),
                maxLines = 3
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(YutaBrandGradient)
                    .clickable { onGenerate() }
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Banao", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun shareImageUrl(context: android.content.Context, url: String) {
    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(android.content.Intent.EXTRA_TEXT, url)
    }
    context.startActivity(android.content.Intent.createChooser(intent, "Share image"))
}
