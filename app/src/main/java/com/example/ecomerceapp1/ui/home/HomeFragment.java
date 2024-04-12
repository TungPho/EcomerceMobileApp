package com.example.ecomerceapp1.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.adapters.CategoryAdapter;
import com.example.ecomerceapp1.adapters.PopularAdapters;
import com.example.ecomerceapp1.adapters.RecommendedAdapters;
import com.example.ecomerceapp1.adapters.ViewAllAdapter;
import com.example.ecomerceapp1.models.HomeCategory;
import com.example.ecomerceapp1.models.PopularModel;
import com.example.ecomerceapp1.models.RecommendProducts;
import com.example.ecomerceapp1.models.ViewAllProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    //use the recycler view to render the popular products
    RecyclerView popularRec;

    RecyclerView categoryRec;

    RecyclerView recommendedRec;

    FirebaseFirestore db;

    //popular items
    PopularAdapters popularAdapters;
    List<PopularModel> popularModelList;

    //category items
    CategoryAdapter categoryAdapter;
    List<HomeCategory> homeCategories;

    //recommended prods
    RecommendedAdapters recommendedAdapters;
    List<RecommendProducts> recommendProducts;


    //Progress bar and scroll view
    ScrollView scrollView;
    ProgressBar progressBar;

    //Search box
    EditText searchbox;
    private RecyclerView searchResultRec;
    private List<ViewAllProductsModel> listSearchItems;
    private ViewAllAdapter viewAllAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //the firestore db

        db = FirebaseFirestore.getInstance();
        //specify the Recycler view you want to render data
        popularRec = root.findViewById(R.id.popular_products_rec);
        popularRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularModelList = new ArrayList<>();
        popularAdapters = new PopularAdapters(getActivity(), popularModelList);
        popularRec.setAdapter(popularAdapters);

        //Category Read from Firestore DB
        homeCategories = new ArrayList<HomeCategory>();
        categoryAdapter = new CategoryAdapter(getContext(), homeCategories);
        categoryRec = root.findViewById(R.id.explore_products_rec);
        categoryRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryRec.setAdapter(categoryAdapter);


        //Recommendations Products
        recommendedRec = root.findViewById(R.id.recommended_rec);
        recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recommendProducts = new ArrayList<RecommendProducts>();
        recommendedAdapters = new RecommendedAdapters(getContext(), recommendProducts);
        recommendedRec.setAdapter(recommendedAdapters);

        progressBar = root.findViewById(R.id.progress_bar);
        scrollView = root.findViewById(R.id.scroll_view);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        //Read the popular Products
        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //if loaded data
                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                                PopularModel popularModel = document.toObject(PopularModel.class);
                                popularModelList.add(popularModel);
                                popularAdapters.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getContext(), "Error Reading Data" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Read data from HomeCategory
        db.collection("Home Collection")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeCategory homeCategory = document.toObject(HomeCategory.class);
                                homeCategories.add(homeCategory);
                                categoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error Reading Data" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //Read the Recommended Products
        //
        db.collection("Recommended")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecommendProducts recommendProduct = document.toObject(RecommendProducts.class);
                                recommendProducts.add(recommendProduct);
                                recommendedAdapters.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error Reading Data" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        ////////////Search View
        //Search box
        searchResultRec = root.findViewById(R.id.search_result_rec);
        searchbox = root.findViewById(R.id.search_box);
        listSearchItems = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(getContext(), listSearchItems);
        searchResultRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        searchResultRec.setHasFixedSize(true);
        searchResultRec.setAdapter(viewAllAdapter);

        /////////observe the text change
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    listSearchItems.clear();
                    viewAllAdapter.notifyDataSetChanged();
                } else {
                    searchProduct(s.toString());
                }
            }
        });


        return root;
    }

    public void searchProduct(String type) {
        if (!type.isEmpty()) {
            db.collection("All Products").whereEqualTo("type", type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                //reset the list
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                        listSearchItems.clear();
                        viewAllAdapter.notifyDataSetChanged();
                        for(DocumentSnapshot doc : task.getResult().getDocuments()){
                            ViewAllProductsModel viewAllProductsModel = doc.toObject(ViewAllProductsModel.class);
                            Toast.makeText(getContext(), viewAllProductsModel.toString(), Toast.LENGTH_SHORT).show();
                            listSearchItems.add(viewAllProductsModel);
                            viewAllAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }


}