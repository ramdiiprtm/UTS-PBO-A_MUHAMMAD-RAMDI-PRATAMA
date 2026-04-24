import java.util.Scanner

// ==========================================
// 1. MODEL LAYER (Encapsulation)
// ==========================================

class Tiket(
    val idTiket: String,
    val jenisTiket: String,
    private val harga: Double,
    private var kuota: Int
) {
    private var terjual: Int = 0

    fun getSisaKuota(): Int = kuota - terjual
    fun getHarga(): Double = harga

    // Aturan Bisnis: Cek Sold Out
    fun prosesPenjualan(jumlah: Int): Boolean {
        if (getSisaKuota() >= jumlah) {
            terjual += jumlah
            return true
        }
        return false
    }

    // Fungsi untuk Panitia (Update Kuota)
    fun updateKuota(jumlahBaru: Int) {
        this.kuota = jumlahBaru
        println("Sistem: Kuota berhasil diperbarui menjadi $kuota.")
    }
}

class Panitia(val idPanitia: String, val nama: String) {
    private val passwordAdmin = "bem2026"
    private val masterKodePromo = "KONSERBEM"

    fun login(pass: String): Boolean = pass == passwordAdmin
    fun getValidPromo(): String = masterKodePromo
}

// ==========================================
// 2. MAIN APP (Interaction Layer)
// ==========================================

fun main() {
    val input = Scanner(System.`in`)
    
    // Inisialisasi Data (Database sederhana)
    val tiketKonser = Tiket("T001", "Festival", 150000.0, 5) // Kuota awal 5
    val panitiaBEM = Panitia("P01", "Muhammad Ramdi Pratama")

    while (true) {
        println("\n=== SISTEM TIKETING EVENT BEM ===")
        println("1. Login sebagai Pembeli")
        println("2. Login sebagai Panitia")
        println("0. Keluar")
        print("Pilih Peran: ")

        when (input.nextInt()) {
            1 -> {
                // ALUR PEMBELI
                println("\n--- PENJUALAN TIKET KONSER ---")
                println("Jenis: ${tiketKonser.jenisTiket} | Harga: Rp${tiketKonser.getHarga()}")
                println("Sisa Kuota: ${tiketKonser.getSisaKuota()}")

                print("Masukkan Nama Anda: ")
                val nama = input.next()
                print("Jumlah tiket yang dibeli: ")
                val jumlah = input.nextInt()

                // VALIDASI ATURAN BISNIS 1: Kuota
                if (tiketKonser.prosesPenjualan(jumlah)) {
                    print("Masukkan Kode Promo (ketik 'none' jika tidak ada): ")
                    val promo = input.next()

                    var totalHarga = tiketKonser.getHarga() * jumlah
                    
                    // VALIDASI ATURAN BISNIS 2: Kode Promo
                    if (promo.lowercase() != "none") {
                        if (promo.uppercase() == panitiaBEM.getValidPromo()) {
                            println("✅ Kode Promo Berhasil! Diskon 20% diterapkan.")
                            totalHarga *= 0.8
                        } else {
                            println("❌ Kode promo tidak valid atau sudah kadaluarsa!")
                            // Sesuai aturan bisnis, harga tetap normal atau bisa dibatalkan
                        }
                    }

                    println("\n--- BUKTI PEMBELIAN ---")
                    println("Nama Pembeli: $nama")
                    println("Total Bayar : Rp$totalHarga")
                    println("Status      : BERHASIL")
                } else {
                    println("\n❌ Maaf, tiket sudah sold out atau kuota tidak mencukupi!")
                }
            }

            2 -> {
                // ALUR PANITIA
                print("\nMasukkan Password Panitia: ")
                val pass = input.next()

                if (panitiaBEM.login(pass)) {
                    println("✅ Login Berhasil. Selamat bertugas, ${panitiaBEM.nama}!")
                    println("1. Update Kuota Tiket")
                    println("2. Cek Laporan Kuota")
                    print("Pilih Aksi: ")
                    
                    when (input.nextInt()) {
                        1 -> {
                            print("Masukkan jumlah kuota baru: ")
                            val baru = input.nextInt()
                            tiketKonser.updateKuota(baru)
                        }
                        2 -> println("Sisa Kuota Saat Ini: ${tiketKonser.getSisaKuota()}")
                    }
                } else {
                    println("❌ Password Salah! Akses Ditolak.")
                }
            }
            0 -> break
            else -> println("Pilihan tidak tersedia.")
        }
    }
    println("Sistem Berhenti. Terima kasih!")
}