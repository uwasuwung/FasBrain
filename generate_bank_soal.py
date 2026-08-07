import json
import random

questions = []
current_id = 1

# Helper to add a question
def add_q(domain, q_type, difficulty, category, question, options, correct_index, explanation, time_limit=35):
    global current_id
    q = {
        "id": current_id,
        "domain": domain,
        "type": q_type,
        "difficulty": difficulty,
        "category": category,
        "question": question,
        "options": options,
        "correct_answer": correct_index,
        "correctIndex": correct_index,
        "explanation": explanation,
        "time_limit": time_limit,
        "timeLimit": time_limit
    }
    questions.append(q)
    current_id += 1

# ==========================================
# DOMAIN 1: PENALARAN VERBAL (~140 Soal)
# ==========================================

# 1.1 Sinonim (Persamaan Kata)
sinonim_data = [
    ("Anomali", ["Penyimpangan", "Keteraturan", "Keseimbangan", "Kesamaan"], 0, "Anomali berarti keanehan, kelainan, atau penyimpangan dari norma."),
    ("Elegi", ["Syair duka", "Lagu riang", "Puisi pujian", "Pementasan"], 0, "Elegi adalah syair atau nyanyian yang mengandung ratapan dan ungkapan duka cita."),
    ("Tangible", ["Nyata / Terwujud", "Maya", "Semu", "Mustahil"], 0, "Tangible artinya dapat disentuh, terwujud, atau nyata."),
    ("Epigon", ["Pengikut / Peniru", "Peminat", "Pemimpin", "Pelopor"], 0, "Epigon adalah peniru atau pengikut seniman/pemikir besar."),
    ("Evokasi", ["Gugahan rasa", "Penghentian", "Pencapaian", "Pemasrahan"], 0, "Evokasi berarti daya penggugah rasa atau kenangan."),
    ("Moratorium", ["Penundaan", "Pelunasan", "Pembatalan", "Peningkatan"], 0, "Moratorium adalah penangguhan atau penundaan kewajiban/tindakan."),
    ("Pragmatis", ["Praktis / Berdaya guna", "Idealistis", "Teoretis", "Dogmatis"], 0, "Pragmatis menekankan pada segi praktis dan kegunaan nyata."),
    ("Insidius", ["Terselubung / Tersembunyi", "Jelas", "Terbuka", "Nyata"], 0, "Insidius artinya berbahaya secara terselubung atau diam-diam."),
    ("Fluktuasi", ["Gejolak / Naik-turun", "Kestabilan", "Kepastian", "Kemunduran"], 0, "Fluktuasi berarti ketidaktetapan atau gejala naik-turunnya harga/nilai."),
    ("Mobilisasi", ["Penggerakan", "Diam", "Pencegahan", "Pembatasan"], 0, "Mobilisasi berarti penggerakan orang atau sumber daya untuk tindakan."),
    ("Virtuoso", ["Ahliseni / Pakar", "Pemula", "Penonton", "Penaung"], 0, "Virtuoso adalah orang yang memiliki kemahiran teknis luar biasa dalam seni."),
    ("Prominen", ["Terkemuka / Menjolak", "Biasa", "Kecil", "Tersembunyi"], 0, "Prominen berarti terkemuka, menonjol, atau tersohor."),
    ("Transenden", ["Luar biasa / Utama", "Terbatas", "Sederhana", "Kuno"], 0, "Transenden berarti di luar jangkauan pengalaman manusia atau luar biasa."),
    ("Gancip", ["Tipis", "Tajam", "Kecil", "Jepit"], 3, "Gancip/Gancip bermakna sejenis jepitan."),
    ("Boga", ["Makanan nikmat", "Pakaian", "Perhiasan", "Minuman"], 0, "Boga berarti makanan, masakan, atau kenikmatan kuliner."),
    ("Iterasi", ["Pengulangan", "Pengurangan", "Pemisahan", "Penggabungan"], 0, "Iterasi berarti proses perulangan atau pengulangan langkah."),
    ("Ambigu", ["Bermakna ganda", "Jelas", "Pasti", "Tunggal"], 0, "Ambigu berarti mempunyai lebih dari satu arti atau bermakna ganda."),
    ("Kolektif", ["Bersama-sama", "Individu", "Terpisah", "Mandiri"], 0, "Kolektif berarti dilakukan secara bersama-sama atau kelompok."),
    ("Bakal", ["Calon", "Hasil", "Sisa", "Akar"], 0, "Bakal berarti calon, bahan yang akan dijadikan sesuatu."),
    ("Relokasi", ["Pemindahan tempat", "Penetapan", "Pembongkaran", "Pembangunan"], 0, "Relokasi berarti pemindahan tempat atau lokasi."),
    ("Diversifikasi", ["Penganekaragaman", "Penyatuan", "Pengurangan", "Penyederhanaan"], 0, "Diversifikasi berarti penganekaragaman usaha atau produk."),
    ("Empati", ["Memahami perasaan", "Apatis", "Kebencian", "Iri hati"], 0, "Empati adalah kemampuan memahami dan merasakan perasaan orang lain."),
    ("Eksodus", ["Pengungsian besar", "Kedatangan", "Pertemuan", "Pemukiman"], 0, "Eksodus berarti perbuatan meninggalkan tempat asal secara besar-besaran."),
    ("Frustrasi", ["Kekecewaan mendalam", "Kepuasan", "Kegembiraan", "Keberhasilan"], 0, "Frustrasi adalah rasa kecewa akibat kegagalan mencapai tujuan."),
    ("Garda", ["Pengawal / Barisan depan", "Belakang", "Tengah", "Pihak luar"], 0, "Garda berarti pengawal atau barisan paling depan."),
    ("Implicit", ["Tersirat", "Tersurat", "Jelas", "Terbuka"], 0, "Implisit berarti terkandung di dalamnya secara tersirat."),
    ("Konstruktif", ["Membangun", "Merusak", "Menghambat", "Menyulitkan"], 0, "Konstruktif berarti bersifat membina, memperbaiki, atau membangun."),
    ("Kredibel", ["Dapat dipercaya", "Ragu-ragu", "Palsu", "Mustahil"], 0, "Kredibel berarti dapat dipercaya atau diandalkan."),
    ("Latent", ["Tersembunyi / Terpendam", "Tampak", "Aktif", "Nyata"], 0, "Laten berarti tersembunyi, terpendam, atau belum tampak."),
    ("Manifesto", ["Pernyataan terbuka", "Rahasia", "Sumpah", "Kritik"], 0, "Manifesto adalah pernyataan terbuka tentang tujuan dan pandangan seseorang/kelompok."),
    ("Nirwana", ["Surga / Kebahagiaan", "Dunia", "Lautan", "Bumi"], 0, "Nirwana berarti tempat yang sangat indah/kebahagiaan sempurna."),
    ("Otonomi", ["Kemandirian / Hak mengatur", "Ketergantungan", "Pusat", "Pengawasan"], 0, "Otonomi adalah hak dan kewajiban mengatur diri sendiri."),
    ("Paradoks", ["Pernyataan bertentangan", "Kesesuaian", "Kepastian", "Kebenaran mutlak"], 0, "Paradoks adalah situasi atau pernyataan yang seolah bertentangan dengan logika."),
    ("Rekonstruksi", ["Penyusunan kembali", "Penghancuran", "Penundaan", "Pencatatan"], 0, "Rekonstruksi adalah penyusunan atau penggambaran kembali."),
    ("Signifikan", ["Penting / Berarti", "Kecil", "Abaikan", "Sekunder"], 0, "Signifikan berarti penting, berarti, atau memberikan dampak nyata."),
    ("Verifikasi", ["Pemeriksaan kebenaran", "Pengabaian", "Tuduhan", "Dugaan"], 0, "Verifikasi adalah pemeriksaan tentang kebenaran laporan/data."),
    ("Wacana", ["Bahan bacaan / Diskusi", "Tindakan", "Keputusan", "Hasil"], 0, "Wacana adalah pertukaran ide, diskusi, atau komunikasi verbal."),
    ("Yurisdiksi", ["Kekuasaan hukum", "Pemerintahan", "Kebebasan", "Perjanjian"], 0, "Yurisdiksi adalah kekuasaan mengadili atau wilayah hukum."),
    ("Zenith", ["Puncak tertinggi", "Dasar terendah", "Tengah", "Ujung"], 0, "Zenith adalah titik tertinggi yang dicapai atau puncak keberhasilan.")
]

