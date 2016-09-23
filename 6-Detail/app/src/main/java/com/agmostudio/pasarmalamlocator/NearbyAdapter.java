/*
 * MIT License
 *
 * Copyright (c) 2016. Agmo Studio
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agmostudio.pasarmalamlocator;


import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.MyViewHolder> {

    List<PasarMalam> pasarMalamList = new ArrayList<>();
    Location currentLocation;

    public void replaceAll(List<PasarMalam> list) {
        pasarMalamList.clear();
        pasarMalamList.addAll(list);
        sortList(); // our list has changed, sort the list
        notifyDataSetChanged(); // notify the UI that the data has changed, rebind the data again
    }

    public void setCurrentLocation(Location location) {
        currentLocation = location;
        sortList(); // our location has changed, sort the list
        notifyDataSetChanged();
    }

    private void sortList() {
        // dont need to sort if both criteria is not valid
        if (currentLocation == null || pasarMalamList.isEmpty()) return;

        Collections.sort(pasarMalamList, new Comparator<PasarMalam>() {

            @Override
            public int compare(PasarMalam o1, PasarMalam o2) {
                return Float.compare( // check which pasar is nearer by using distance
                        o1.calculateDistance(currentLocation), // my distance to pasar 1
                        o2.calculateDistance(currentLocation)); // my distance to pasar 2
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create our View from XML
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_nearby, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        // listen to user clicking the list item
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the pasar malam that user clicked in the list
                PasarMalam pasarMalam = pasarMalamList.get(viewHolder.getAdapterPosition());
                DetailActivity.start(v.getContext(), pasarMalam);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // get the PasarMalam that is being displayed in the UI
        PasarMalam pasarMalam = pasarMalamList.get(position);

        Glide.with(holder.image.getContext())
                .load(pasarMalam.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(holder.image);
        holder.name.setText(pasarMalam.name);
        holder.hours.setText(pasarMalam.operationHour);

        if (currentLocation == null) {
            // reset the text
            holder.distance.setText(null);
        } else {
            float distanceMeter = pasarMalam.calculateDistance(currentLocation);
            String text;
            if (distanceMeter < 1000f) {
                // less than 1km
                text = String.format(Locale.ENGLISH, "%.2f m", distanceMeter);
            } else {
                // 1km or more
                text = String.format(Locale.ENGLISH, "%.2f km", distanceMeter / 1000f);
            }
            holder.distance.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return pasarMalamList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView distance;
        TextView hours;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            distance = (TextView) itemView.findViewById(R.id.distance);
            hours = (TextView) itemView.findViewById(R.id.hours);
        }

    }
}
