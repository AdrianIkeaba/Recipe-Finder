package com.example.recipefinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText


class MainActivity : AppCompatActivity() {
    private lateinit var searchButton: Button
    private lateinit var searchTermEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.searchButton)
        searchTermEdit = findViewById(R.id.searchTermEdt)

        searchButton.setOnClickListener {
            val intent = Intent(this, Recipe_List::class.java)
            val searchTerm = searchTermEdit.text.toString().trim()
            intent.putExtra("Search", searchTerm)
            startActivity(intent)
        }
    }
}