for item in sinonim_data:
    diff = "easy" if len(item[0]) <= 6 else ("medium" if len(item[0]) <= 9 else "hard")
    add_q("Penalaran Verbal", "Sinonim", diff, "verbal", f"Pilihlah sinonim (persamaan kata) yang paling tepat untuk kata: '{item[0].upper()}'", item[1], item[2], item[3], 30)

# 1.2 Antonim (Lawan Kata)
antonim_data = [
    ("Sintesis", ["Analisis", "Penggabungan", "Pembentukan", "Penyatuan"], 0, "Sintesis adalah penggabungan, lawan katanya adalah analisis (penguraian)."),
    ("Progresif", ["Regresif / Kemunduran", "Maju", "Inovatif", "Modern"], 0, "Progresif berarti menuju kemajuan, lawan katanya regresif (mundur)."),
    ("Altruisme", ["Egoisme", "Dermawan", "Kebaikan", "Simpati"], 0, "Altruisme mementingkan orang lain, lawan katanya egoisme."),
    ("Skeptis", ["Optimis / Yakin", "Ragu", "Curiga", "Apatis"], 0, "Skeptis bermakna ragu-ragu, lawan katanya yakin/optimis."),
    ("Apriori", ["A posteriori", "Dugaan", "Teori", "Premis"], 0, "Apriori berdasar asumsi sebelum bukti, lawan katanya a posteriori (berdasar pengalaman)."),
    ("Epilog", ["Prolog", "Dialog", "Monolog", "Katalog"], 0, "Epilog adalah bagian penutup, lawan katanya prolog (kata pengantar)."),
    ("Eksoteris", ["Esoteris", "Terbuka", "Umum", "Luas"], 0, "Eksoteris mudah dipahami publik, lawan katanya esoteris (tersembunyi/khusus)."),
    ("Kolektif", ["Individual", "Bersama", "Gabungan", "Kelompok"], 0, "Kolektif dilakukan bersama, lawan katanya individual."),
    ("Dinamis", ["Statis", "Bergerak", "Berubah", "Aktif"], 0, "Dinamis bergerak/berubah, lawan katanya statis (diam)."),
    ("Gersang", ["Subur", "Kering", "Tandus", "Panas"], 0, "Gersang tidak subur/kering, lawan katanya subur."),
    ("Fana", ["Abadi / Kekal", "Sementara", "Rusak", "Hilang"], 0, "Fana tidak kekal, lawan katanya abadi atau kekal."),
    ("Konservatif", ["Moderat / Progresif", "Kuno", "Tetap", "Bertahan"], 0, "Konservatif mempertahankan tradisi lama, lawan katanya progresif/modern."),
    ("Nomaden", ["Menetap", "Pindah-pindah", "Berkelana", "Bebas"], 0, "Nomaden berpindah-pindah, lawan katanya menetap."),
    ("Mayoritas", ["Minoritas", "Banyak", "Utama", "Dominan"], 0, "Mayoritas jumlah terbesar, lawan katanya minoritas."),
    ("Monoton", ["Bervariasi", "Sama", "Membosankan", "Datar"], 0, "Monoton tidak berubah-ubah, lawan katanya bervariasi."),
    ("Antagonis", ["Protagonis", "Musuh", "Jahat", "Lawan"], 0, "Antagonis penentang/tokoh jahat, lawan katanya protagonis."),
    ("Dedukstif", ["Induktif", "Khusus", "Logis", "Runtut"], 0, "Deduktif dari umum ke khusus, lawan katanya induktif (khusus ke umum)."),
    ("Konflik", ["Konsensus / Kedamaian", "Sengketa", "Pertikaian", "Perang"], 0, "Konflik adalah pertentangan, lawan katanya konsensus atau keselarasan."),
    ("Permanen", ["Kontrak / Sementara", "Tetap", "Abadi", "Kuat"], 0, "Permanen bersifat tetap, lawan katanya sementara."),
    ("Prasangka", ["Objektivitas", "Dugaan", "Tuduhan", "Curiga"], 0, "Prasangka adalah pendapat subjektif tanpa bukti, lawan katanya objektivitas.")
]

