package com.boonya.ben.firebasecamerademo.adapter.recycler.ViewHolder;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boonya.ben.firebasecamerademo.R;
import com.bumptech.glide.Glide;

/**
 * Created by Boonya Kitpitak on 3/30/17.
 */

public class ImagePreviewViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView imageTitle;
    private TextView imageDescription;

    public ImagePreviewViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.circleImageView);
        imageTitle = (TextView) itemView.findViewById(R.id.image_title);
        imageDescription = (TextView) itemView.findViewById(R.id.image_description);
    }

    public void loadImage(String imageUri) {
        Glide.with(imageView.getContext())
                .load(imageUri)
                .placeholder(new ColorDrawable(ContextCompat.getColor(imageView.getContext(), R.color.grey)))
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }

    public void setImageTitle(String title) {
        imageTitle.setText(title);
    }

    public void setImageDescription(String description) {
        imageDescription.setText(description);
    }
}
