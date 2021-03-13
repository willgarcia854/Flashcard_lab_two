package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        ImageView cancelCardButton =  findViewById(R.id.floatingCancelButton);
        ImageView addInputButton = findViewById(R.id.floatingSaveButton);

        String question = getIntent().getStringExtra("question");
        String answer = getIntent().getStringExtra("answer");

        EditText newQuestion = findViewById(R.id.questionInput);
        newQuestion.setText(question);
        EditText newAnswer = findViewById(R.id.answerInput);
        newAnswer.setText(answer);




        cancelCardButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View View){
                finish();
            }
        });

        addInputButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View View){
                String newQuestion = ((EditText) findViewById(R.id.questionInput)).getText().toString();
                String newAnswer = ((EditText) findViewById(R.id.answerInput)).getText().toString();
                Intent data = new Intent();
                data.putExtra("question", newQuestion);
                data.putExtra("answer", newAnswer);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}