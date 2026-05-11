package com.scribesoul.app.ui.screens.therapist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.scribesoul.R
import com.scribesoul.app.utils.DateTimeUtils
import com.scribesoul.app.viewModels.HomeViewModel
import com.scribesoul.app.viewModels.TherapistHomeViewModel

@Composable
fun ClientDetailScreen(
    navController: NavController,
    viewModel: TherapistHomeViewModel,
    clientId: String
) {

    // Ambil data history dan info client saat layar dibuka
    LaunchedEffect(clientId) {
        viewModel.loadClientDetailData(clientId)
    }

    // Cari info client dari cache yang ada di ViewModel
    val clientInfo = viewModel.therapistClients.find { it.clientId == clientId }

    // List warna selang-seling (Biru, Kuning, Merah)
    val pillColors = listOf(
        Color(0xFF74A8FF),
        Color(0xFFFFC107),
        Color(0xFFD10000)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp) // Ruang untuk Navbar
        ) {
            // --- HEADER CLUSTER (SOS & Profile) ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 56.dp, bottom = 10.dp)
                ) {
                    Icon(
                        // Gunakan ArrowBack standar atau drawable arrow custom kamu
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF2B395B),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() } // Navigasi kembali
                    )
                }
            }

// --- NAMA & DETAIL WAKTU (Rapat Kiri sesuai Figma) ---
            item {
                val latestSession = viewModel.selectedClientSessions.firstOrNull()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 24.dp)
                ) {
                    // Nama Client
                    Text(
                        text = clientInfo?.name ?: "Loading...",
                        style = TextStyle(
                            fontSize = 31.sp,
                            fontFamily = FontFamily(Font(R.font.verdana_bold)),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2B395B),
                            lineHeight = 44.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Detail 4 Baris (Dinamis)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        // 1. Tanggal (Dinamis)
                        Text(
                            text = if (latestSession != null)
                                DateTimeUtils.getFormattedDate(latestSession.dateTimestamp)
                            else "Date not set",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                color = Color(0xFF2B395B).copy(alpha = 0.8f)
                            )
                        )

                        // 2. Kondisi Utama (Dinamis)
                        Text(
                            text = clientInfo?.mainCondition ?: "General Consultation",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                color = Color(0xFF2B395B).copy(alpha = 0.8f)
                            )
                        )

                        // 3. Jam (Dinamis)
                        Text(
                            text = if (latestSession != null)
                                DateTimeUtils.formatTimeRange(latestSession.dateTimestamp, latestSession.durationMinutes)
                            else "00.00 - 00.00",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                color = Color(0xFF2B395B).copy(alpha = 0.8f)
                            )
                        )

                        // 4. Status (Dinamis)
                        Text(
                            text = "Status: ${latestSession?.status ?: "Scheduled"}",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                color = Color(0xFF2B395B).copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }

            // --- SECTION JADWAL (Biru Muda Grid) ---
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp).fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Color(0xFFBCD5FF))) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.fillMaxWidth().height(180.dp).verticalScroll(rememberScrollState())) {
                            Column {
                                val hours = (5..10).map { String.format("%02d:00", it) }
                                hours.forEach { hour ->
                                    Row(modifier = Modifier.fillMaxWidth().height(60.dp), verticalAlignment = Alignment.Top) {
                                        Text(text = hour, modifier = Modifier.width(55.dp).padding(start = 12.dp, top = 2.dp), style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        HorizontalDivider(modifier = Modifier.padding(top = 10.dp).weight(1f), thickness = 1.dp, color = Color.White.copy(alpha = 0.5f))
                                    }
                                }
                            }
                            Column(modifier = Modifier.padding(start = 65.dp)) {
                                Spacer(modifier = Modifier.height(40.dp))
                                ScheduleGridItem(
                                    name = clientInfo?.name?.split(" ")?.firstOrNull() ?: "Client",
                                    category = clientInfo?.mainCondition ?: "Session",
                                    time = "Upcoming",
                                    indicatorColor = Color(0xFF74A8FF)
                                )
                            }
                        }
                    }
                }
            }

            // --- SECTION HISTORY (Ungu) ---
            item {
                SectionContainer(title = "Session History", bgColor = Color(0xFFEBE0FF).copy(alpha = 0.6f)) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (viewModel.selectedClientSessions.isEmpty()) {
                            Text("No history recorded", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
                        } else {
                            viewModel.selectedClientSessions.forEachIndexed { index, session ->
                                HistoryItemPill(
                                    date = DateTimeUtils.getFormattedDate(session.dateTimestamp),
                                    time = DateTimeUtils.formatTimeRange(session.dateTimestamp, session.durationMinutes),
                                    indicatorColor = pillColors[index % pillColors.size]
                                )
                            }
                        }
                    }
                }
            }

            // --- SECTION NOTES (Pink) ---
            item {
                SectionContainer(title = "Therapist notes", bgColor = Color(0xFFFFE0EB).copy(alpha = 0.6f)) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val sessionsWithNotes = viewModel.selectedClientSessions.filter { it.therapistNote.isNotEmpty() }
                        if (sessionsWithNotes.isEmpty()) {
                            Text("No notes found", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
                        } else {
                            sessionsWithNotes.forEachIndexed { index, session ->
                                // Menggunakan komponen Card yang sama agar desain tidak berubah
                                NoteItemCard(
                                    date = DateTimeUtils.getFormattedDate(session.dateTimestamp),
                                    note = session.therapistNote,
                                    indicatorColor = pillColors[index % pillColors.size]
                                )
                            }
                        }
                    }
                }
            }

            // --- GRADIENT BORDER CARD (Di paling bawah) ---
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .fillMaxWidth()
                        .height(150.dp)
                        .shadow(4.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(width = 2.dp, brush = Brush.linearGradient(listOf(Color(0xFFBCD5FF), Color(0xFFFFE0EB))), shape = RoundedCornerShape(24.dp))
                )
            }
        }

        // --- BOTTOM NAVBAR KHUSUS THERAPIST ---
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomBarTherapist(navController)
        }
    }
}

