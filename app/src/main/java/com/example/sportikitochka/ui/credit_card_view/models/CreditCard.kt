//package com.example.sportikitochka.ui.credit_card_view.models
//
//import com.example.sportikitochka.ui.credit_card_view.extensions.isNumeric
//import java.util.Calendar
//
//open class CreditCard : Cloneable {
//    /**
//     * Name of the holder on the front of the credit card
//     */
//    var holder: String
//
//    /**
//     * Digits on the front of the credit card
//     */
//    var number: String
//        set(value) {
//            field = value
//
//            brand = Brand.parse(value)
//        }
//
//    /**
//     * 3 digits number on the back of the credit card
//     */
//    var cvv: String
//
//    /**
//     * Expiration date on the back of the credit card
//     */
//    var expiry: String
//
//    /**
//     * Pin code used to confirm payments or withdraw money,
//     *
//     * This code is not necessary used, you should ask the user this code only if you're
//     * building a credit card vault or generic credential vault
//     */
//    var pinCode: String
//
//    /**
//     * Brand of the credit card, this field is read-only and will change based on the [number]
//     */
//    var brand: Brand
//        private set
//
//    constructor(card: CreditCard) : this(
//        card.holder,
//        card.number,
//        card.cvv,
//        card.expiry,
//        card.pinCode
//    )
//
//    @JvmOverloads
//    constructor(
//        holder: String = "",
//        number: String = "",
//        cvv: String = "",
//        expiry: String = "",
//        pinCode: String = ""
//    ) {
//        this.brand = Brand.GENERIC
//        this.holder = holder
//        this.number = number
//        this.cvv = cvv
//        this.expiry = expiry
//        this.pinCode = pinCode
//    }
//
//    /**
//     * Checks if the card's number only contains numbers
//     */
//    fun isNumberValid(): Boolean {
//        return number.isNumeric()
//    }
//
//    /**
//     * Checks if the expiry is valid, both month and year must start from 1
//     */
//    fun isExpiryValid(): Boolean {
//        val c = Calendar.getInstance()
//        val month: Int
//        val year: Int
//
//        return try {
//            month = expiry.substring(0, 2).toInt()
//            year = expiry.substring(2, 4).toInt()
//
//            year >= c.get(Calendar.YEAR) % 100 && month in 1..12
//        } catch (e: Exception) {
//            false
//        }
//    }
//
//    /**
//     * Checks if the CVV is valid, a valid CVV must be 3 digits long and contain numbers only
//     */
//    fun isCvvValid(): Boolean {
//        return cvv.length == 3 && cvv.isNumeric()
//    }
//
//    override fun toString(): String {
//        return "Holder: $holder, Number: $number, Expiry: $expiry, CVV: $cvv"
//    }
//
//    override fun equals(other: Any?): Boolean {
//        other?.let {
//            it as CreditCard
//
//            return it.holder == holder &&
//                    it.number == number &&
//                    it.cvv == cvv &&
//                    it.expiry == expiry
//        }
//
//        return false
//    }
//
//    override fun clone(): Any {
//        return CreditCard(
//            holder,
//            number,
//            cvv,
//            expiry
//        )
//    }
//}