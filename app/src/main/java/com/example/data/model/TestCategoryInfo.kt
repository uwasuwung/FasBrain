package com.example.data.model

data class TestCategoryInfo(
    val id: String,
    val title: String,
    val description: String,
    val questionCount: Int,
    val defaultTimeLimit: Int,
    val iconName: String
)

object TestCategories {
    val ALL = listOf(
        TestCategoryInfo(
            id = "all",
            title = "Tes IQ Komprehensif",
            description = "Gabungan 6 jenis tes psikotes menyeluruh untuk mengukur tingkat IQ lengkap.",
            questionCount = 30,
            defaultTimeLimit = 45,
            iconName = "Psychology"
        ),
        TestCategoryInfo(
            id = "iq",
            title = "Tes IQ Utama",
            description = "Uji kemampuan penalaran logika, analisis numerik, dan pemahaman konsep.",
            questionCount = 30,
            defaultTimeLimit = 45,
            iconName = "AutoAwesome"
        ),
        TestCategoryInfo(
            id = "deret_angka",
            title = "Deret Angka",
            description = "Latih pola logika matematika, hubungan angka beruntun, dan analisis deret.",
            questionCount = 15,
            defaultTimeLimit = 35,
            iconName = "FormatListNumbered"
        ),
        TestCategoryInfo(
            id = "deret_huruf",
            title = "Deret Huruf",
            description = "Tes kemampuan identifikasi pola abjad, urutan alfabet, dan logika kata.",
            questionCount = 15,
            defaultTimeLimit = 35,
            iconName = "Abc"
        ),
        TestCategoryInfo(
            id = "verbal",
            title = "Tes Verbal",
            description = "Kosa kata, sinonim, antonim, serta hubungan analogi kata.",
            questionCount = 20,
            defaultTimeLimit = 30,
            iconName = "Translate"
        ),
        TestCategoryInfo(
            id = "spasial",
            title = "Logika Spasial",
            description = "Visualisasi 2D/3D, rotasi bentuk, pencerminan, dan persepsi ruang.",
            questionCount = 15,
            defaultTimeLimit = 45,
            iconName = "Category"
        ),
        TestCategoryInfo(
            id = "memori",
            title = "Memori & Kecepatan",
            description = "Rentang angka, memori jangka pendek, dan pencocokan simbol (Coding Test).",
            questionCount = 15,
            defaultTimeLimit = 30,
            iconName = "Timer"
        ),
        TestCategoryInfo(
            id = "buta_warna",
            title = "Cek Buta Warna",
            description = "Tes kesehatan penglihatan warna menggunakan metode pelat Ishihara.",
            questionCount = 10,
            defaultTimeLimit = 30,
            iconName = "Palette"
        )
    )

    fun getById(id: String): TestCategoryInfo {
        return ALL.find { it.id == id } ?: ALL[0]
    }
}