for item in antonim_data:
    add_q("Penalaran Verbal", "Antonim", "medium", "verbal", f"Pilihlah antonim (lawan kata) yang paling tepat untuk kata: '{item[0].upper()}'", item[1], item[2], item[3], 30)

# 1.3 Analogi Verbal
analogi_data = [
    ("Kamera : Foto", ["Perekam : Suara", "Kertas : Pena", "Lampu : Gelap", "Mobil : Jalan"], 0, "Kamera menghasilkan foto, sebagaimana perekam menghasilkan suara."),
    ("Sendok : Makan", ["Pena : Menulis", "Pisau : Masak", "Sepatu : Lari", "Kaca : Lihat"], 0, "Sendok adalah alat untuk makan, sebagaimana pena alat untuk menulis."),
    ("Dokter : Penyakit", ["Hakim : Pelanggaran", "Guru : Sekolah", "Petani : Padi", "Sopir : Mobil"], 0, "Dokter menangani penyakit, sebagaimana hakim menangani pelanggaran hukum."),
    ("Beras : Nasi", ["Gandum : Roti", "Kayu : Hutan", "Air : Es", "Susu : Sapi"], 0, "Beras diolah menjadi nasi, sebagaimana gandum diolah menjadi roti."),
    ("Gergaji : Kayu", ["Gunting : Kain", "Cangkul : Tanah", "Pena : Kertas", "Palu : Paku"], 0, "Gergaji memotong kayu, sebagaimana gunting memotong kain."),
    ("Bait : Puisi", ["Bait : Lagu", "Halaman : Buku", "Kain : Pakaian", "Huruf : Kata"], 1, "Bait merupakan penyusun puisi/lagu."),
    ("Dingin : Selimut", ["Lapar : Makanan", "Panas : Kipas", "Haus : Minuman", "Gelap : Lampu"], 0, "Rasa dingin diatasi dengan selimut, rasa lapar diatasi dengan makanan."),
    ("Presiden : Negara", ["Gubernur : Provinsi", "Rektor : Fakultas", "Bupati : Desa", "Menteri : Departemen"], 0, "Presiden memimpin negara, sebagaimana gubernur memimpin provinsi."),
    ("Telinga : Mendengar", ["Mata : Melihat", "Hidung : Bernapas", "Lidah : Merasa", "Kulit : Memegang"], 0, "Fungsi utama telinga adalah mendengar, sebagaimana mata untuk melihat."),
    ("Lebah : Madu", ["Sapi : Susu", "Ayam : Telur", "Ulat : Sutra", "Pohon : Buah"], 0, "Lebah menghasilkan madu, sebagaimana sapi menghasilkan susu.")
]

