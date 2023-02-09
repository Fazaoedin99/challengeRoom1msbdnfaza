package com.example.challengeroom1msbdnfaza


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengeroom1msbdnfaza.room.*
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Intent as Intent

class MainActivity : AppCompatActivity() {

    val db by lazy { NoteDB(this) }
    lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListener()
        setupRecyclerview()
    }

    override fun onStart() {
        super.onStart()
        loadNote()

        }
        fun loadNote(){
            CoroutineScope(Dispatchers.IO).launch {
                val notes = db.noteDao().getNotes()
                Log.d("MainActivity","dbResponse: $notes")
                withContext(Dispatchers.Main){
                    noteAdapter.setData( notes )
                }
            }
        }

    fun setupListener(){
        button_create.setOnClickListener {
           intentEdit(0,Constant.TYPE_CREATE)


        }

     }
    fun intentEdit(noteId: Int, intentType: Int) {
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", noteId)
                .putExtra("intent_type", intentType)
        )
    }
    fun setupRecyclerview(){
        noteAdapter = NoteAdapter(arrayListOf(),object  :NoteAdapter.OnAdapterListener{
            override fun onClick(note: Note) {
                // read detail note
                intentEdit(note.id,Constant.TYPE_READ)
            }

            override fun onUpdate(note: Note) {
                intentEdit(note.id,Constant.TYPE_UPDATE)
            }

            override fun onDelete(note: Note) {
                deletedialog(note)
                }

        })
        list_siswa.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }
    private fun deletedialog(note :Note){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin Hapus ${note.title}?")
            setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()

            }
            setPositiveButton("Hapus") { dialogInterface, i ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote(note)
                    dialogInterface.dismiss()
                    loadNote()
                }
            }
        }
        alertDialog.show()
    }
}


