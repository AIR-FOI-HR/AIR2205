package hr.foi.air.mbanking.features.ui.useraccounts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.mbanking.R
import hr.foi.air.mbanking.databinding.LayoutUserAccountsListBinding
import hr.foi.air.mbanking.features.domain.models.UserAccount

class UserAccountsRecyclerViewAdapter(
    context: Context, private val onUserAccountClickListener: OnUserAccountClickListener
) : RecyclerView.Adapter<UserAccountsViewHolder>() {

    private var userAccounts: List<UserAccount> = listOf()
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAccountsViewHolder {
        val inflatedBinding: LayoutUserAccountsListBinding =
            LayoutUserAccountsListBinding.inflate(layoutInflater, parent, false)
        return UserAccountsViewHolder(
            inflatedBinding,
            onUserAccountClickListener
        )
    }

    override fun getItemCount(): Int = userAccounts.size

    override fun onBindViewHolder(holder: UserAccountsViewHolder, position: Int) {
        holder.bind(userAccounts[position])
    }

    fun swapList(newUserAccount: List<UserAccount>) {
        val diffCallBack = UserAccountListDiffCallback(this.userAccounts, newUserAccount)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        userAccounts = newUserAccount
        diffResult.dispatchUpdatesTo(this)
    }
}


class UserAccountsViewHolder(
    private val binding: LayoutUserAccountsListBinding,
    private val onUserAccountClickListener: OnUserAccountClickListener
) : RecyclerView.ViewHolder(binding.root) {
    val context: Context = itemView.context
    fun bind(model: UserAccount) {

        binding.txtIban.text = context.getString(R.string.iban, model.iban)
        binding.txtStanje.text = context.getString(R.string.stanje_1_f, model.stanje)
        binding.txtAktivnost.text = context.getString(R.string.aktivnost_1_s, model.aktivnost)
        binding.txtVrstaRacuna.text = context.getString(R.string.vrsta_ra_una_1_s, model.vrsta_racuna.typeName)

        binding.root.setOnClickListener {
            onUserAccountClickListener.onUserAccountClicked(model)
        }
    }
}

class UserAccountListDiffCallback(
    private val oldItem: List<UserAccount>,
    private val newItem: List<UserAccount>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItem.size

    override fun getNewListSize(): Int = newItem.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem[oldItemPosition] == newItem[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem == newItem
}

interface OnUserAccountClickListener {
    fun onUserAccountClicked(userAccount: UserAccount)
}