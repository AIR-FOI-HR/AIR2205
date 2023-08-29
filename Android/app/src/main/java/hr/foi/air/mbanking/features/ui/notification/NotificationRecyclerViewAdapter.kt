package hr.foi.air.mbanking.features.ui.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.mbanking.R
import hr.foi.air.mbanking.databinding.LayoutListItemButtonBinding
import hr.foi.air.mbanking.features.domain.models.Notification

class NotificationRecyclerViewAdapter(
    context: Context, private val onNotificationClickListener: OnNotificationClickListener
) : RecyclerView.Adapter<NotificationViewHolder>() {

    private var notifications: List<Notification> = listOf()
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflatedBinding: LayoutListItemButtonBinding =
            LayoutListItemButtonBinding.inflate(layoutInflater, parent, false)
        return NotificationViewHolder(
            inflatedBinding,
            onNotificationClickListener
        )
    }

    override fun getItemCount(): Int = notifications.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    fun swapList(newNotifications: List<Notification>) {
        val diffCallBack = NotificationListDiffCallback(this.notifications, newNotifications)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        notifications = newNotifications
        diffResult.dispatchUpdatesTo(this)
    }
}


class NotificationViewHolder(
    private val binding: LayoutListItemButtonBinding,
    private val onNotificationClickListener: OnNotificationClickListener
) : RecyclerView.ViewHolder(binding.root) {
    val context: Context = itemView.context
    fun bind(model: Notification) {
        binding.buttonItem.apply {
            text = context.getString(R.string.notifikacija_1_d, model.obavijest_id, model.datum)
            setOnClickListener { onNotificationClickListener.onNotificationClicked(model.obavijest_id) }
        }
    }
}

class NotificationListDiffCallback(
    private val oldItem: List<Notification>,
    private val newItem: List<Notification>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItem.size

    override fun getNewListSize(): Int = newItem.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem[oldItemPosition] == newItem[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem == newItem
}

interface OnNotificationClickListener {
    fun onNotificationClicked(notificationId: Int)
}
