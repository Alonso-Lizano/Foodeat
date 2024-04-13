package com.ren.renshomemadefood.content.screens.drawermenu;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.adapters.LangAdapter;
import com.ren.renshomemadefood.content.models.LangData;


public class SettingsScreen extends AppCompatActivity {

    private Spinner langSpinner;
    private LangAdapter langAdapter;
    private TextView idTema;
    private ImageView backBtnSettings;
    private TextView idPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);

        langSpinner = findViewById(R.id.idSpinnerLang);
        idTema = findViewById(R.id.idTheme);
        backBtnSettings = findViewById(R.id.backBtnSettings);
        idPrivacyPolicy = findViewById(R.id.idPrivacyPolicy);

        langAdapter = new LangAdapter(this, LangData.getLangList());
        langSpinner.setAdapter(langAdapter);

        idPrivacyPolicy.setOnClickListener(v -> Toast.makeText(this, "Privacy",
                Toast.LENGTH_SHORT).show());
        onClickThemeBtn(idTema);
        backBtnSettings.setOnClickListener(v -> finish());

        //String currentLanguage = Locale.getDefault().getLanguage();
        //setTextViewText(currentLanguage);
    }

    private void onClickThemeBtn(TextView textView) {
        textView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select theme")
                    .setItems(new CharSequence[]{"System", "Light Mode", "Dark Mode"}, (dialog, which) -> {
                        switch (which) {
                            case 0: // Sistema
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                break;
                            case 1: // Modo Claro
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                break;
                            case 2: // Modo Oscuro
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                break;
                        }
                        recreate();
                    });
            builder.create().show();
        });
    }

    // Método para establecer el texto del TextView según el idioma
    /*private void setTextViewText(String language) {
        switch (language) {
            case "es":
                idTema.setText(R.string.tema);
                break;
            default:
                idTema.setText(R.string.theme);
                break;
        }
    }*/
}