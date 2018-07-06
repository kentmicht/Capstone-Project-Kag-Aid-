package com.example.kagaid.kagaid.Camera;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kagaid.kagaid.R;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class GalleryAdapter extends CursorAdapter {

    public GalleryAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.memory_list_item, viewGroup, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();

        GalleryImg galleryImg = new GalleryImg(cursor);

        holder.titleTextView.setText(galleryImg.getTitle());
        holder.imageView.setImageBitmap(galleryImg.getImage());
    }

    private class ViewHolder {
        final ImageView imageView;
        final TextView titleTextView;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.list_item_image_view);
            titleTextView = view.findViewById(R.id.list_item_text_view);
        }
    }
}
