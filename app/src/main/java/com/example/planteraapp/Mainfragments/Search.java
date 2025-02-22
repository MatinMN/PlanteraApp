package com.example.planteraapp.Mainfragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planteraapp.AppDatabase;
import com.example.planteraapp.MyPlant;
import com.example.planteraapp.R;
import com.example.planteraapp.Utilities.SearchAdapter;
import com.example.planteraapp.entities.DAO.PlantDAO;
import com.example.planteraapp.entities.Relations.PlantsWithEverything;

import java.util.List;
import java.util.stream.Collectors;

public class Search extends Fragment implements SearchAdapter.SearchItemClickListener {

    // Plants fetched from DB when view created
    private List<PlantsWithEverything> allPlants;

    // Filtered by name when user types in search bar
    private List<PlantsWithEverything> filteredPlants;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;

    public Search() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        PlantDAO plantDAO = AppDatabase.getInstance(getContext()).plantDAO();
        allPlants = plantDAO.getAllPlantsWithEverything();
        recyclerView = view.findViewById(R.id.search_list);
        // View to display when list search result is empty
        emptyView = view.findViewById(R.id.empty_view);
        EditText searchBar = view.findViewById(R.id.search_bar);
        // Calling it initially to display all plants
        filter_plants("");
        // Filter plants every keystroke instead of pressing enter
        // This way is much smoother and responsive
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter_plants(searchBar.getText().toString().toLowerCase());
            }
        });

        return view;
    }

    public void filter_plants(String txt) {
        filteredPlants = allPlants.stream().filter(
                p -> p.plant.plantName.toLowerCase()
                        .contains(txt)).collect(Collectors.toList());
        if (filteredPlants.size() == 0)
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
        recyclerView.setAdapter(new SearchAdapter(filteredPlants, Search.this, requireContext()));
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getContext(), MyPlant.class);
        intent.putExtra("plantName", filteredPlants.get(position).plant.plantName);
        startActivity(intent);
    }
}
