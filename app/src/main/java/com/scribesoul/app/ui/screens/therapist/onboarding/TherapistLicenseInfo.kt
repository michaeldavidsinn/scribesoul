package com.scribesoul.app.ui.screens.therapist.onboarding

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.scribesoul.app.ui.components.OnboardingButton
import com.scribesoul.app.ui.components.OnboardingTemplate
import com.scribesoul.app.ui.components.OnboardingTextField

@Composable
fun TherapistLicenseInfo(navController: NavController) {
    // State untuk nomor STR
    var strNumber by remember { mutableStateOf("") }

    // State untuk menyimpan gambar hasil jepretan kamera
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    val darkBlue = Color(0xFF2B395B)

    // Launcher untuk membuka kamera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // Simpan hasil foto ke state
        if (bitmap != null) {
            capturedImage = bitmap
        }
    }

    OnboardingTemplate(
        title = "Professional\nLicense",
        subtitle = "STR Number (Surat Tanda Registrasi Psikolog)",
        backgroundColor = Color(0xFFFFFBEB), // Warna cream/kuning pucat sesuai Figma
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            navController.navigate("therapist_approaches")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Input Nomor STR
            OnboardingTextField(
                value = strNumber,
                onValueChange = { strNumber = it },
                placeholder = "STR Number",
                modifier = Modifier.width(280.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            // 2. Kotak Putih Tempat Muncul Gambar Sertifikat
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(280.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(40.dp))
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (capturedImage != null) {
                    // Jika gambar sudah diambil, tampilkan gambarnya
                    Image(
                        bitmap = capturedImage!!.asImageBitmap(),
                        contentDescription = "Certificate Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop // Supaya gambar memenuhi kotak dengan rapi
                    )
                } else {
                    // Jika belum ada gambar, tampilkan instruksi atau biarkan kosong
                    Text(
                        text = "",
                        color = darkBlue.copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 3. Tombol Upload (Memicu Kamera)
            Box(
                modifier = Modifier
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .clickable {
                        // Jalankan kamera
                        cameraLauncher.launch(null)
                    }
                    .padding(horizontal = 30.dp, vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Upload Sertificate",
                    color = darkBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTherapistLicenseInfo() {
    TherapistLicenseInfo(navController = NavController(LocalContext.current))
}