package com.example.atlasai;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextToSpeech tts;
    private SpeechRecognizer recognizer;
    private TextView status, transcript;
    private boolean ttsReady = false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(3, 8, 24));
        getWindow().setNavigationBarColor(Color.rgb(3, 8, 24));
        buildUi();

        tts = new TextToSpeech(this, result -> {
            if (result == TextToSpeech.SUCCESS) {
                int language = tts.setLanguage(new Locale("tr", "TR"));
                ttsReady = language != TextToSpeech.LANG_MISSING_DATA
                        && language != TextToSpeech.LANG_NOT_SUPPORTED;
                if (ttsReady) speak("ATLAS çevrimiçi. Seni dinlemeye hazırım.");
                else status.setText("Türkçe ses paketi telefonda bulunamadı. Metin modu hazır.");
            }
        });
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new SimpleRecognitionListener(
                text -> handleSpeech(text),
                () -> status.setText("Seni anlayamadım. Tekrar deneyebilirsin.")
            ));
        } else {
            status.setText("Bu cihazda konuşma tanıma hizmeti bulunamadı.");
        }
    }

    private void buildUi() {
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setGravity(Gravity.CENTER_HORIZONTAL);
        page.setPadding(28, 38, 28, 24);
        page.setBackgroundColor(Color.rgb(3, 8, 24));

        TextView title = new TextView(this);
        title.setText("A T L A S");
        title.setTextColor(Color.rgb(90, 210, 255));
        title.setTextSize(29);
        title.setGravity(Gravity.CENTER);
        title.setLetterSpacing(0.18f);
        page.addView(title, new LinearLayout.LayoutParams(-1, 55));

        TextView sub = new TextView(this);
        sub.setText("PERSONAL AI  •  JARVIS EDITION");
        sub.setTextColor(Color.rgb(116, 161, 196));
        sub.setTextSize(11);
        sub.setGravity(Gravity.CENTER);
        page.addView(sub, new LinearLayout.LayoutParams(-1, 34));

        HoloView holo = new HoloView(this);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(300, 300);
        hp.topMargin = 18; hp.bottomMargin = 18;
        page.addView(holo, hp);

        status = new TextView(this);
        status.setText("Başlatılıyor...");
        status.setTextColor(Color.rgb(110, 225, 255));
        status.setTextSize(15);
        status.setGravity(Gravity.CENTER);
        page.addView(status, new LinearLayout.LayoutParams(-1, 42));

        transcript = new TextView(this);
        transcript.setText("ATLAS: Merhaba. Komutunu bekliyorum.");
        transcript.setTextColor(Color.WHITE);
        transcript.setTextSize(15);
        transcript.setGravity(Gravity.CENTER);
        transcript.setPadding(10, 12, 10, 12);
        page.addView(transcript, new LinearLayout.LayoutParams(-1, 90));

        Button listen = new Button(this);
        listen.setText("🎙  ATLAS İLE KONUŞ");
        listen.setTextOn("DİNLENİYOR");
        listen.setTextColor(Color.WHITE);
        listen.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(0, 112, 170)));
        page.addView(listen, new LinearLayout.LayoutParams(-1, 58));
        listen.setOnClickListener(v -> startListening());

        TextView note = new TextView(this);
        note.setText("İlk sürüm • Yerel komutlar ve sesli yanıt\nSürekli uyandırma ve çevrimiçi yapay zekâ sonraki aşamada eklenecek.");
        note.setTextColor(Color.rgb(120, 145, 170));
        note.setTextSize(11);
        note.setGravity(Gravity.CENTER);
        note.setPadding(8, 22, 8, 0);
        page.addView(note, new LinearLayout.LayoutParams(-1, -2));

        setContentView(page);
    }

    private void startListening() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 7);
            return;
        }
        if (recognizer == null) {
            status.setText("Konuşma tanıma kullanılamıyor.");
            return;
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "tr-TR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "ATLAS seni dinliyor...");
        status.setText("Dinliyorum...");
        recognizer.startListening(intent);
    }

    private void handleSpeech(String text) {
        transcript.setText("SEN: " + text);
        String q = text.toLowerCase(new Locale("tr", "TR"));
        String answer;
        if (q.contains("merhaba") || q.contains("selam")) answer = "Merhaba. Ben ATLAS. Sana nasıl yardımcı olabilirim?";
        else if (q.contains("saat")) answer = "Saat bilgisini telefonunun durum çubuğundan görebilirsin.";
        else if (q.contains("adın") || q.contains("kimsin")) answer = "Ben ATLAS. Kişisel yapay zekâ asistanınım.";
        else if (q.contains("nasılsın")) answer = "Teşekkür ederim. Sistemlerim hazır.";
        else if (q.contains("teşekkür")) answer = "Rica ederim. Yardımcı olmak görevim.";
        else answer = "Bu komut henüz çevrimdışı sürümde tanımlı değil. İnternetli yapay zekâ bağlantısını sonraki sürümde ekleyeceğiz.";
        transcript.setText("SEN: " + text + "\n\nATLAS: " + answer);
        status.setText("Hazır");
        speak(answer);
    }

    private void speak(String text) {
        if (tts != null && ttsReady) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "atlas_reply");
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == 7 && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) startListening();
        else status.setText("Mikrofon izni verilmedi.");
    }

    @Override protected void onDestroy() {
        if (recognizer != null) recognizer.destroy();
        if (tts != null) { tts.stop(); tts.shutdown(); }
        super.onDestroy();
    }

    interface SpeechCallback { void onText(String text); }
    interface ErrorCallback { void onError(); }

    static class SimpleRecognitionListener implements android.speech.RecognitionListener {
        private final SpeechCallback callback; private final ErrorCallback error;
        SimpleRecognitionListener(SpeechCallback c, ErrorCallback e) { callback=c; error=e; }
        public void onResults(Bundle b) {
            ArrayList<String> r = b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (r != null && !r.isEmpty()) callback.onText(r.get(0)); else error.onError();
        }
        public void onError(int e) { error.onError(); }
        public void onReadyForSpeech(Bundle b) {}
        public void onBeginningOfSpeech() {}
        public void onRmsChanged(float r) {}
        public void onBufferReceived(byte[] b) {}
        public void onEndOfSpeech() {}
        public void onPartialResults(Bundle b) {}
        public void onEvent(int t, Bundle b) {}
    }

    static class HoloView extends View {
        Paint p = new Paint(3);
        HoloView(Activity c) { super(c); }
        @Override protected void onDraw(Canvas c) {
            super.onDraw(c);
            float x=getWidth()/2f, y=getHeight()/2f;
            p.setStyle(Paint.Style.STROKE);
            for (int i=0;i<7;i++) {
                p.setStrokeWidth(i%2==0?2:1);
                p.setColor(Color.argb(180-i*18, 45, 190, 255));
                c.drawCircle(x,y,48+i*17,p);
            }
            p.setStyle(Paint.Style.FILL);
            p.setShader(new RadialGradient(x,y,92,
                new int[]{Color.argb(95,0,190,255),Color.argb(18,0,90,180),Color.TRANSPARENT},
                null,Shader.TileMode.CLAMP));
            c.drawCircle(x,y,92,p);
            p.setShader(null);
            p.setColor(Color.rgb(145,235,255));
            p.setTextAlign(Paint.Align.CENTER);
            p.setTextSize(24);
            p.setTypeface(Typeface.create("sans-serif-light",Typeface.NORMAL));
            c.drawText("ATLAS",x,y+8,p);
            p.setTextSize(9);
            p.setColor(Color.rgb(90,175,220));
            c.drawText("SYSTEM ONLINE",x,y+28,p);
        }
    }
}
