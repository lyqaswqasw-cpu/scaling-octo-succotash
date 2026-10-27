package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurpleDeep
import com.example.ui.theme.FoulPurpleGlow
import com.example.ui.theme.FoulTextSecondary

/**
 * Settings Screen ("إضافة قسم في شريط سفلي باسم الإعدادات واجعل هاذهي قناة تيلجرام https://t.me/jdj_q
 * وعند الضغط بنقلة إلى قناة تيلجرام وبشكل شغال فقط هاذا الخيار وبشكل أيقونة احترافية جداً")
 */
@Composable
fun SettingsScreen(
    telegramUrl: String = "https://t.me/jdj_q"
) {
    val context = LocalContext.current
    val targetUrl = telegramUrl.ifBlank { "https://t.me/jdj_q" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FoulPurpleDeep.copy(alpha = 0.65f), FoulBlackBg)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(listOf(FoulPurpleBright, FoulPurpleDeep))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = "الإعدادات",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "التواصل والتحديثات الرسمية لتطبيق Foul TV",
                        style = MaterialTheme.typography.bodySmall,
                        color = FoulTextSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ONLY Option: Official Telegram Channel Card with Custom Vector Telegram Icon
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = FoulCardDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(22.dp))
                .clickable {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/jdj_q"))
                        context.startActivity(intent)
                    } catch (_: Exception) {
                        try {
                            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl))
                            context.startActivity(fallbackIntent)
                        } catch (_: Exception) {
                        }
                    }
                }
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF2AABEE), FoulPurpleGlow)
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .testTag("settings_telegram_button")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                FoulPurpleDeep.copy(alpha = 0.55f),
                                FoulCardDark
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Custom Professional Telegram Paper-Plane Emblem
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF2AABEE), Color(0xFF229ED9))
                            )
                        )
                        .border(2.dp, Color.White.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(32.dp)) {
                        val w = size.width
                        val h = size.height
                        val path = Path().apply {
                            moveTo(w * 0.92f, h * 0.14f)
                            lineTo(w * 0.08f, h * 0.47f)
                            lineTo(w * 0.34f, h * 0.58f)
                            lineTo(w * 0.76f, h * 0.28f)
                            lineTo(w * 0.45f, h * 0.64f)
                            lineTo(w * 0.45f, h * 0.86f)
                            lineTo(w * 0.58f, h * 0.72f)
                            lineTo(w * 0.78f, h * 0.86f)
                            close()
                        }
                        drawPath(path = path, color = Color.White)
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "قناة التيلجرام الرسمية",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "انضم الآن لمتابعة التحديثات والقنوات المباشرة",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FoulTextSecondary
                    )
                    Surface(
                        color = FoulPurpleDeep,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "t.me/jdj_q",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF38BDF8),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(FoulPurpleBright.copy(alpha = 0.25f))
                        .border(1.dp, FoulPurpleGlow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "فتح قناة التيلجرام",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
