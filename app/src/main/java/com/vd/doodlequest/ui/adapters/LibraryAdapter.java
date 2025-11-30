package com.vd.doodlequest.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vd.doodlequest.models.LibraryItem;
import com.vd.doodlequest.R;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    private final List<LibraryItem> doodleList;

    public LibraryAdapter(List<LibraryItem> doodleList) {
        this.doodleList = doodleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_library_doodle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LibraryItem item = doodleList.get(position);

        if (item.isUnlocked()) {
            String drawableName = item.getName()
                    .toLowerCase()
                    .replace(" ", "_");

            int resId = holder.itemView.getContext()
                    .getResources()
                    .getIdentifier(
                            drawableName,
                            "drawable",
                            holder.itemView.getContext().getPackageName()
                    );

            if (resId != 0) {
                holder.imageView.setImageResource(resId);
            }

            holder.textView.setText(item.getName());
        } else {
            holder.imageView.setImageResource(R.drawable.ic_lock);
            holder.textView.setText("Locked");
        }
    }

    @Override
    public int getItemCount() {
        return doodleList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            textView = itemView.findViewById(R.id.itemName);
        }
    }
}
