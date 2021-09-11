package com.demirli.a42gamesuggestionapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.demirli.a42gamesuggestionapp.R
import com.demirli.a42gamesuggestionapp.model.Game
import com.demirli.a42gamesuggestionapp.model.Poll
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.game_item.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.GzipSource
import org.json.JSONObject

class MainActivity : AppCompatActivity(), PollListAdapter.SetOnUserPollClick, GameListAdapter.SetOnUserVoteClick {

    private lateinit var viewModel: MainViewModel

    private lateinit var gson: Gson

    private var gameNameList =  ArrayList<String>()

    private var gameListForPoll = ArrayList<Game>()

    private lateinit var adapterAutoComplateText: ArrayAdapter<String>

    private lateinit var gameListAdapter: GameListAdapter

    private var pollForVote: Poll? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            viewModel = MainViewModel(application)

        gson = Gson()

        setAutoComplateTextAdapter(gameNameList)

        setOnTextChangeListener()

        gameList_recyclerView.layoutManager = LinearLayoutManager(this)
        gameListAdapter = GameListAdapter(gameListForPoll,null,this)
        gameList_recyclerView.adapter = gameListAdapter

        viewModel.getAllPolls().observe(this, Observer {

            val pollListAdapter = PollListAdapter(it, this)
            pollList_recyclerView.layoutManager = LinearLayoutManager(this)
            pollList_recyclerView.adapter = pollListAdapter

        })

        add_btn.setOnClickListener {

            val game = Game(games_ac_textView.text.toString(), 0f)
            gameListForPoll.add(game)
            gameListAdapter.notifyDataSetChanged()
        }

        create_poll_btn.setOnClickListener {

            val question = question_editText.text.toString()

            val gameListString = gson.toJson(gameListForPoll)

            val poll = Poll(question = question, gameListString = gameListString)

            viewModel.insertPoll(poll)
        }

        create_new_poll_btn.setOnClickListener {
            create_poll_btn.visibility = View.VISIBLE
            linearLayout.visibility = View.VISIBLE
            create_new_poll_btn.visibility = View.GONE


            question_editText.setText("")
            gameListForPoll.clear()
            pollForVote = null

            gameListAdapter = GameListAdapter(gameListForPoll, null,this)
            gameList_recyclerView.adapter = gameListAdapter
            gameListAdapter.notifyDataSetChanged()
        }
    }

    fun setOnTextChangeListener(){
        games_ac_textView.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                gameNameList.clear()

                val rb = RequestBody.create(MediaType.parse("text/plain"),"fields name; search \"${s.toString()}\";")

                viewModel.getGames(rb)?.observe(this@MainActivity, Observer {

                    val gameNameList = ArrayList<String>()
                    for(i in it){
                        gameNameList.add(i.name)
                    }

                    setAutoComplateTextAdapter(gameNameList)
                    adapterAutoComplateText.notifyDataSetChanged()
                })

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                setAutoComplateTextAdapter(gameNameList)
                adapterAutoComplateText.notifyDataSetChanged()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setAutoComplateTextAdapter(gameNameList)
                adapterAutoComplateText.notifyDataSetChanged()
            }
        })
    }

    fun setAutoComplateTextAdapter(gameNameList: List<String>){
        adapterAutoComplateText = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item, gameNameList)
        games_ac_textView.setAdapter(adapterAutoComplateText)
    }

    override fun onUserPollClick(poll: Poll) {

        gameListForPoll.clear()

        pollForVote = poll

        create_poll_btn.visibility = View.GONE
        linearLayout.visibility = View.GONE
        create_new_poll_btn.visibility = View.VISIBLE

        var alreadyExecuted = false

        viewModel.getSinglePoll(poll.pollId).observe(this, Observer {

            if(!alreadyExecuted) {

                question_editText.setText(it.question)

                val typeOftT = object : TypeToken<ArrayList<Game>>(){}.type
                val gameList = gson.fromJson<ArrayList<Game>>(it.gameListString, typeOftT)

                gameListForPoll = gameList

                gameListAdapter = GameListAdapter(gameListForPoll,null,this)
                gameList_recyclerView.adapter = gameListAdapter
                alreadyExecuted = true;
            }
        })

        gameListAdapter.notifyDataSetChanged()
    }

    override fun onUserVoteClick(votedPosition: Int, votedButton: Button) {

        if(pollForVote != null){

            val typeOftT = object : TypeToken<ArrayList<Game>>(){}.type
            val gameList = gson.fromJson<ArrayList<Game>>(pollForVote!!.gameListString, typeOftT)

            gameList[votedPosition].vote ++

            val gameListString = gson.toJson(gameList)

            gameListForPoll = gameList

            gameListAdapter = GameListAdapter(gameListForPoll, votedPosition,this)
            gameList_recyclerView.adapter = gameListAdapter


            viewModel.insertPoll(Poll(
                pollForVote!!.pollId,
                pollForVote!!.question,
                gameListString))

        }else{
            Toast.makeText(this, "Henüz kayıt oluşturmadığınızdan oy veremezsiniz.", Toast.LENGTH_LONG).show()
        }

        votedButton.visibility = View.INVISIBLE
        gameListAdapter.notifyDataSetChanged()

    }
}
