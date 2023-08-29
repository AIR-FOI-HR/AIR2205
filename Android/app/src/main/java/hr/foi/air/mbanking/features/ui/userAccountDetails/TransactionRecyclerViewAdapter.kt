package hr.foi.air.mbanking.features.ui.userAccountDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.mbanking.R
import hr.foi.air.mbanking.databinding.LayoutListItemButtonBinding
import hr.foi.air.mbanking.entities.Transaction


class TransactionRecyclerViewAdapter(
    context: Context, private val onTransactionClickListener: OnTransactionClickListener
) : RecyclerView.Adapter<TransactionViewHolder>() {

    private var transactions: List<Transaction> = listOf()
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val inflatedBinding: LayoutListItemButtonBinding =
            LayoutListItemButtonBinding.inflate(layoutInflater, parent, false)
        return TransactionViewHolder(
            inflatedBinding,
            onTransactionClickListener
        )
    }

    override fun getItemCount(): Int = transactions.size


    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    fun swapList(newTransactions: List<Transaction>) {
        val diffCallBack = UserAccountListDiffCallback(this.transactions, newTransactions)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        transactions = newTransactions
        diffResult.dispatchUpdatesTo(this)
    }
}

class TransactionViewHolder(
    private val binding: LayoutListItemButtonBinding,
    private val onTransactionClickListener: OnTransactionClickListener
) : RecyclerView.ViewHolder(binding.root) {
    val context: Context = itemView.context
    fun bind(model: Transaction) {
        binding.buttonItem.apply {
            text = context.getString(R.string.transakcija_1_d, model.transakcija_id)
            setOnClickListener { onTransactionClickListener.onTransactionClicked(model.transakcija_id) }
        }
    }
}


class UserAccountListDiffCallback(
    private val oldItem: List<Transaction>,
    private val newItem: List<Transaction>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItem.size

    override fun getNewListSize(): Int = newItem.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem[oldItemPosition] == newItem[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem == newItem
}

interface OnTransactionClickListener {
    fun onTransactionClicked(transactionId: Int)
}