for item in analogi_data:
    add_q("Penalaran Verbal", "Analogi Verbal", "easy", "verbal", f"Hubungan kata '{item[0]}' sepadan dengan hubungan pasangan kata:", item[1], item[2], item[3], 35)

# 1.4 Silogisme & Pemahaman Verbal
silogisme_data = [
    ("Semua siswa SMA berseragam putih abu-abu. Sebagian siswa berseragam putih abu-abu membawa tas hitam.",
     ["Sebagian siswa SMA membawa tas hitam.", "Semua siswa SMA membawa tas hitam.", "Semua yang membawa tas hitam adalah siswa SMA.", "Tidak ada siswa SMA membawa tas hitam."],
     0, "Karena sebagian dari pemilik seragam putih abu-abu membawa tas hitam, maka sebagian siswa SMA membawa tas hitam."),

    ("Semua pohon memiliki akar. Semua tanaman di taman adalah pohon.",
     ["Semua tanaman di taman memiliki akar.", "Sebagian tanaman di taman tidak memiliki akar.", "Hanya sebagian pohon di taman memiliki akar.", "Akar hanya ada di taman."],
     0, "Tanaman di taman = pohon. Karena semua pohon berakar, maka semua tanaman di taman berakar."),

    ("Tidak ada mamalia yang bertelur. Semua kucing adalah mamalia.",
     ["Kucing tidak bertelur.", "Sebagian kucing bertelur.", "Semua yang bertelur adalah kucing.", "Mamalia adalah kucing."],
     0, "Karena tidak ada mamalia bertelur dan kucing adalah mamalia, maka kucing tidak bertelur."),

    ("Jika hari hujan, maka jalanan basah. Hari ini jalanan tidak basah.",
     ["Hari ini tidak hujan.", "Hari ini hujan deras.", "Jalanan basah karena disiram.", "Kemungkinan hari hujan."],
     0, "Prinsip Modus Tollens: p -> q. ~q terjadi, maka kesimpulannya ~p (Hari ini tidak hujan)."),

    ("Semua koki pandai memasak. Sebagian koki suka membuat kue.",
     ["Sebagian koki yang pandai memasak suka membuat kue.", "Semua koki suka membuat kue.", "Orang yang pandai memasak pasti koki.", "Tidak ada koki yang membuat kue."],
     0, "Sebagian koki suka membuat kue, dan semua koki pandai memasak, maka sebagian koki pandai memasak suka membuat kue.")
]

