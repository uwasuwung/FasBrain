package com.example.utils

import androidx.compose.ui.graphics.Color

data class ScoreInterpretation(
    val categoryLabel: String,
    val description: String,
    val colorHex: String,
    val composeColor: Color
)

object ScoreUtils {

    fun calculateIqScore(correctCount: Int, totalQuestions: Int): Int {
        if (totalQuestions <= 0) return 70
        val percentage = correctCount.toFloat() / totalQuestions
        // Scaled to realistic IQ distribution [70 - 145]
        val rawScore = 70 + (percentage * 75).toInt()
        return rawScore.coerceIn(70, 145)
    }

    fun getInterpretation(iqScore: Int): ScoreInterpretation {
        return when {
            iqScore >= 130 -> ScoreInterpretation(
                categoryLabel = "Genius",
                description = "Anda memiliki kemampuan intelektual luar biasa dan daya analisis tingkat tinggi!",
                colorHex = "#FFD700", // Emas
                composeColor = Color(0xFFFFD700)
            )
            iqScore in 120..129 -> ScoreInterpretation(
                categoryLabel = "Cerdas",
                description = "Anda sangat cerdas di atas rata-rata dengan penalaran yang tajam!",
                colorHex = "#00BCD4", // Cyan
                composeColor = Color(0xFF00BCD4)
            )
            iqScore in 110..119 -> ScoreInterpretation(
                categoryLabel = "Di Atas Rata-rata",
                description = "Anda lebih cerdas dari kebanyakan orang dan mampu memecahkan masalah kompleks.",
                colorHex = "#4CAF50", // Hijau
                composeColor = Color(0xFF4CAF50)
            )
            iqScore in 90..109 -> ScoreInterpretation(
                categoryLabel = "Rata-rata",
                description = "Anda memiliki tingkat kecerdasan normal dan daya tangkap yang stabil.",
                colorHex = "#2196F3", // Biru
                composeColor = Color(0xFF2196F3)
            )
            iqScore in 80..89 -> ScoreInterpretation(
                categoryLabel = "Di Bawah Rata-rata",
                description = "Anda memiliki potensi baik, teruslah berlatih logika secara konsisten.",
                colorHex = "#FF9800", // Jingga
                composeColor = Color(0xFFFF9800)
            )
            iqScore in 70..79 -> ScoreInterpretation(
                categoryLabel = "Batas Rendah",
                description = "Pertimbangkan latihan rutin dan asah kemampuan pemecahan masalah.",
                colorHex = "#E91E63", // Merah Muda / Magenta
                composeColor = Color(0xFFE91E63)
            )
            else -> ScoreInterpretation(
                categoryLabel = "Perlu Pendampingan",
                description = "Segera tingkatkan latihan logika dan daya konsentrasi secara bertahap.",
                colorHex = "#F44336", // Merah
                composeColor = Color(0xFFF44336)
            )
        }
    }

    fun getCategoryName(categoryKey: String): String {
        return when (categoryKey) {
            "iq" -> "Tes IQ Utama"
            "deret_angka" -> "Deret Angka"
            "deret_huruf" -> "Deret Huruf"
            "verbal" -> "Tes Verbal"
            "spasial" -> "Logika Spasial"
            "buta_warna" -> "Cek Buta Warna"
            else -> "Tes IQ Komprehensif"
        }
    }
}
