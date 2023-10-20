package com.example.myproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home_fragment : Fragment() {

    lateinit var gridView: GridView
    lateinit var dataList: ArrayList<Data_Upload>
    lateinit var adapter: MyAdapter
    private var mDatabaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
    private lateinit var searchView: SearchView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_fragment, container, false)
        gridView = view.findViewById(R.id.gridView)
        dataList = ArrayList<Data_Upload>()
        adapter = MyAdapter(dataList, requireContext())
        gridView.adapter = (adapter)
        searchView = view.findViewById(R.id.searchView)

        mDatabaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (dataSnapshot in snapshot.children) {
                    val dataUpload = dataSnapshot.getValue(Data_Upload::class.java)
                    //step 4 delete button
                    dataUpload?.key = dataSnapshot.key;
                    if (dataUpload != null) {
                        dataList.add(dataUpload)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                searchList(query)
                return true
            }
        })

        return view
    }


    fun searchList(text: String) {
        val searchList = ArrayList<Data_Upload>()
        for (dataUpload in dataList) {
            if (dataUpload.getuTitle().lowercase().contains(text.lowercase())) {
                searchList.add(dataUpload)
            }
        }
        adapter.searchDataList(searchList)
    }

}