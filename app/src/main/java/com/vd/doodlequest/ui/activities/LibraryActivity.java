package com.vd.doodlequest.ui.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.vd.doodlequest.ui.adapters.LibraryAdapter;
import com.vd.doodlequest.models.LibraryItem;
import com.vd.doodlequest.R;
import com.vd.doodlequest.data.DoodleProgressRepository;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private LibraryAdapter adapter;
    private List<LibraryItem> doodleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (getSupportActionBar() != null) getSupportActionBar().hide();
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        window.setNavigationBarColor(getResources().getColor(android.R.color.black));

        setContentView(R.layout.activity_library);

        MaterialToolbar toolbar = findViewById(R.id.libraryToolbar);
        toolbar.setElevation(0f);
        toolbar.setOutlineProvider(null);
        toolbar.setBackgroundColor(Color.parseColor("#8BC34A"));
        toolbar.setNavigationIconTint(Color.parseColor("#FFFFFF"));
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.libraryRecycler);
        recyclerView.setVisibility(android.view.View.INVISIBLE);

        int screenWidthPx = getResources().getDisplayMetrics().widthPixels;
        int paddingPx = (int) (16 * getResources().getDisplayMetrics().density);
        int estimatedAvailableWidth = screenWidthPx - paddingPx;
        int itemMinWidthPx = (int) (150 * getResources().getDisplayMetrics().density);
        int initialSpanCount = Math.max(2, estimatedAvailableWidth / itemMinWidthPx);

        recyclerView.setLayoutManager(new GridLayoutManager(this, initialSpanCount));

        DoodleProgressRepository progress = new DoodleProgressRepository(this);
        String[] classes = progress.getAllItemsArray();

        doodleList = new ArrayList<>();
        int unlockedCount = 0;

        for (String item : classes) {
            boolean unlocked = progress.isEverythingDiscovered() || progress.isDiscovered(item);
            if (unlocked) unlockedCount++;
            doodleList.add(new LibraryItem(capitalizeWords(item), unlocked));
        }

        adapter = new LibraryAdapter(doodleList);
        recyclerView.setAdapter(adapter);

        recyclerView.post(() -> {
            int availableWidth = recyclerView.getWidth()
                    - recyclerView.getPaddingLeft()
                    - recyclerView.getPaddingRight();

            int spanCount = Math.max(2, availableWidth / itemMinWidthPx);

            if (spanCount != initialSpanCount) {
                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(spanCount);
                adapter.notifyDataSetChanged();
            }

            recyclerView.setVisibility(android.view.View.VISIBLE);
        });

        TextView counterView = findViewById(R.id.libraryCounter);
        counterView.setText(unlockedCount + "/" + classes.length);
    }

    private String capitalizeWords(String str) {
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }
}
