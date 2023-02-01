package hr.foi.air.mbanking.bankorderRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.mbanking.R
import hr.foi.air.mbanking.entities.Transaction

class BankOrderAdapter (private val bankOrders: List<Transaction>): RecyclerView.Adapter<BankOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankOrderViewHolder {
        return BankOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_bankorder,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BankOrderViewHolder, position: Int) {
        val currBankOrder = bankOrders[position]
        holder.bindDataToView(currBankOrder)
    }

    override fun getItemCount(): Int {
        return bankOrders.size
    }
}