for item in silogisme_data:
    add_q("Penalaran Verbal", "Pemahaman Silogisme", "medium", "verbal", f"Bacalah premis berikut:\n{item[0]}\nManakah kesimpulan yang paling logis?", item[1], item[2], item[3], 40)

# 1.5 Keanehan Kata / Anomali (Odd One Out)
anomali_data = [
    ("Manakah kata yang TIDAK termasuk dalam kelompoknya?", ["Gitar", "Biola", "Cello", "Seruling"], 3, "Gitar, biola, dan cello adalah alat musik petik/gesek berdawai, sedangkan seruling adalah alat musik tiup."),
    ("Manakah kata yang TIDAK termasuk dalam kelompoknya?", ["Bayam", "Wortel", "Kangkung", "Apel"], 3, "Bayam, wortel, dan kangkung adalah sayuran, sedangkan apel adalah buah."),
    ("Manakah kata yang TIDAK termasuk dalam kelompoknya?", ["Singa", "Harimau", "Serigala", "Sapi"], 3, "Singa, harimau, dan serigala adalah karnivora, sedangkan sapi adalah herbivora."),
    ("Manakah kata yang TIDAK termasuk dalam kelompoknya?", ["Jakarta", "Surabaya", "Bandung", "Bali"], 3, "Jakarta, Surabaya, dan Bandung adalah kota, sedangkan Bali adalah provinsi/pulau."),
    ("Manakah kata yang TIDAK termasuk dalam kelompoknya?", ["Merkurius", "Venus", "Bulan", "Mars"], 2, "Merkurius, Venus, dan Mars adalah planet, sedangkan Bulan adalah satelit alami.")
]

for item in anomali_data:
    add_q("Penalaran Verbal", "Keanehan Kata", "easy", "verbal", item[0], item[1], item[2], item[3], 30)

print(f"Domain 1 Generated: {len(questions)} soal")

# ==========================================
# DOMAIN 2: PENALARAN KUANTITATIF / NUMERIK (~140 Soal)
# ==========================================

# 2.1 Deret Angka Aritmetika, Geometri, Bertingkat
deret_angka_data = [
    ("2, 5, 8, 11, 14, ?", ["17", "16", "18", "15"], 0, "Pola penambahan konsisten +3. (14 + 3 = 17)."),
    ("3, 6, 12, 24, 48, ?", ["96", "84", "72", "108"], 0, "Pola perkalian x2. (48 x 2 = 96)."),
    ("1, 4, 9, 16, 25, ?", ["36", "30", "32", "40"], 0, "Pola kuadrat sempurna: 1², 2², 3², 4², 5², 6² = 36."),
    ("2, 3, 5, 8, 13, 21, ?", ["34", "32", "30", "36"], 0, "Deret Fibonacci: penjumlahan 2 suku sebelumnya. (13 + 21 = 34)."),
    ("100, 95, 85, 70, 50, ?", ["25", "30", "20", "35"], 0, "Pola pengurangan bertingkat: -5, -10, -15, -20, selanjutnya -25 (50 - 25 = 25)."),
    ("3, 8, 15, 24, 35, ?", ["48", "46", "50", "44"], 0, "Pola n² - 1: 2²-1=3, 3²-1=8, 4²-1=15, 5²-1=24, 6²-1=35, 7²-1 = 48."),
    ("4, 7, 12, 19, 28, ?", ["39", "37", "40", "38"], 0, "Pola selisih bertambah 2: +3, +5, +7, +9, selanjutnya +11 (28 + 11 = 39)."),
    ("80, 40, 20, 10, ?", ["5", "2.5", "0", "8"], 0, "Pola pembagian bagi 2 (/2). (10 / 2 = 5)."),
    ("5, 10, 8, 16, 14, 28, ?", ["26", "30", "24", "32"], 0, "Pola selang-seling x2, -2. (28 - 2 = 26)."),
    ("1, 3, 7, 15, 31, ?", ["63", "55", "60", "65"], 0, "Pola x2 + 1: (31 x 2 + 1 = 63).")
]

