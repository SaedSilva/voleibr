package br.dev.saed.voleibr.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object ConfigRoute


fun exitTransition() = shrinkHorizontally(animationSpec = tween(TIME_TRANSITION))

fun enterTransition() = expandHorizontally(animationSpec = tween(TIME_TRANSITION))

val TIME_TRANSITION = 500