package com.example.commutual.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.commutual.R

val OpenSans = FontFamily(
    Font(R.font.open_sans_light, FontWeight.Light),
    Font(R.font.open_sans_regular),
    Font(R.font.open_sans_medium, FontWeight.Medium),
    Font(R.font.open_sans_semi_bold, FontWeight.SemiBold),
    Font(R.font.open_sans_bold, FontWeight.Bold),
    Font(R.font.open_sans_extra_bold, FontWeight.ExtraBold)
)


// Set of Material typography styles to start with
val Typography = Typography(
    // Top bar title,
    displayLarge = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    // Username on profile page + Post details
    displayMedium = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp
    ),
    // Extended FAB text, basic buttons
    displaySmall = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    // Large titles (e.g. upcoming task sessions & chart title)
    headlineLarge = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    // Confirmation messages headline, post details dialog, Log out dialog
    headlineMedium = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    // Task username, post detail username, no upcoming sessions text
    // Chat Item Text, Message Screen Tabs, Charts titles
    headlineSmall = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    // Post title
    titleLarge = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    // Task item titles
    titleMedium = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    // Confirmation Result Item title, Bottom Modal Text,
    titleSmall = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    //
    bodyLarge = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    // body text, description, bio, card text, text field text
    bodyMedium = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    // Profile numbers
    labelLarge = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    // Days on commutual, username
    labelMedium = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp
    ),
    // Timestamp, username, bottom nav label
    labelSmall = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    ),
)