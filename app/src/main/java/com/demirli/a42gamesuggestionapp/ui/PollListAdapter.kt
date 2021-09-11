package com.demirli.a42gamesuggestionapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demirli.a42gamesuggestionapp.R
import com.demirli.a42gamesuggestionapp.model.Poll

class PollListAdapter(var pollList: List<Poll>, var setOnUserPollClick: SetOnUserPollClick): RecyclerView.Adapter<PollListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.poll_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = pollList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pollName.setText(pollList[position].question)

        holder.pollName.setOnClickListener {
            setOnUserPollClick.onUserPollClick(pollList[position])
        }

    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val pollName = view.findViewById<Button>(R.id.poll_name_btn)
    }

    interface SetOnUserPollClick{
        fun onUserPollClick(polll: Poll)
    }
}