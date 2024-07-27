//package com.unit_3.sogong_test
//
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.fragment.app.FragmentActivity
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//class FeedRVAdapter(private val items: MutableList<FeedModel>) : RecyclerView.Adapter<FeedRVAdapter.ViewHolder>() {
//    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference.child("feeds")
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val usersDb: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedRVAdapter.ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.feed_rv_item, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: FeedRVAdapter.ViewHolder, position: Int) {
//        holder.bindItems(items[position])
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bindItems(item: FeedModel) {
//            val name = itemView.findViewById<TextView>(R.id.nameTextView)
//            val title = itemView.findViewById<TextView>(R.id.titleTextView)
//            val time = itemView.findViewById<TextView>(R.id.timeTextView)
//            val content = itemView.findViewById<TextView>(R.id.contentTextView)
//            val likeBtn = itemView.findViewById<ImageView>(R.id.likeBtn)
//            val commentBtn = itemView.findViewById<ImageView>(R.id.commentBtn)
//            val likesTextView = itemView.findViewById<TextView>(R.id.likesTextView)
//            val commentsTextView = itemView.findViewById<TextView>(R.id.commentsTextView)
//            val articleTextView = itemView.findViewById<TextView>(R.id.articleTextView)
//            val articleImageView = itemView.findViewById<ImageView>(R.id.articleImageArea)
//
//            // Firebase에서 사용자 닉네임을 조회하여 설정
//            usersDb.child(item.uid).child("nickname").addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val nickname = snapshot.getValue(String::class.java)
//                    name.text = nickname ?: "Unknown"
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // 오류 처리
//                    name.text = "Unknown"
//                }
//            })
//
////            name.text = item.uid
//            title.text = item.title
//            time.text = item.time
//            content.text = item.content
//            likesTextView.text = "좋아요 ${item.likes}개"
//            commentsTextView.text = "댓글 ${item.commentsCnt}개 모두 보기"
//
//            articleTextView.text = item.articleTitle
//
//            // Use Glide to load image and handle visibility
//            if (item.imageUrl.isNullOrEmpty()) {
//                articleImageView.visibility = View.GONE
//            } else {
//                articleImageView.visibility = View.VISIBLE
//                Glide.with(itemView.context).load(item.imageUrl).into(articleImageView)
//            }
//
//
//            // Check if articleTitle is null or empty
//            if (item.articleTitle.isNullOrEmpty()) {
//                articleTextView.visibility = View.GONE
//            } else {
//                articleTextView.visibility = View.VISIBLE
//                articleTextView.text = item.articleTitle
//            }
//
//            val currentUserId = auth.currentUser?.uid
//
//            // Update like button icon based on user's like status
//            if (item.likedUsers.contains(currentUserId)) {
//                likeBtn.setImageResource(R.drawable.heart)
//            } else {
//                likeBtn.setImageResource(R.drawable.favorite)
//            }
//
//            likeBtn.setOnClickListener {
//                if (currentUserId != null) {
//                    if (!item.likedUsers.contains(currentUserId)) {
//                        // 좋아요 추가
//                        item.likes += 1
//                        item.likedUsers.add(currentUserId)
//                        likesTextView.text = "좋아요 ${item.likes}개"
//                        likeBtn.setImageResource(R.drawable.heart)
//                    } else {
//                        // 좋아요 취소
//                        item.likes -= 1
//                        item.likedUsers.remove(currentUserId)
//                        likesTextView.text = "좋아요 ${item.likes}개"
//                        likeBtn.setImageResource(R.drawable.favorite)
//                    }
//                    updateLikesInDatabase(item)
//                }
//            }
//
//            commentBtn.setOnClickListener {
//                val activity = itemView.context as FragmentActivity
//                val commentFragment = CommentBottomSheetDialogFragment.newInstance(item.id)
//                commentFragment.show(activity.supportFragmentManager, commentFragment.tag)
//            }
//
//            commentsTextView.setOnClickListener {
//                val activity = itemView.context as FragmentActivity
//                val commentFragment = CommentBottomSheetDialogFragment.newInstance(item.id)
//                commentFragment.show(activity.supportFragmentManager, commentFragment.tag)
//            }
//
//            articleTextView.setOnClickListener {
//                val context = itemView.context
//                val intent = Intent(context,WebViewActivity::class.java).apply {
//                    putExtra("link", item.link)
//                }
//                context.startActivity(intent)
//            }
//
//
//
//
//        }
//
//        private fun updateLikesInDatabase(item: FeedModel) {
//            val itemRef = db.child(item.id)
//            itemRef.child("likes").setValue(item.likes)
//            itemRef.child("likedUsers").setValue(item.likedUsers)
//                .addOnSuccessListener {
//                    // 성공적으로 업데이트됨
//                }
//                .addOnFailureListener { e ->
//                    // 업데이트 실패
//                }
//        }
//    }
//}

package com.unit_3.sogong_test

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FeedRVAdapter(private val items: MutableList<FeedModel>) : RecyclerView.Adapter<FeedRVAdapter.ViewHolder>() {
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference.child("feeds")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val usersDb: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.feed_rv_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FeedRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: FeedModel) {
            val name = itemView.findViewById<TextView>(R.id.nameTextView)
            val title = itemView.findViewById<TextView>(R.id.titleTextView)
            val time = itemView.findViewById<TextView>(R.id.timeTextView)
            val content = itemView.findViewById<TextView>(R.id.contentTextView)
            val likeBtn = itemView.findViewById<ImageView>(R.id.likeBtn)
            val commentBtn = itemView.findViewById<ImageView>(R.id.commentBtn)
            val likesTextView = itemView.findViewById<TextView>(R.id.likesTextView)
            val commentsTextView = itemView.findViewById<TextView>(R.id.commentsTextView)
            val articleTextView = itemView.findViewById<TextView>(R.id.articleTextView)
            val articleImageView = itemView.findViewById<ImageView>(R.id.articleImageArea)

            // Firebase에서 사용자 닉네임을 조회하여 설정
            usersDb.child(item.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nickname = snapshot.child("nickname").getValue(String::class.java)
                        Log.d("FeedRVAdapter", "Nickname for uid ${item.uid}: $nickname")
                        name.text = nickname ?: "Unknown"
                    } else {
                        Log.d("FeedRVAdapter", "User with uid ${item.uid} does not exist.")
                        name.text = "Unknown"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // 오류 처리
                    Log.e("FeedRVAdapter", "Error fetching nickname for uid ${item.uid}: ${error.message}")
                    name.text = "Unknown"
                }
            })

            title.text = item.title
            time.text = item.time
            content.text = item.content
            likesTextView.text = "좋아요 ${item.likes}개"
            commentsTextView.text = "댓글 ${item.commentsCnt}개 모두 보기"

            articleTextView.text = item.articleTitle

            // Use Glide to load image and handle visibility
            if (item.imageUrl.isNullOrEmpty()) {
                articleImageView.visibility = View.GONE
            } else {
                articleImageView.visibility = View.VISIBLE
                Glide.with(itemView.context).load(item.imageUrl).into(articleImageView)
            }

            // Check if articleTitle is null or empty
            if (item.articleTitle.isNullOrEmpty()) {
                articleTextView.visibility = View.GONE
            } else {
                articleTextView.visibility = View.VISIBLE
                articleTextView.text = item.articleTitle
            }

            val currentUserId = auth.currentUser?.uid

            // Update like button icon based on user's like status
            if (item.likedUsers.contains(currentUserId)) {
                likeBtn.setImageResource(R.drawable.heart)
            } else {
                likeBtn.setImageResource(R.drawable.favorite)
            }

            likeBtn.setOnClickListener {
                if (currentUserId != null) {
                    if (!item.likedUsers.contains(currentUserId)) {
                        // 좋아요 추가
                        item.likes += 1
                        item.likedUsers.add(currentUserId)
                        likesTextView.text = "좋아요 ${item.likes}개"
                        likeBtn.setImageResource(R.drawable.heart)
                    } else {
                        // 좋아요 취소
                        item.likes -= 1
                        item.likedUsers.remove(currentUserId)
                        likesTextView.text = "좋아요 ${item.likes}개"
                        likeBtn.setImageResource(R.drawable.favorite)
                    }
                    updateLikesInDatabase(item)
                }
            }

            commentBtn.setOnClickListener {
                val activity = itemView.context as FragmentActivity
                val commentFragment = CommentBottomSheetDialogFragment.newInstance(item.id)
                commentFragment.show(activity.supportFragmentManager, commentFragment.tag)
            }

            commentsTextView.setOnClickListener {
                val activity = itemView.context as FragmentActivity
                val commentFragment = CommentBottomSheetDialogFragment.newInstance(item.id)
                commentFragment.show(activity.supportFragmentManager, commentFragment.tag)
            }

            articleTextView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, WebViewActivity::class.java).apply {
                    putExtra("link", item.link)
                }
                context.startActivity(intent)
            }
        }

        private fun updateLikesInDatabase(item: FeedModel) {
            val itemRef = db.child(item.id)
            itemRef.child("likes").setValue(item.likes)
            itemRef.child("likedUsers").setValue(item.likedUsers)
                .addOnSuccessListener {
                    // 성공적으로 업데이트됨
                }
                .addOnFailureListener { e ->
                    // 업데이트 실패
                }
        }
    }
}

