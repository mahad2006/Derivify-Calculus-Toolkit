// In file: ui/screens/AboutScreen.kt

package com.codewithmahad.derivativecalculator.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codewithmahad.derivativecalculator.R

@Composable
fun AboutScreen(contentPadding: PaddingValues) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- Card 1: Personal Info ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dev_photo),
                    contentDescription = "Mahad's Profile Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Shaikh Mahad",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    "BSSE Student @ UBIT '29",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Aspiring Android developer on a journey to build impactful, user-friendly apps with Kotlin and Jetpack Compose.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Divider(modifier = Modifier.padding(vertical = 24.dp))
                Text("Community Initiative", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Founder of \"The UBIT Hub,\" a WhatsApp community connecting students at UBIT for collaboration and learning.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider(modifier = Modifier.padding(vertical = 24.dp))
                Text("Connect With Me", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SocialButton(icon = Icons.Default.Email, text = "Email", onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:codewithmahad@gmail.com")
                        }
                        context.startActivity(intent)
                    })
                    SocialButton(
                        icon = painterResource(id = R.drawable.github_logo),
                        text = "GitHub",
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mahad2006"))
                            context.startActivity(intent)
                        })
                    SocialButton(
                        icon = painterResource(id = R.drawable.linkedln_logo),
                        text = "LinkedIn",
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/codewithmahad"))
                            context.startActivity(intent)
                        })
                }
            }
        }

        // --- Card 2: About This Project ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "About This Project",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Derivify was developed as a final project for a Multi-Variable Calculus course. It's built from the ground up as a native Android application using the latest technologies.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    "Tech Stack:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kotlin • Jetpack Compose • Material 3",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // --- Card 3: Project Roadmap ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Project Roadmap", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                RoadmapItem(text = "Second Partial Derivative Test (Maxima/Minima)")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                RoadmapItem(text = "Interactive 3D Surface Plotting")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                RoadmapItem(text = "Directional Derivative Calculator")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                RoadmapItem(text = "Interactive Calculus Tutorials")
            }
        }
    }
}

@Composable
fun RoadmapItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}


// The SocialButton was missing from the file, so it's included here.
@Composable
fun SocialButton(icon: Any, text: String, onClick: () -> Unit) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedIconButton(
            onClick = onClick, modifier = Modifier.size(50.dp), shape = CircleShape
        ) {
            when (icon) {
                is ImageVector -> Icon(
                    icon, contentDescription = text
                )
                is Painter -> Icon(
                    painter = icon,
                    contentDescription = text,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Unspecified
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, style = MaterialTheme.typography.labelSmall)
    }
}
