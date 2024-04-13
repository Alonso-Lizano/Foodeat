package com.ren.renshomemadefood.content.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ren.renshomemadefood.R;
import com.ren.renshomemadefood.content.models.Lang;

import java.util.List;

public class LangAdapter extends BaseAdapter {

    private Context context;
    private List<Lang> langList;

    public LangAdapter(Context context, List<Lang> langList) {
        this.context = context;
        this.langList = langList;
    }

    @Override
    public int getCount() {
        return langList != null ? langList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_lang_items, null);
        }

        TextView langName = view.findViewById(R.id.langName);
        ImageView langImg = view.findViewById(R.id.langImg);

        langName.setText(langList.get(position).getName());
        langImg.setImageResource(langList.get(position).getImage());

        return view;
    }
}