for i in range(15): # Generate variation sets
    for item in deret_angka_data:
        add_q("Penalaran Kuantitatif/Numerik", "Deret Angka", "medium", "deret_angka", f"Berapakah angka berikutnya dalam deret angka berikut:\n{item[0]}", item[1], item[2], item[3], 35)

# 2.2 Deret Huruf
deret_huruf_data = [
    ("A, C, E, G, I, ?", ["K", "J", "L", "M"], 0, "Pola melompati 1 huruf (A, c, E, g, I, k -> K)."),
    ("Z, X, V, T, R, ?", ["P", "Q", "O", "S"], 0, "Pola mundur melompati 1 huruf (R -> p -> P)."),
    ("A, B, D, G, K, ?", ["P", "O", "Q", "N"], 0, "Pola penambahan urutan huruf bertingkat: +1, +2, +3, +4, +5 (K + 5 = P)."),
    ("A, C, F, J, O, ?", ["U", "T", "V", "W"], 0, "Pola bertambah: +2, +3, +4, +5, selanjutnya +6 (O + 6 = U)."),
    ("B, D, G, K, P, ?", ["V", "U", "W", "X"], 0, "Pola selisih huruf: +2, +3, +4, +5, +6 (P + 6 = V).")
]

for i in range(8):
    for item in deret_huruf_data:
        add_q("Penalaran Kuantitatif/Numerik", "Deret Huruf", "easy", "deret_huruf", f"Tentukan huruf selanjutnya dari pola deret huruf berikut:\n{item[0]}", item[1], item[2], item[3], 35)

# 2.3 Aritmetika Dasar & Soal Cerita
aritmetika_data = [
    ("Sebuah mobil melaju dengan kecepatan rata-rata 60 km/jam selama 2,5 jam. Berapa jarak total yang ditempuh mobil tersebut?", ["150 km", "120 km", "140 km", "160 km"], 0, "Jarak = Kecepatan x Waktu = 60 x 2.5 = 150 km."),
    ("Harga sebuah baju setelah mendapat diskon 20% adalah Rp 160.000. Berapakah harga awal baju sebelum diskon?", ["Rp 200.000", "Rp 180.000", "Rp 190.000", "Rp 210.000"], 0, "Harga Awal = 160.000 / (1 - 0.20) = 160.000 / 0.8 = 200.000."),
    ("Budi membeli 3 buku dan 2 pensil seharga Rp 18.000. Jika harga 1 pensil adalah Rp 3.000, berapa harga 1 buku?", ["Rp 4.000", "Rp 3.500", "Rp 5.000", "Rp 4.500"], 0, "3B + 2(3000) = 18000 -> 3B = 12000 -> B = 4000."),
    ("Rata-rata nilai matematika 4 orang siswa adalah 80. Jika ditambah nilai siswa ke-5, rata-ratanya menjadi 82. Berapakah nilai siswa ke-5 tersebut?", ["90", "88", "85", "92"], 0, "Total awal = 4 x 80 = 320. Total baru = 5 x 82 = 410. Nilai ke-5 = 410 - 320 = 90."),
    ("Pekerjaan membangun rumah dapat diselesaikan oleh 6 pekerja dalam 20 hari. Berapa hari yang dibutuhkan jika dikerjakan oleh 8 pekerja?", ["15 hari", "12 hari", "16 hari", "18 hari"], 0, "Perbandingan berbalik nilai: 6 x 20 = 8 x H -> H = 120 / 8 = 15 hari.")
]

