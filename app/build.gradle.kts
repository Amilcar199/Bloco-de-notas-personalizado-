plugins {
\tid("com.android.application")
\tid("org.jetbrains.kotlin.android")
\tid("com.google.devtools.ksp")
}

android {
\tcompileSdk = 34

\tdefaultConfig {
\t\tapplicationId = "com.example.noteswithalarms"
\t\tminSdk = 24
\t\ttargetSdk = 34
\t\tversionCode = 1
\t\tversionName = "1.0.0"

\t\ttestInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
\t}

\tbuildTypes {
\t\trelease {
\t\t\tisMinifyEnabled = false
\t\t\tproguardFiles(
\t\t\t\tgetDefaultProguardFile("proguard-android-optimize.txt"),
\t\t\t\t"proguard-rules.pro"
\t\t\t)
\t\t}
\t}

\tcompileOptions {
\t\tsourceCompatibility = JavaVersion.VERSION_17
\t\ttargetCompatibility = JavaVersion.VERSION_17
\t}
\tkotlinOptions {
\t\tjvmTarget = "17"
\t}

\tbuildFeatures {
\t\tcompose = true
\t}
\tcomposeOptions {
\t\tkotlinCompilerExtensionVersion = "1.5.14"
\t}

\tpackaging {
\t\tresources {
\t\t\texcludes += "/META-INF/{AL2.0,LGPL2.1}"
\t\t}
\t}
}

dependencies {
\t// Compose BOM
\timplementation(platform("androidx.compose:compose-bom:2024.09.02"))
\tandroidTestImplementation(platform("androidx.compose:compose-bom:2024.09.02"))

\t// Core
\timplementation("androidx.core:core-ktx:1.13.1")
\timplementation("androidx.activity:activity-compose:1.9.2")
\timplementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
\timplementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

\t// Compose UI
\timplementation("androidx.compose.ui:ui")
\timplementation("androidx.compose.ui:ui-tooling-preview")
\tdebugImplementation("androidx.compose.ui:ui-tooling")
\timplementation("androidx.compose.material3:material3")
\timplementation("androidx.navigation:navigation-compose:2.8.2")

\t// Room
\timplementation("androidx.room:room-runtime:2.6.1")
\timplementation("androidx.room:room-ktx:2.6.1")
\tksp("androidx.room:room-compiler:2.6.1")

\t// Coroutines
\timplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

\t// Accompanist (optional for system UI)
\timplementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")

\t// Testing
\ttestImplementation("junit:junit:4.13.2")
\tandroidTestImplementation("androidx.test.ext:junit:1.2.1")
\tandroidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
\tandroidTestImplementation("androidx.compose.ui:ui-test-junit4")
}

plugins {
	id("com.android.application") version "8.6.1"
	id("org.jetbrains.kotlin.android") version "2.0.20"
	id("org.jetbrains.kotlin.kapt") version "2.0.20"
	id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
}

android {
\tnamespace = "com.example.notesalarm"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.example.notesalarm"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		vectorDrawables.useSupportLibrary = true
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
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = "17"
		freeCompilerArgs += listOf(
			"-Xjvm-default=all"
		)
	}

	buildFeatures {
		compose = true
	}

	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.14"
	}

	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	implementation(platform("androidx.compose:compose-bom:2024.09.02"))

	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.activity:activity-compose:1.9.2")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
	implementation("androidx.navigation:navigation-compose:2.8.0")

	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")

	implementation("androidx.room:room-runtime:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")
	kapt("androidx.room:room-compiler:2.6.1")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.02"))
	androidTestImplementation("androidx.test.ext:junit:1.2.1")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")

	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}

