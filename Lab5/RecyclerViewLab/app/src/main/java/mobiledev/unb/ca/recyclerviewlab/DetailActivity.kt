package mobiledev.unb.ca.recyclerviewlab

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // TODO 1
        //  Get the intent that started this activity, and get the extras from it
        //  corresponding to the title and description of the course
        val intent = intent

        val title = intent.getStringExtra("title").toString()
        val desc = intent.getStringExtra("description").toString()

        // TODO 2
        //  Set the description TextView to be the course description
        val textView = findViewById<TextView>(R.id.description_textview)
        textView.setText(desc)

        // TODO 3
        //  Make the TextView scrollable
        //  HINT: Look at the movementMethod attribute for descTextView
        textView.movementMethod = ScrollingMovementMethod()

        // TODO 4
        //  Set the title of the action bar to be the course title
        //  HINT:
        //   This might help you - http://developer.android.com/reference/android/support/v7/app/AppCompatActivity.html#getSupportActionBar%28%29
        supportActionBar?.title = title
    }
}