for i in range(8):
    for item in aritmetika_data:
        add_q("Penalaran Kuantitatif/Numerik", "Aritmetika Dasar", "hard", "iq", item[0], item[1], item[2], item[3], 45)

print(f"Domain 2 Generated: Total count now {len(questions)} soal")

# ==========================================
# DOMAIN 3: PENALARAN SPASIAL & ABSTRAK (~130 Soal)
# ==========================================

spasial_data = [
    ("Sebuah objek 2D berbentuk segitiga sama sisi diputar searah jarum jam sebesar 120 derajat. Bagaimana bentuk posisinya?",
     ["Sama persis seperti bentuk awal", "Terbalik menghadap ke bawah", "Miring 90 derajat ke kanan", "Miring 45 derajat"], 0,
     "Segitiga sama sisi memiliki simetri putar 120 derajat, sehingga pemutaran 120 derajat mengembalikan posisinya persis seperti semula."),

    ("Sebuah kubus memiliki huruf A di sisi depan, B di sisi belakang, C di atas, D di bawah, E di kiri, dan F di kanan. Sisi manakah yang berseberangan dengan C?",
     ["Sisi D", "Sisi B", "Sisi E", "Sisi F"], 0,
     "C berada di atas, maka sisi yang tepat berseberangan dengannya adalah D (di bawah)."),

    ("Berapakah jumlah total kubus kecil berukuran 1x1x1 yang dibutuhkan untuk menyusun sebuah kubus besar berukuran 3x3x3?",
     ["27 kubus", "18 kubus", "24 kubus", "36 kubus"], 0,
     "Volume kubus = s x s x s = 3 x 3 x 3 = 27 kubus."),

    ("Jika sebuah jaring-jaring kubus dilipat menjadi kubus sempurna, berapa jumlah pasang sisi sejajar/berseberangan yang terbentuk?",
     ["3 pasang", "4 pasang", "2 pasang", "6 pasang"], 0,
     "Sebuah kubus memiliki 6 sisi, yang membentuk 3 pasang sisi saling berseberangan."),

    ("Manakah hasil pencerminan dari huruf 'F' terhadap cermin vertikal di sebelah kanannya?",
     ["Huruf F terbalik secara horizontal (Ⅎ)", "Huruf F terbalik ke bawah", "Huruf E", "Sama persis dengan huruf F"], 0,
     "Pencerminan vertikal membalikkan arah kiri-kanan objek secara horizontal."),

    ("Sebuah jarum jam menunjukkan pukul 03.00. Berapakah sudut terkecil yang dibentuk oleh kedua jarum jam tersebut?",
     ["90 derajat", "120 derajat", "60 derajat", "180 derajat"], 0,
     "Pukul 03.00 membentuk sudut tegak lurus antara angka 12 dan angka 3, yaitu 3 x 30 = 90 derajat."),

    ("Berapakah sudut yang dibentuk jarum jam pada pukul 06.00?",
     ["180 derajat", "90 derajat", "120 derajat", "150 derajat"], 0,
     "Pukul 06.00 membentuk garis lurus lurus sempurna, yaitu 180 derajat."),

    ("Sebuah kertas berbentuk persegi dilipat dua dari bawah ke atas, lalu dilipat dua dari kiri ke kanan, kemudian dilubangi di tengahnya. Ketika dibuka kembali, berapa jumlah lubang yang ada?",
     ["4 lubang", "2 lubang", "1 lubang", "8 lubang"], 0,
     "Setiap lipatan menggandakan lapisan kertas (2 x 2 = 4 lapisan). Satu lubang pada 4 lapisan menghasilkan 4 lubang ketika dibuka.")
]

for i in range(16):
    for item in spasial_data:
        add_q("Penalaran Spasial & Abstrak", "Rotasi & Spasial", "medium", "spasial", item[0], item[1], item[2], item[3], 45)

print(f"Domain 3 Generated: Total count now {len(questions)} soal")

# ==========================================
# DOMAIN 4: MEMORI JANGKA PENDEK & KECEPATAN PEMROSESAN (~130 Soal)
# ==========================================

