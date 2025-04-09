package com.example.final_project;
// The folloiwng code was created with the assistance of Artificial Intelligence/LLMs
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VenueBrowseFragment extends Fragment {
    private RecyclerView venueRecyclerView;
    private EditText searchEditText;
    private ImageButton clearSearchButton;
    private TabLayout sportTabs;
    private VenueAdapter venueAdapter;
    private Map<String, List<VenueItem>> venuesBySport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_browse, container, false);

        // Initialize views
        venueRecyclerView = view.findViewById(R.id.venueRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        clearSearchButton = view.findViewById(R.id.clearSearchButton);
        sportTabs = view.findViewById(R.id.sportTabs);

        // Setup RecyclerView
        venueRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        venueAdapter = new VenueAdapter();
        venueRecyclerView.setAdapter(venueAdapter);

        // Initialize venue data
        initializeVenueData();

        // Setup listeners
        setupTabListener();
        setupSearchListener();
        clearSearchButton.setOnClickListener(v -> searchEditText.setText(""));

        return view;
    }

    private void initializeVenueData() {
        venuesBySport = new HashMap<>();

        // Basketball venues
        List<VenueItem> basketballVenues = new ArrayList<>();
        basketballVenues.add(new VenueItem("Downtown Arena", "123 Main St", 300, 2.5f, "Available Today From 5PM-7PM", R.drawable.court));
        basketballVenues.add(new VenueItem("Community Center", "456 Oak Ave", 200, 2.5f, "Available Tomorrow At 6:00PM", R.drawable.court));
        basketballVenues.add(new VenueItem("Sports Complex", "789 Pine Rd", 250, 9.0f, "Fully Booked Until Sunday", R.drawable.court));
        venuesBySport.put("Basketball", basketballVenues);

        // Soccer venues
        List<VenueItem> soccerVenues = new ArrayList<>();
        soccerVenues.add(new VenueItem("City Stadium", "321 Park Lane", 400, 3.0f, "Available Today From 3PM-9PM", R.drawable.park));
        soccerVenues.add(new VenueItem("Soccer Center", "654 Green St", 350, 4.2f, "Available Tomorrow All Day", R.drawable.park));
        soccerVenues.add(new VenueItem("Training Ground", "987 Field Ave", 275, 5.5f, "Limited Availability", R.drawable.park));
        venuesBySport.put("Soccer", soccerVenues);

        // Hockey venues
        List<VenueItem> hockeyVenues = new ArrayList<>();
        hockeyVenues.add(new VenueItem("Ice Palace", "147 Frost Rd", 500, 3.8f, "Available Today From 2PM-6PM", R.drawable.rink));
        hockeyVenues.add(new VenueItem("Winter Arena", "258 Cold St", 450, 6.1f, "Available After 8PM", R.drawable.rink));
        hockeyVenues.add(new VenueItem("Hockey Center", "369 Ice Ave", 425, 7.3f, "Weekend Slots Available", R.drawable.rink));
        venuesBySport.put("Hockey", hockeyVenues);

        // Baseball venues
        List<VenueItem> baseballVenues = new ArrayList<>();
        baseballVenues.add(new VenueItem("City Diamond", "111 Baseball St", 350, 2.0f, "Available Today From 4PM-8PM", R.drawable.diamond));
        baseballVenues.add(new VenueItem("Community Field", "222 Diamond Ave", 300, 3.5f, "Available Tomorrow At 5:00PM", R.drawable.diamond));
        venuesBySport.put("Baseball", baseballVenues);

        // Football venues
        List<VenueItem> footballVenues = new ArrayList<>();
        footballVenues.add(new VenueItem("National Stadium", "333 Football Rd", 500, 4.0f, "Available Today From 1PM-5PM", R.drawable.field));
        footballVenues.add(new VenueItem("Local Field", "444 Gridiron St", 450, 5.0f, "Available Tomorrow All Day", R.drawable.field));
        venuesBySport.put("Football", footballVenues);

        // Set initial data
        venueAdapter.setVenues(venuesBySport.get("Basketball"));
    }

    private void setupTabListener() {
        sportTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String sport = tab.getText().toString();
                venueAdapter.setVenues(venuesBySport.get(sport));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase();
                String selectedSport = sportTabs.getTabAt(sportTabs.getSelectedTabPosition()).getText().toString();
                List<VenueItem> allVenues = venuesBySport.get(selectedSport);
                List<VenueItem> filteredVenues = new ArrayList<>();

                if (allVenues != null) {
                    for (VenueItem venue : allVenues) {
                        if (venue.name.toLowerCase().contains(query) ||
                            venue.address.toLowerCase().contains(query)) {
                            filteredVenues.add(venue);
                        }
                    }
                }
                venueAdapter.setVenues(filteredVenues);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private static class VenueItem {
        String name;
        String address;
        int pricePerHour;
        float distance;
        String availability;
        int imageResource;

        VenueItem(String name, String address, int pricePerHour, float distance, String availability, int imageResource) {
            this.name = name;
            this.address = address;
            this.pricePerHour = pricePerHour;
            this.distance = distance;
            this.availability = availability;
            this.imageResource = imageResource;
        }
    }

    private class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder> {
        private List<VenueItem> venues = new ArrayList<>();

        void setVenues(List<VenueItem> venues) {
            this.venues = venues != null ? venues : new ArrayList<>();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_venue, parent, false);
            return new VenueViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
            VenueItem venue = venues.get(position);
            holder.venueImage.setImageResource(venue.imageResource);
            holder.venueNameText.setText(venue.name);
            holder.venueAddressText.setText(venue.address);
            holder.venuePriceText.setText(String.format("$%d/hr", venue.pricePerHour));
            holder.venueDistanceText.setText(String.format("%.1f km", venue.distance));

            holder.itemView.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putString("venueName", venue.address);
                args.putInt("venuePrice", venue.pricePerHour);
                VenueReservationFragment venueReservationFragment = new VenueReservationFragment();
                venueReservationFragment.setArguments(args);
                requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, venueReservationFragment)
                    .addToBackStack(null)
                    .commit();
            });
        }

        @Override
        public int getItemCount() {
            return venues.size();
        }

        class VenueViewHolder extends RecyclerView.ViewHolder {
            ImageView venueImage;
            TextView venueNameText;
            TextView venueAddressText;
            TextView venuePriceText;
            TextView venueDistanceText;

            VenueViewHolder(@NonNull View itemView) {
                super(itemView);
                venueImage = itemView.findViewById(R.id.venueImage);
                venueNameText = itemView.findViewById(R.id.venueNameText);
                venueAddressText = itemView.findViewById(R.id.venueAddressText);
                venuePriceText = itemView.findViewById(R.id.venuePriceText);
                venueDistanceText = itemView.findViewById(R.id.venueDistanceText);
            }
        }
    }
} 