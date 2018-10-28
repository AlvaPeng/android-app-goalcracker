package com.example.dell.v;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ChallengeAdapter  extends ArrayAdapter{
    private final int resourceId;

    public ChallengeAdapter(Context context, int textViewResourceId, List<ChallengeItem> items)
    {
        super(context, textViewResourceId, items);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ChallengeItem item = (ChallengeItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        //ImageView challenge_icon = view.findViewById(R.id.challenge_icon);
        //TextView challenge_text = view.findViewById(R.id.challenge_text);
        ImageView challenge_bg = view.findViewById(R.id.challenge_bg);
        //challenge_icon.setImageResource(item.getIconId());
        //challenge_text.setText(item.getChallengeName());
        challenge_bg.setImageResource(item.getChallengeBG());
        return view;
    }

}
