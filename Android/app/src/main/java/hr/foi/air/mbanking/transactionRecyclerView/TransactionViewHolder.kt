package hr.foi.air.mbanking.transactionRecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.mbanking.R
import hr.foi.air.mbanking.entities.Transaction
import hr.foi.air.mbanking.entities.Transaction1

class TransactionViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    var transactionName = itemView.findViewById<TextView>(R.id.transaction)

    fun bindDataToView(transaction: Transaction){
        transactionName?.text = transaction.opis_placanja.plus("    ").plus(transaction.iznos.toString()).plus(" EUR")
    }
}