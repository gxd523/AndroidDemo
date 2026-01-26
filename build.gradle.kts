import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false// 「apply false」:只声明不应用
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.android.library) apply false
}

subprojects {
    // 全局添加kotlin新特性
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            // 1. 开启特性：显式备用字段
//            freeCompilerArgs.add("-Xexplicit-backing-fields")
            // 2. 开启语言引擎开关（报错信息要求的关键参数）
//            freeCompilerArgs.add("-XXLanguage:+ExplicitBackingFields")
        }
    }
}

tasks.register("ggg") {
    dependsOn(":app:assembleDebug")
    doLast {
        println("hahaha")
    }
}