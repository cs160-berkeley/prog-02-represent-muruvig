package com.example.vignesh.represent;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vignesh on 3/11/16.
 */
public class ListAdapter extends ArrayAdapter {
    private Activity context;
    private HashMap<Integer, HashMap<String, String>> map;

    public ListAdapter(Activity context, List<Integer> positions, HashMap<Integer, HashMap<String, String>> all_rep_map) {
        super(context, R.layout.candidate, positions);
        this.map = all_rep_map;
//        this.allConcat = allConcat;
        this.context = context;
//        this.candidateNames = candidateNames;
//        this.candidateTweets = candidateTweets;
//        this.website = website;
//        this.candidateImages = candidateImages;
//        this.email = email;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.candidate, null, true);
//
        TextView candidateName = (TextView) rowView.findViewById(R.id.name);
        candidateName.setText(map.get(position).get("title") + ". " + map.get(position).get("name"));
        TextView candidateEmail = (TextView) rowView.findViewById(R.id.email);
        candidateEmail.setText(map.get(position).get("email"));
        TextView candidateWebsite = (TextView) rowView.findViewById(R.id.website);
        candidateWebsite.setText(map.get(position).get("website"));
        TextView candidateTweet = (TextView) rowView.findViewById(R.id.tweet);
        candidateTweet.setText(map.get(position).get("tweet"));
        ImageView imageView = (ImageView) rowView.findViewById(R.id.photo);
        Picasso.with(context).load(map.get(position).get("photo_url")).into(imageView);

        return rowView;
    }
}