// Komponen Kotak Berwarna untuk History & Notes
@Composable
fun SectionContainer(title: String, bgColor: Color, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp)) // Corner lebih tumpul sesuai figma
            .background(bgColor)
            .padding(20.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 32.sp, // Judul besar
                fontFamily = FontFamily(Font(R.font.verdana_bold)),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B395B)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        content()

        Spacer(modifier = Modifier.height(12.dp))

        // Link "see more history"
        Text(
            text = "see more history",
            fontSize = 10.sp,
            fontFamily = FontFamily(Font(R.font.verdana)),
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable { /* Aksi */ }
        )
    }
}

// Komponen Kartu Riwayat Sesi (Pill Kecil Putih)
@Composable
fun HistoryItemPill(date: String, time: String, indicatorColor: Color) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // --- TAMBAHKAN IntrinsicSize.Min DI SINI ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indikator Vertikal
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp) // Jarak atas-bawah agar bar terlihat "mengambang" rapi
                    .fillMaxHeight() // Sekarang ini akan bekerja!
                    .width(5.dp)
                    .clip(RoundedCornerShape(50.dp)) // Membuat ujung bulat sempurna
                    .background(indicatorColor)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Kolom Tanggal & Waktu tetap sama
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = date,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    color = Color(0xFF2B395B)
                )
                Text(
                    text = time,
                    fontSize = 10.sp,
                    color = Color(0xFF2B395B).copy(alpha = 0.7f),
                    fontFamily = FontFamily(Font(R.font.verdana))
                )
            }

            // Info Durasi & Harga tetap sama
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color(0xFF2B395B).copy(alpha = 0.8f))) {
                        append("2 Hours Session ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF2B395B))) {
                        append("Rp, 350.000")
                    }
                },
                style = TextStyle(fontSize = 8.sp, fontFamily = FontFamily(Font(R.font.verdana)), textAlign = TextAlign.End),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

// Komponen Kartu Jadwal Mungil (Stacked Grid)
@Composable
fun ScheduleGridItem(name: String, category: String, time: String, indicatorColor: Color) {
    Box(
        modifier = Modifier
            .padding(top = 2.dp, bottom = 2.dp, end = 16.dp)
            .width(200.dp)
            .height(60.dp) // Mengikuti tinggi baris grid 1 jam
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 24.dp, bottomEnd = 24.dp))
            .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 24.dp, bottomEnd = 24.dp))
            .background(Color.White.copy(alpha = 0.85f))
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.fillMaxHeight().width(5.dp).background(indicatorColor))
            Column(modifier = Modifier.padding(horizontal = 12.dp), verticalArrangement = Arrangement.Center) {
                Text(name, fontSize = 13.sp, fontFamily = FontFamily(Font(R.font.verdana_bold)), fontWeight = FontWeight.Bold, color = Color(0xFF2B395B))
                Text(category, fontSize = 10.sp, fontFamily = FontFamily(Font(R.font.verdana)), color = Color(0xFF2B395B).copy(alpha = 0.7f))
                Text(time, fontSize = 10.sp, fontFamily = FontFamily(Font(R.font.verdana)), color = Color(0xFF2B395B).copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun NoteItemCard(date: String, note: String, indicatorColor: Color) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White).padding(16.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(modifier = Modifier.fillMaxHeight().width(4.dp).clip(CircleShape).background(indicatorColor))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = date, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B395B))
                Text(text = note, fontSize = 11.sp, color = Color.DarkGray, lineHeight = 16.sp)
            }
        }
    }
}
