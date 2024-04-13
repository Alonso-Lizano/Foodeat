package com.ren.renshomemadefood.content.models;

import com.ren.renshomemadefood.R;

import java.util.ArrayList;
import java.util.List;

public class LangData {

    public static List<Lang> getLangList() {
        List<Lang> langList = new ArrayList<>();

        Lang spanish = new Lang();
        spanish.setName("Spanish");
        spanish.setImage(R.drawable.espana);
        langList.add(spanish);

        Lang english = new Lang();
        english.setName("English");
        english.setImage(R.drawable.estados_unidos);
        langList.add(english);

        return langList;
    }

}
