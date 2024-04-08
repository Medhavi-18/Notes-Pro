package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {
    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;

    String title,content,docId;
    boolean isEditMode =false;
    TextView deleteNoteTextViewBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText =findViewById(R.id.notes_title_text);
        contentEditText =findViewById(R.id.notes_content_text);
        saveNoteBtn =findViewById(R.id.save_note_btn);
        pageTitleTextView =findViewById(R.id.page_title);
        deleteNoteTextViewBtn =findViewById(R.id.delete_text_view_btn);


        //Receive data -> for viewing again with existing data in it
        title =getIntent().getStringExtra("title");
        content =getIntent().getStringExtra("content");
        docId =getIntent().getStringExtra("docId");


        //checking condition for updating data => (directly from clicking on note)
        if(docId !=null && !docId.isEmpty()){
            isEditMode =true;

        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        //we can only edit note by clicking on note, when we click on "+" -> we can add new note
        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener((v) -> saveNote() );

        //for deleting note from Firebase
        deleteNoteTextViewBtn.setOnClickListener((v) -> deleteNoteFromFirebase());


    }

    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent =contentEditText.getText().toString();

        //validate Data
        if(noteTitle == null || noteTitle.isEmpty()){
            titleEditText.setError("Title is requird");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        //calling function
        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        //if it is in editMode we can edit and save edited note.
        if(isEditMode){
            //UPDATE NOTE
            documentReference =Utility.getCollectionReferenceForNotes().document(docId);
        }
        else{
            //creating new note.
            //(IN my_notes creating our note)
            //CREATE NEW NOTE
            documentReference =Utility.getCollectionReferenceForNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(NoteDetailsActivity.this,"Note added Sucessfully");
                    finish();
                }
                else{
                    //note is not added
                    Utility.showToast(NoteDetailsActivity.this,"Failed while adding note");
                }
            }
        });
    }

    //DELETING NOTES
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;

        //UPDATE NOTE
        documentReference =Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(NoteDetailsActivity.this,"Note Deleted Sucessfully");
                    finish();
                }
                else{
                    //note is not added
                    Utility.showToast(NoteDetailsActivity.this,"Failed while deleting note");
                }
            }
        });
    }

}