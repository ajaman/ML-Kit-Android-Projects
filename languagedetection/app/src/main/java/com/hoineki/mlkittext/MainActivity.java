package com.hoineki.mlkittext;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button view_lan;
    TextView lan;
    EditText edit_lan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_lan = findViewById(R.id.view_lan);
        lan = findViewById(R.id.lan);
        edit_lan = findViewById(R.id.edit_lan);
        view_lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lan_str = edit_lan.getText().toString();
                if (!lan_str.isEmpty()){
                    showLan(lan_str);
                }
            }
        });
    }
    private void showLan(String lan_str){
        FirebaseLanguageIdentification languageIdentifier =
                FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        languageIdentifier.identifyLanguage(lan_str)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                String out_str;
                                if (languageCode != "und") {
                                    out_str = languageCode;
                                    Locale loc = new Locale(out_str);
                                    lan.setText(loc.getDisplayLanguage());
                                } else {
                                    out_str = "Can't identify language.";
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be loaded or other internal error.
                                // ...
                            }
                        });
    }

}