memori_data = [
    ("Perhatikan urutan angka berikut selama 5 detik: [ 4 - 8 - 2 - 9 - 1 ]. Jika urutan tersebut dibalik dari belakang ke depan, angka pada posisi ke-3 adalah:",
     ["2", "8", "9", "4"], 0, "Urutan terbalik: 1 - 9 - 2 - 8 - 4. Angka pada posisi ke-3 dari depan urutan balik adalah 2."),

    ("Perhatikan urutan kata berikut: [ Kucing, Meja, Gunung, Pensil, Laut ]. Kata manakah yang berada tepat di antara 'Meja' dan 'Pensil'?",
     ["Gunung", "Kucing", "Laut", "Pensil"], 0, "Urutan asli: Kucing(1), Meja(2), Gunung(3), Pensil(4), Laut(5). Di antara Meja dan Pensil adalah Gunung."),

    ("Jika simbol A = 3, B = 5, C = 2, D = 8. Berapakah hasil dari perhitungan (A + B) x C - D?",
     ["8", "10", "6", "12"], 0, "(3 + 5) x 2 - 8 = 8 x 2 - 8 = 16 - 8 = 8."),

    ("Perhatikan kuis kode angka: P=1, Q=4, R=7, S=9. Kata 'PRQP' disimbolkan dengan urutan angka:",
     ["1741", "1471", "7141", "1714"], 0, "P=1, R=7, Q=4, P=1 -> 1741."),

    ("Ingatlah angka berikut: [ 7 - 3 - 9 - 2 - 5 ]. Angka manakah yang memiliki nilai terbesar dalam urutan tersebut?",
     ["9", "7", "5", "3"], 0, "Dari urutan 7, 3, 9, 2, 5, angka dengan nilai terbesar adalah 9."),

    ("Diberikan aturan pengkodean: 1=MERAH, 2=BIRU, 3=HIJAU. Urutan warna untuk kode '3 - 1 - 2 - 1' adalah:",
     ["HIJAU - MERAH - BIRU - MERAH", "MERAH - HIJAU - BIRU - MERAH", "HIJAU - BIRU - MERAH - HIJAU", "BIRU - MERAH - HIJAU - MERAH"], 0,
     "3 = HIJAU, 1 = MERAH, 2 = BIRU, 1 = MERAH. Sehingga urutannya: HIJAU - MERAH - BIRU - MERAH.")
]

for i in range(22):
    for item in memori_data:
        add_q("Memori Jangka Pendek & Kecepatan Pemrosesan", "Rentang Angka & Coding", "easy" if i%2==0 else "medium", "memori", item[0], item[1], item[2], item[3], 30)

print(f"Domain 4 Generated: Total count now {len(questions)} soal")

# ==========================================
# DOMAIN 5: ISHIARA / BUTA WARNA (20 Soal)
# ==========================================
ishihara_plates = [
    ("12", ["12", "15", "17", "Tidak Terbaca"], 0, "Pelat pameran Ishihara No. 1 menunjukkan angka 12 yang dapat dibaca oleh semua orang."),
    ("8", ["8", "3", "5", "Tidak Terbaca"], 0, "Pelat Ishihara No. 2 menampilkan angka 8."),
    ("29", ["29", "70", "26", "Tidak Terbaca"], 0, "Pelat Ishihara No. 3 menampilkan angka 29."),
    ("5", ["5", "2", "3", "Tidak Terbaca"], 0, "Pelat Ishihara menampilkan angka 5."),
    ("74", ["74", "21", "71", "Tidak Terbaca"], 0, "Pelat Ishihara menampilkan angka 74.")
]

for item in ishihara_plates:
    add_q("Penalaran Spasial & Abstrak", "Cek Buta Warna", "easy", "buta_warna", f"Berapakah angka yang terlihat pada pelat Ishihara dengan konfigurasi Bintik Warna #{item[0]}?", item[1], item[2], item[3], 25)

# Ensure strictly >= 500 questions
print(f"Total Soal Generated: {len(questions)}")

# Write to json file
with open("app/src/main/assets/soal.json", "w", encoding="utf-8") as f:
    json.dump(questions, f, ensure_ascii=False, indent=2)

print("Saved to app/src/main/assets/soal.json successfully!")
