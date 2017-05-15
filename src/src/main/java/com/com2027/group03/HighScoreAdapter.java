package com.com2027.group03;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.list;


/**
 * Created by Lukasz on 14/05/2017.
 */

public class HighScoreAdapter extends ArrayAdapter<String> {


    private final Context context;
    private final List<String> list;
    private final List<List> temp;

    public HighScoreAdapter(Context context, List<String> list) {
        super(context, R.layout.highscorelist,list);
        this.context = context;
        this.list = list;
        this.temp = prepareHighscores(sortMap(assignHighscores(list)));
    }


    static class ViewHolder {
        protected TextView name;
        protected TextView score;
        protected TextView rank;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        List<String> names;
        List<String> scores;

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(context);
            view = inflator.inflate(R.layout.highscorelist, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.hsname);
            viewHolder.score = (TextView) view.findViewById(R.id.hsscore);
            viewHolder.rank = (TextView) view.findViewById(R.id.hsrank);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if (!(position >= temp.get(0).size())){
            holder.name.setText(temp.get(0).get(position).toString());
            holder.rank.setText(String.valueOf(position+1)+")");
            holder.score.setText(temp.get(1).get(position).toString());
        }
        else {
            Log.i("HighScoreAdapter", "Catching trouble");
        }


        return view;
    }
    private HashMap<String,String> assignHighscores(List<String> hs){
        HashMap<String,String> highscores = new HashMap<String, String>();
        String[] temp;
        for (int i = 0; i < hs.size(); i++){
            temp = hs.get(i).split(",");
            highscores.put(temp[0],temp[1]);
            Log.i("assignHighscores",temp[0]+" "+" "+temp[1]);
        }
        return highscores;
    }
    private HashMap<String,String> sortMap(HashMap<String,String> hs){
        MapSort ms = new MapSort();
        return ms.sortByValues(hs);
    }
    private List<List> prepareHighscores(HashMap<String,String> hs){
        List<List> temp = new ArrayList<List>();
        List<String> names = new ArrayList<String>();
        List<String> scores = new ArrayList<String>();
        for ( Map.Entry<String, String> entry : hs.entrySet()) {
            if(!(names.contains(entry.getKey()))){
                names.add(entry.getKey());
                scores.add(entry.getValue());
            }
            else {
                //Do nothing
            }

        }
        hs.clear();
        temp.add(names);
        temp.add(scores);
        return temp;
    }
}

