package hr.foi.air.mbanking.features.ui.useraccounts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import hr.foi.air.mbanking.databinding.ActivityUserAccountsBinding
import hr.foi.air.mbanking.features.domain.models.UserAccount
import hr.foi.air.mbanking.features.ui.userAccountDetails.UserAccountDetailsActivity

@AndroidEntryPoint
class UserAccountsActivity : AppCompatActivity(),
    OnUserAccountClickListener {
    private lateinit var binding: ActivityUserAccountsBinding
    private val viewModel: UserAccountsViewModel by viewModels()
    private lateinit var userAccountsRecyclerViewAdapter: UserAccountsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userIban = intent.getStringExtra(USER_IBAN)
        initRecyclerViews()
        viewModel.fetchUserAccounts(userIban ?: "")
        observeUserAccounts()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerViews() {
        binding.recyclerViewMyAccounts.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            userAccountsRecyclerViewAdapter =
                UserAccountsRecyclerViewAdapter(
                    context,
                    this@UserAccountsActivity
                )
            adapter = userAccountsRecyclerViewAdapter
        }
    }

    private fun observeUserAccounts() {
        viewModel.fetchUserAccountsLiveData.observe(this) {
            if (it != null) {
                userAccountsRecyclerViewAdapter.swapList(it)
            }
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, UserAccountsActivity::class.java)
        const val USER_IBAN = "user_iban_extra"
    }

    override fun onUserAccountClicked(userAccount: UserAccount) {
        val intent = UserAccountDetailsActivity.createIntent(this)
        intent.putExtra(UserAccountDetailsActivity.ACCOUNT_TYPE, userAccount.vrsta_racuna_id)
        startActivity(intent)
    }
}