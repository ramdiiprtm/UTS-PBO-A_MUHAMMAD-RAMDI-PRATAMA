// Entitas Tiket
data class Ticket(
    val id: Int,
    val name: String,
    var stock: Int,
    val price: Double
)

// Entitas Pembeli
data class Buyer(
    val name: String,
    val promoCode: String?
)

// Entitas Panitia
class Committee(private val validPromoCodes: Map<String, Boolean>) {

    fun isPromoValid(code: String?): Boolean {
        if (code == null) return true // tidak pakai promo tetap valid
        return validPromoCodes[code] == true
    }
}

// Sistem Ticketing
class TicketingSystem(
    private val ticket: Ticket,
    private val committee: Committee
) {

    fun buyTicket(buyer: Buyer, quantity: Int) {
        println("Pembeli: ${buyer.name}")

        // Cek stok tiket
        if (ticket.stock < quantity) {
            println("❌ Pembelian ditolak: Tiket sudah habis (Sold Out)")
            return
        }

        // Cek kode promo
        if (!committee.isPromoValid(buyer.promoCode)) {
            println("❌ Pembelian ditolak: Kode promo salah atau kadaluarsa")
            return
        }

        // Proses pembelian
        ticket.stock -= quantity
        val totalPrice = ticket.price * quantity

        println("✅ Pembelian berhasil!")
        println("Jumlah tiket: $quantity")
        println("Total harga: Rp $totalPrice")
        println("Sisa tiket: ${ticket.stock}")
    }
}

// Main Program
fun main() {
    // Data tiket
    val ticket = Ticket(
        id = 1,
        name = "Konser BEM 2026",
        stock = 5,
        price = 100000.0
    )

    // Data promo (true = valid, false = kadaluarsa)
    val promoList = mapOf(
        "DISKON10" to true,
        "EXPIRED" to false
    )

    val committee = Committee(promoList)
    val system = TicketingSystem(ticket, committee)

    // Simulasi pembeli
    val buyer1 = Buyer("Ramdi", "DISKON10")
    val buyer2 = Buyer("Andi", "SALAH")
    val buyer3 = Buyer("Budi", null)

    println("=== Transaksi 1 ===")
    system.buyTicket(buyer1, 2)

    println("\n=== Transaksi 2 ===")
    system.buyTicket(buyer2, 1)

    println("\n=== Transaksi 3 ===")
    system.buyTicket(buyer3, 4) // melebihi stok
}