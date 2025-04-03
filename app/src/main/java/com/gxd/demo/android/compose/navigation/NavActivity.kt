package com.gxd.demo.android.compose.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

class NavActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNavScreen(2222)
        }
    }
}

@Preview
@Composable
private fun MyNavScreen(defaultId: Int = 1111) {
    val navController = rememberNavController()

    NavHost(navController, "${ScreenType.SCREEN_A.name}/$defaultId"/* 初始页面 */) {
        composable(
            "${ScreenType.SCREEN_A.name}/{id}",
            listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 }),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down) }, // 返回时的进入动画
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up) }
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            NavScreen(ScreenType.SCREEN_A, navController, id)
        }
        composable(
            "${ScreenType.SCREEN_B.name}/{id}",
            listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, // 从左侧滑入
                    tween(500, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, // 向右滑出
                    tween(500)
                )
            }
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            NavScreen(ScreenType.SCREEN_B, navController, id)
        }
        composable(
            "${ScreenType.DEEP_LINK}/{id}",
            listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 }),
            listOf(navDeepLink { uriPattern = "myapp://navigation.com/screen/c/{id}" }),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            NavScreen(ScreenType.SCREEN_A, navController, id)
        }
    }
}

@Composable
private fun NavScreen(type: ScreenType, navController: NavController, id: Int = 0) {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Text("这是Screen ${type}, id = $id")
        val nextId = id + 1
        Button(onClick = {
            navController.navigate("${ScreenType.SCREEN_A.name}/$nextId") {
                // TODO: 没搞明白
                launchSingleTop = true
                popUpTo(ScreenType.SCREEN_A.name) { inclusive = false }
            }
        }) { Text("跳转Screen A") }
        Button(onClick = {
            navController.navigate("${ScreenType.SCREEN_B.name}/$nextId") {
                // TODO: 没搞明白
                launchSingleTop = true
                popUpTo(ScreenType.SCREEN_B.name) { inclusive = true }
            }
        }) { Text("跳转Screen B") }
        Button(onClick = { navController.navigate("${ScreenType.DEEP_LINK.name}/$nextId") }) { Text("DeepLink跳转") }
        Button(onClick = { navController.popBackStack() }) { Text("返回") }
    }
}

enum class ScreenType {
    SCREEN_A, SCREEN_B, DEEP_LINK
}
