package com.tyagiabhinav.imagesearch.ui.gridscreen;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tyagiabhinav.imagesearch.R;
import com.tyagiabhinav.imagesearch.model.database.Images;
import com.tyagiabhinav.imagesearch.ui.fullscreen.FullscreenActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private Context context;
    private final LayoutInflater inflater;
    private List<Images> imagesList; // Cached copy of words
    private int lastInsertedPosition = 0;

    RecyclerViewAdapter(Context context) {
        Log.d(TAG, "RecyclerViewAdapter: New Adapter");
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Images image = imagesList.get(position);
        holder.image = image;
        holder.position = position;


        // for caching testing
//        Picasso picasso = Picasso.get();
//        picasso.setIndicatorsEnabled(true);
//        picasso.load(image.getUrl()+"_t.jpg")
//                .placeholder(R.drawable.ic_image_placeholder_24dp)
//                .error(R.drawable.ic_image_error_24dp)
//                .into(holder.photo);
        Picasso.get()
                .load(image.getUrl() + "_q.jpg")
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.ic_image_placeholder_24dp)
                .error(R.drawable.ic_image_error_24dp)
//                .resize(145, 14514530)
                .into(holder.photo, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        //Try again online if cache failed
                        Picasso.get()
                                .load(image.getUrl() + "_q.jpg")
                                .error(R.drawable.ic_image_error_24dp)
                                .placeholder(R.drawable.ic_image_placeholder_24dp)
                                .into(holder.photo, new Callback() {
                                    @Override
                                    public void onSuccess() {}

                                    @Override
                                    public void onError(Exception e) {
                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });

        // grid item click listener
        holder.mView.setOnClickListener((v) -> handleOnClickListener(v, holder));
    }

    void setImages(List<Images> imageList) {
        imagesList = imageList;
//        Log.d(TAG, "setImages: itemCount->" + this.getItemCount());
        if (getItemCount() > lastInsertedPosition) {
            notifyDataSetChanged();
        }
//        notifyItemRangeChanged(lastInsertedPosition, getItemCount());
        lastInsertedPosition = getItemCount();
    }

    void clearImages() {
        if (imagesList != null && !imagesList.isEmpty()) {
            imagesList.clear();
            lastInsertedPosition = 0;
            notifyDataSetChanged();
        }
    }

    // getItemCount() is called many times, and when it is first called,
// mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (imagesList != null)
            return imagesList.size();
        else return 0;
    }

    private void handleOnClickListener(View v, ViewHolder holder) {
        Log.d(TAG, "handleOnClickListener: " + holder.image.getUrl());
        Log.d(TAG, "handleOnClickListener: holder position->" + holder.position);
        Intent intent = new Intent(context, FullscreenActivity.class);
        intent.putExtra(FullscreenActivity.URL, holder.image.getUrl());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((GridActivity) context, (View) holder.photo, "pic");
        context.startActivity(intent, options.toBundle());
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;

        @BindView(R.id.photo)
        ImageView photo;

        private Images image;
        private int position;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}