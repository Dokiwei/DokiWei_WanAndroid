@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinxSerializationJson)
}

android {
    namespace = "com.dokiwei.wanandroid"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.dokiwei.wanandroid"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //json序列化
    implementation(libs.kotlinx.serialization.json)
    //webView
    implementation(libs.accompanist.webview)
    //下拉刷新
    implementation(libs.accompanist.swiperefresh)
    //cookie持久化
    implementation(libs.persistentCookieJar)
    //coil加载图片
    implementation(libs.coil)
    implementation(libs.coil.compose)
    //网络
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    //带动画的navigation
    implementation(libs.accompanist.navigation.animation)
    //Compose
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    //androidx.compose
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.ui)
    debugImplementation(libs.ui.tooling)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    androidTestImplementation(libs.ui.test.junit4)
    //Kotlin
    implementation(libs.core.ktx)
    //生命周期感知
    implementation(libs.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    debugImplementation(libs.ui.test.manifest)
}