package com.example.gamehub.ui.admin.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.repository.PrefsRepository

class AdminUsersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_users, container, false)
        recyclerView = view.findViewById(R.id.rvUsers)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val prefsRepository = PrefsRepository(requireContext())
        val users = prefsRepository.getUsers()
        adapter = AdminUsersAdapter(users)

        recyclerView.adapter = adapter

        return view
    }
}
