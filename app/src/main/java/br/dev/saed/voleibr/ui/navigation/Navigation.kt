package br.dev.saed.voleibr.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import kotlinx.serialization.Serializable

fun enterTransition() = fadeIn(animationSpec = tween(TIME_TRANSITION))
fun exitTransition() = fadeOut(animationSpec = tween(TIME_TRANSITION))

private const val TIME_TRANSITION = 500