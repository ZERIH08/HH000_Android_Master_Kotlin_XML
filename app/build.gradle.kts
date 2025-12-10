plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.example.hh000_android_master_kotlin_xml"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.hh000_android_master_kotlin_xml"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

room {
    // Directory where Room will export schema JSONs
    // @app/schemas/ ???
    // Position - after plugin and before android block ???
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    //----------------------------------------------------------------------------------------------
    testImplementation(libs.junit)
    //----------------------------------------------------------------------------------------------
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //----------------------------------------------------------------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.browser)
    //----------------------------------------------------------------------------------------------
    platform(libs.compose.bom).apply {
        implementation(this)
        testImplementation(this)
        androidTestImplementation(this)
        debugImplementation(this)
    }
    implementation(libs.bundles.compose)
    testImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling.data)
    debugImplementation(libs.compose.ui.tooling.preview)
    //----------------------------------------------------------------------------------------------
//    platform(libs.firebase.bom).apply {
//        implementation(this)
//        testImplementation(this)
//        androidTestImplementation(this)
//        debugImplementation(this)
//    }
//    implementation(libs.bundles.firebase)
    //----------------------------------------------------------------------------------------------
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler) // or annotationProcessor(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)
    //----------------------------------------------------------------------------------------------
    implementation(libs.pdfbox.android)
    //----------------------------------------------------------------------------------------------
}