
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("org.jetbrains.dokka") version "2.0.0"
}

android {
    namespace = "com.example.sistemagestionbiblioteca"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sistemagestionbiblioteca"
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.airbnb.android:lottie-compose:6.1.0")
    implementation("androidx.compose.material3:material3:1.3.1-beta02")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.31.0-alpha")
    implementation("androidx.compose.material:material-icons-extended:1.5.1")
    implementation("androidx.compose.material3:material3:1.3.1")


    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")


    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.google.accompanist:accompanist-placeholder-material:0.32.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.32.0")

}

//// Configuración de Dokka V2
//tasks.named<org.jetbrains.dokka.gradle.DokkaTaskPartial>("dokkaHtml") {
//    outputDirectory.set(buildDir.resolve("dokka/html"))
//    moduleName.set("SistemaGestionBiblioteca")
//
//    dokkaSourceSets {
//        named("main") {
//            // Raíces de código Kotlin y Java
//            kotlinSourceRoots.from(file("src/main/kotlin"))
//            sourceRoots.from(file("src/main/java"))
//
//            // Ajustes opcionales
//            skipEmptyPackages.set(true)
//            reportUndocumented.set(false)
//            perPackageOption { matchingRegex.set(".*") ; includeNonPublic.set(false) }
//        }
//    }

