package id.idprogramming.githubapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.idprogramming.githubapp.data.model.UserModel
import id.idprogramming.githubapp.databinding.ItemUserBinding

class MainAdapter : ListAdapter<UserModel, MainAdapter.RecyclerViewHolder>(DiffCallback) {

    private var actionAdapter: ActionAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val bind = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RecyclerViewHolder(bind)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun addList(list: List<UserModel>?) {
        if (list != null) {
            submitList(list)
        } else {
            submitList(emptyList())
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: ActionAdapter) {
        actionAdapter = onItemClickCallback
    }

    inner class RecyclerViewHolder(private val bind: ItemUserBinding) :
        RecyclerView.ViewHolder(bind.root) {

        fun bind(data: UserModel) {
            bind.tvUsername.text = data.username

            Glide.with(itemView.context)
                .load(data.urlAvatar)
                .into(bind.ivProfile)

            itemView.setOnClickListener {
                actionAdapter!!.onItemClick(data)
            }
        }
    }

    interface ActionAdapter {
        fun onItemClick(data: UserModel)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem == newItem
        }
    }
}
