package com.example.firebase_pt.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firebase_pt.Friend
import com.example.firebase_pt.MessageActivity
import com.example.firebase_pt.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private var friend : ArrayList<Friend> = arrayListOf()

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //프레그먼트를 포함하고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        database = Firebase.database.reference
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.home_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = RecyclerViewAdapter()


//      val recyclerView : RecyclerView = view!!.findViewById<RecyclerView>(R.id.home_recycler)
//      recyclerView.adapter = RecyclerViewAdapter()
//      recyclerView.layoutManager = LinearLayoutManager(inflater.context) //this는 액티비티에서 사용가능 inflater.context는 됨

        return view
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {

        init {
            val myUid = Firebase.auth.currentUser?.uid.toString()
            FirebaseDatabase.getInstance().reference.child("users").addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    friend.clear()
                    for(data in snapshot.children){
                        val item = data.getValue<Friend>()
                        if(item?.uid.equals(myUid)) { continue } // 본인은 친구창에서 제외
                        friend.add(item!!)
                    }
                    notifyDataSetChanged()
                }
            })
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            return CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false))
        }

        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.home_item_iv)
            val textView : TextView = itemView.findViewById(R.id.home_item_tv)
            val textViewEmail : TextView = itemView.findViewById(R.id.home_item_email)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            Glide.with(holder.itemView.context).load(friend[position].profileImageUrl)
                .apply(RequestOptions().circleCrop())
                .into(holder.imageView)
            holder.textView.text = friend[position].name
            holder.textViewEmail.text = friend[position].email

            holder.itemView.setOnClickListener{
                val intent = Intent(context, MessageActivity::class.java)
                intent.putExtra("destinationUid", friend[position].uid)
                context?.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return friend.size
        }
    }

    companion object{
        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }
}