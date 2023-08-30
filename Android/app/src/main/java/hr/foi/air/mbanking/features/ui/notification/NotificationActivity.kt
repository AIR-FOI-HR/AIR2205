package hr.foi.air.mbanking.features.ui.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import hr.foi.air.mbanking.databinding.ActivityNotificationsBinding

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity(), OnNotificationClickListener {
    private lateinit var binding: ActivityNotificationsBinding
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var notificationRecyclerViewAdapter: NotificationRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerViews()
        observeNotifications()
        observeNotification()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerViews() {
        binding.notificationRecyclerViewAdapter.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            notificationRecyclerViewAdapter =
                NotificationRecyclerViewAdapter(
                    context,
                    this@NotificationActivity
                )
            adapter = notificationRecyclerViewAdapter
        }
    }

    private fun observeNotifications() {
        viewModel.fetchNotificationsLiveData.observe(this) {
            if (it != null) {
                notificationRecyclerViewAdapter.swapList(it)
            }
        }
    }

    private fun observeNotification() {
        viewModel.fetchNotificationLiveData.observe(this) {
            if (it != null) {
                showDialog(it.obavijest_id, it.datum, it.sadrzaj)
            }
        }
    }

    private fun showDialog(title: Int, date: String, content: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Notifikacija: $title")
            .setMessage("Datum: $date\n\n$content")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, NotificationActivity::class.java)
    }

    override fun onNotificationClicked(notificationId: Int) {
        viewModel.fetchNotification(notificationId)
    }
}