package com.example.note_z.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.note_z.R
import com.example.note_z.data.Priority
import com.example.note_z.data.TodoData
import com.example.note_z.databinding.RowLayoutBinding

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<TodoData>() {
        override fun areItemsTheSame(oldItem: TodoData, newItem: TodoData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoData, newItem: TodoData): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffCallback)

    fun setData(todoData: List<TodoData>) {
        asyncListDiffer.submitList(todoData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position], holder.itemView, holder.itemView.context)
    }

    inner class NotesViewHolder(private val binding: RowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todoData: TodoData, view: View, context: Context) {
            binding.tvTitle.text = todoData.title
            binding.tvDesc.text = todoData.description
            binding.rowBackground.setOnClickListener {
                view.findNavController().navigate(R.id.action_listFragment_to_updateFragment)
            }
            when (todoData.priority) {
                Priority.HIGH -> binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                )

                Priority.LOW -> binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )

                Priority.MEDIUM -> binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.yellow
                    )
                )
            }
        }
    }
}
