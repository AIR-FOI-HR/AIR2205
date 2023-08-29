package hr.foi.air.mbanking.bankorderRecyclerView

import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.mbanking.LogInActivity.Companion.currentUser
import hr.foi.air.mbanking.R
import hr.foi.air.mbanking.entities.Transaction

class BankOrderViewHolder (itemView : View, listener: BankOrderAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView) {
    var dateTime = itemView.findViewById<TextView>(R.id.datetime)
    var nameAndSurname = itemView.findViewById<TextView>(R.id.nameandsurname)
    var racunIban = itemView.findViewById<TextView>(R.id.accountiban)
    var description = itemView.findViewById<TextView>(R.id.description)
    var amount = itemView.findViewById<TextView>(R.id.amount)
    var imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

    fun bindDataToView(transaction: Transaction){
        dateTime?.text = transaction.datum_izvrsenja.subSequence(5, transaction.datum_izvrsenja.length)
        nameAndSurname?.text = currentUser!!.ime.plus(" ").plus(currentUser!!.prezime)
        racunIban?.text = transaction.platitelj_iban
        description?.text = transaction.opis_placanja
        amount?.text = transaction.iznos.toString()
    }

    init {
        imageButton.setOnClickListener{
            listener.onItemClick(adapterPosition)
        }
    }

}