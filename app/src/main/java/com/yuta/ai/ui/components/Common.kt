package com.yuta.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.yuta.ai.ui.theme.YutaBrandGradient

/** The signature pill-shaped gradient button used across Yuta's screens. */
@Composable
fun GradientPillIcon(
    icon: ImageVector,
    contentDescription: String?,
    size: androidx.compose.ui.unit.Dp = 52.dp,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                if (enabled) YutaBrandGradient
                else androidx.compose.ui.graphics.Brush.linearGradient(
                    listOf(Color(0xFF3A3650), Color(0xFF3A3650))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier
                .padding((size.value / 3).dp)
        )
    }
}

@Composable
fun GradientText(text: String, style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium) {
    androidx.compose.material3.Text(
        text = text,
        style = style.copy(brush = YutaBrandGradient)
    )
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    )
}
