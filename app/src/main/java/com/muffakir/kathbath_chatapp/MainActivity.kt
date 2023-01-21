package com.muffakir.kathbath_chatapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.muffakir.kathbath_chatapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userList : ArrayList<User>
    private lateinit var mAuth : FirebaseAuth
    private lateinit var adapter : UserAdapter
    private lateinit var mdbRef : DatabaseReference
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        mAuth = FirebaseAuth.getInstance()
        mdbRef = FirebaseDatabase.getInstance().getReference()





        userList = ArrayList()
        adapter = UserAdapter(this,userList)
        userRecyclerView = findViewById(R.id.rcmain)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter



        mdbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (mAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout_btn) {
            mAuth.signOut()
            val intent = Intent(this,Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        else if (item.itemId == R.id.share) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"Hey I am using KATH BATH First kashmiri App\nThrough which you can chat with your \nFreinds and Family\nGet This app here  link ")
            startActivity(Intent.createChooser(intent,"Select App to Continue!"))

        }
        else if (item.itemId == R.id.about_us) {
            val BrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/muffakiribnhamid"))
            startActivity(BrowserIntent)
        }
        return true
    }
}