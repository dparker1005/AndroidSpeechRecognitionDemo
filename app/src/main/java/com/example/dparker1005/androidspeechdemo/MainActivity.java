package com.example.dparker1005.androidspeechdemo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    protected SpeechRecognizer speechRecognizer;
    private final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        ActivityCompat.requestPermissions(this, permissions,REQUEST_RECORD_AUDIO_PERMISSION);
        if ( SpeechRecognizer.isRecognitionAvailable(this) ) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecogListener());
        } else {
            this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));
            speechRecognizer.setRecognitionListener(new RecogListener());
        }
    }

    public void start_speech(View view) {
            if ( speechRecognizer != null ) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                speechRecognizer.startListening(intent);
            }
    }

    class RecogListener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Button start_speech_button = findViewById(R.id.bt_start_speech);
            start_speech_button.setText("Ready to listen...");
        }

        @Override
        public void onBeginningOfSpeech() {
            Button start_speech_button = findViewById(R.id.bt_start_speech);
            start_speech_button.setText("Listening...");
        }

        @Override
        public void onRmsChanged(float rmsdB) {System.out.println(rmsdB);}

        @Override
        public void onBufferReceived(byte[] buffer) {}

        @Override
        public void onEndOfSpeech() {
            Button start_speech_button = findViewById(R.id.bt_start_speech);
            start_speech_button.setText("Click and Name a Color");
        }

        @Override
        public void onError(int error) {
            System.out.println("Speech recognition error: " + error);
            Button start_speech_button = findViewById(R.id.bt_start_speech);
            start_speech_button.setText("Click and Name a Color");
        }

        @Override
        public void onResults(Bundle results) {
            // Where you do the color change
            String bestResult = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
            System.out.println("Speech recognition results:  "+ bestResult);
            ConstraintLayout screen = findViewById(R.id.change_my_bacground);
            switch (bestResult.toUpperCase()) {
                case "BLUE":
                    screen.setBackgroundColor(Color.BLUE);
                    break;
                case "RED":
                    screen.setBackgroundColor(Color.RED);
                    break;
                case "YELLOW":
                    screen.setBackgroundColor(Color.YELLOW);
                    break;
                case "GREEN":
                    screen.setBackgroundColor(Color.GREEN);
                    break;
                case "PURPLE":
                    screen.setBackgroundColor(Color.MAGENTA);
                    break;
                case "WHITE":
                    screen.setBackgroundColor(Color.WHITE);
                    break;
                case "BLACK":
                    screen.setBackgroundColor(Color.BLACK);
                    break;
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    }
}
