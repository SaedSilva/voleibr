package br.dev.saed.voleibr.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object ConfigRoute

@Serializable
object StatsRoute

fun enterTransition() = slideInHorizontally(initialOffsetX = { TIME_TRANSITION }) + fadeIn()

fun exitTransition() = slideOutHorizontally(targetOffsetX = { -TIME_TRANSITION }) + fadeOut()



private val TIME_TRANSITION = 1000