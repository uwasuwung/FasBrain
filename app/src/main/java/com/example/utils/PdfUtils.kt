package com.example.utils

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfUtils {

    fun generateAndShareCertificate(
        context: Context,
        userName: String,
        iqScore: Int,
        categoryLabel: String,
        testName: String,
        correctCount: Int,
        totalQuestions: Int
    ) {
        try {
            val pdfDocument = PdfDocument()
            // Landscape A4 proportions (842 x 595 points)
            val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            // Paints
            val bgPaint = Paint().apply { color = Color.parseColor("#F8FAFC") }
            val borderPaint = Paint().apply {
                color = Color.parseColor("#1A237E") // Navy
                style = Paint.Style.STROKE
                strokeWidth = 6f
            }
            val innerBorderPaint = Paint().apply {
                color = Color.parseColor("#FFD700") // Gold
                style = Paint.Style.STROKE
                strokeWidth = 2f
            }
            val titlePaint = Paint().apply {
                color = Color.parseColor("#1A237E")
                textSize = 32f
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
            }
            val subtitlePaint = Paint().apply {
                color = Color.parseColor("#00BCD4") // Cyan
                textSize = 18f
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
            }
            val bodyPaint = Paint().apply {
                color = Color.parseColor("#334155")
                textSize = 14f
                textAlign = Paint.Align.CENTER
            }
            val namePaint = Paint().apply {
                color = Color.parseColor("#1E293B")
                textSize = 28f
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
            }
            val scoreBoxPaint = Paint().apply {
                color = Color.parseColor("#1A237E")
                style = Paint.Style.FILL
            }
            val scoreTextPaint = Paint().apply {
                color = Color.parseColor("#FFD700")
                textSize = 36f
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
            }
            val scoreLabelPaint = Paint().apply {
                color = Color.WHITE
                textSize = 12f
                textAlign = Paint.Align.CENTER
            }
            val footerPaint = Paint().apply {
                color = Color.parseColor("#64748B")
                textSize = 11f
                textAlign = Paint.Align.CENTER
            }

            // Draw Background & Borders
            canvas.drawRect(0f, 0f, 842f, 595f, bgPaint)
            canvas.drawRect(20f, 20f, 822f, 575f, borderPaint)
            canvas.drawRect(26f, 26f, 816f, 569f, innerBorderPaint)

            // Header Logo & Title
            canvas.drawText("FOR AND SEEK STUDIO", 421f, 75f, subtitlePaint)
            canvas.drawText("SERTIFIKAT HASIL PSIKOTES & IQ", 421f, 115f, titlePaint)
            canvas.drawText("Diberikan secara resmi kepada:", 421f, 155f, bodyPaint)

            // Participant Name
            canvas.drawText(userName.ifEmpty { "Peserta FasBrain" }, 421f, 200f, namePaint)

            // Line under name
            val linePaint = Paint().apply {
                color = Color.parseColor("#CBD5E1")
                strokeWidth = 2f
            }
            canvas.drawLine(250f, 215f, 592f, 215f, linePaint)

            // Test Details
            canvas.drawText(
                "Telah menyelesaikan $testName dengan $correctCount dari $totalQuestions soal terisi benar.",
                421f,
                250f,
                bodyPaint
            )

            // Score Badge Box
            val scoreBox = RectF(331f, 280f, 511f, 380f)
            canvas.drawRoundRect(scoreBox, 16f, 16f, scoreBoxPaint)

            canvas.drawText("SKOR IQ", 421f, 305f, scoreLabelPaint)
            canvas.drawText(iqScore.toString(), 421f, 350f, scoreTextPaint)
            canvas.drawText(categoryLabel.uppercase(), 421f, 372f, scoreLabelPaint)

            // Date & Issuer
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val currentDate = dateFormat.format(Date())

            canvas.drawText("Tanggal Tes: $currentDate", 421f, 420f, bodyPaint)

            // Signatures
            val sigTitlePaint = Paint().apply {
                color = Color.parseColor("#1E293B")
                textSize = 13f
                isFakeBoldText = true
            }
            val sigSubPaint = Paint().apply {
                color = Color.parseColor("#64748B")
                textSize = 11f
            }

            // Left Sig - Tim Penguji
            canvas.drawText("Tim Psikotes FasBrain", 150f, 480f, sigTitlePaint)
            canvas.drawLine(90f, 520f, 230f, 520f, linePaint)
            canvas.drawText("PT Fas Technology Solutions", 100f, 538f, sigSubPaint)

            // Right Sig - Stempel Studio
            canvas.drawText("Direksi Pengembang", 650f, 480f, sigTitlePaint)
            canvas.drawLine(590f, 520f, 730f, 520f, linePaint)
            canvas.drawText("For And Seek Studio", 610f, 538f, sigSubPaint)

            // Footer
            canvas.drawText(
                "Dokumen Digital Resmi • FasBrain IQ & Psikotes Engine v1.0 • ID Verifikasi: FB-${System.currentTimeMillis() % 1000000}",
                421f,
                555f,
                footerPaint
            )

            pdfDocument.finishPage(page)

            // Write File
            val pdfDir = File(context.cacheDir, "certificates")
            if (!pdfDir.exists()) pdfDir.mkdirs()
            val pdfFile = File(pdfDir, "Sertifikat_FasBrain_${System.currentTimeMillis()}.pdf")
            val outputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(outputStream)
            outputStream.close()
            pdfDocument.close()

            // Share File
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Sertifikat Hasil Tes IQ FasBrain - $userName")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hasil Tes IQ FasBrain milik $userName:\nSkor IQ: $iqScore ($categoryLabel)\nDiuji oleh For And Seek Studio."
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Bagikan Sertifikat PDF"))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
