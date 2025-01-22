import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

class MyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("myExtension", MyExtension::class.java)
        target.beforeEvaluate {
            println("beforeEvaluate = ${extension.name}")
        }
        target.afterEvaluate {
            println("afterEvaluate = ${extension.name}")
        }
    }
}

open class MyExtension {
    var name = "abc"
}

apply<MyPlugin>()

myExtension {
    name = "efg"
}

fun Project.myExtension(action: MyExtension.() -> Unit) {
    extensions.configure<MyExtension>("myExtension", action)
}

android {
    namespace = "com.gxd.demo.compose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gxd.demo.compose"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        val properties = Properties()
        val inputStream = project.rootProject.file("local.properties").inputStream()
        properties.load(inputStream)
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("api_key") ?: ""}\"")
        buildConfigField("String", "BASE_URL", "\"${project.findProperty("BASE_URL") ?: ""}\"")
    }

//    signingConfigs {
//        create("sign_config") {
//            storeFile = file(System.getProperty("user.home") + "/.android/debug.keystore")
//            storePassword = "android"
//            keyAlias = "androiddebugkey"
//            keyPassword = "android"
//        }
//    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.named("debug").get()
        }
        release {
            isMinifyEnabled = true
            isDebuggable = false
            signingConfig = signingConfigs.named("debug").get()
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeCompiler { /* 配置Compose编译器 */ }

    flavorDimensions += listOf("default")
    productFlavors {
        create("tst") {
            dimension = "default"
            applicationIdSuffix = ".test"
            manifestPlaceholders["app_name"] = "ComposeDemoTest"
            manifestPlaceholders["app_icon"] = "@android:mipmap/sym_def_app_icon"
            buildConfigField("String", "BASE_URL", "\"https://test.api.github.com/\"")
        }
        create("prod") {
            dimension = "default"
        }
    }
}

dependencies {
    implementation(project(":lib_dal"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.browser)

    implementation(libs.coil.compose)
    implementation(libs.coil.okhttp)
    implementation(libs.coil.svg)

    implementation(libs.coroutines.android)

    implementation(libs.retrofit)

//    debugImplementation(libs.leakcanary)
//    releaseImplementation(libs.leakcanary.no.op)

    debugImplementation(libs.androidx.compose.ui.tooling)
}