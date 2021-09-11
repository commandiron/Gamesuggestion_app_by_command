package com.demirli.a42gamesuggestionapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.demirli.a42gamesuggestionapp.R
import com.demirli.a42gamesuggestionapp.model.Game

class GameListAdapter(var gameList: List<Game>, var selectedPosition: Int?, var setOnUserVoteClick: SetOnUserVoteClick): RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = gameList.size

    override fun onBindViewHolder(holder: GameListAdapter.ViewHolder, position: Int) {

        holder.gameName.setText(gameList[position].name)
        holder.gameVote.setText(gameList[position].vote.toString() + " Vote")

        var voteSum = 0f
        for(i in gameList){
            voteSum += i.vote
        }

        if(voteSum != 0f){

            holder.gameVotePercentage.setText(String.format("%.2f", gameList[position].vote/voteSum*100) + " %")
        }

        if(selectedPosition == null){
            holder.voteBtn.visibility = View.VISIBLE
        }else{
            holder.voteBtn.visibility = View.INVISIBLE
        }

        holder.voteBtn.setOnClickListener{

            setOnUserVoteClick.onUserVoteClick(position, holder.voteBtn)
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val gameName = view.findViewById<TextView>(R.id.game_name_textView)
        val gameVote = view.findViewById<TextView>(R.id.game_vote_textView)
        val gameVotePercentage = view.findViewById<TextView>(R.id.game_votePercentage_textView)
        val voteBtn = view.findViewById<Button>(R.id.vote_btn)
    }

    interface SetOnUserVoteClick{
        fun onUserVoteClick(votedPosition: Int, votedButton: Button)
    }
}