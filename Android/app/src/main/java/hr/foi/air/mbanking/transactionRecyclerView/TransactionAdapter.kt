package hr.foi.air.mbanking.transactionRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.mbanking.R
import hr.foi.air.mbanking.entities.Transaction

class TransactionAdapter (private val transtactions: List<Transaction>) : RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_transaction,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currTransaction = transtactions[position]
        holder.bindDataToView(currTransaction)
    }

    override fun getItemCount(): Int {
        return transtactions.size
    }
}