package com.boonya.ben.firebasecamerademo.adapter.recycler;

import com.boonya.ben.firebasecamerademo.adapter.recycler.ViewHolder.ImagePreviewViewHolder;
import com.boonya.ben.firebasecamerademo.model.ImageInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by Boonya Kitpitak on 3/30/17.
 */

public class ImagePreviewAdapter extends FirebaseRecyclerAdapter<ImageInfo, ImagePreviewViewHolder> {

    public ImagePreviewAdapter(Class<ImageInfo> modelClass, int modelLayout, Class<ImagePreviewViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(ImagePreviewViewHolder viewHolder, ImageInfo model, int position) {
        viewHolder.setImageDescription(model.getImageDecription());
        viewHolder.setImageTitle(model.getImageName());
        viewHolder.loadImage(model.getImageUrl());
    }
}
