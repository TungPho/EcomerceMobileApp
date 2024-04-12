package com.example.ecomerceapp1.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.adapters.NavCategoryAdapter;
import com.example.ecomerceapp1.models.NavCategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {



    RecyclerView recyclerView;
    List<NavCategoryModel> navCategoryModelList;
    NavCategoryAdapter categoryAdapter;

    FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        db = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.cat_rec);

        navCategoryModelList = new ArrayList<NavCategoryModel>();
        categoryAdapter = new NavCategoryAdapter(getActivity(), navCategoryModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(categoryAdapter);

        //Read data from HomeCategory
        db.collection("NavCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
                                navCategoryModelList.add(navCategoryModel);
                                categoryAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getContext(), "Error Reading Data" + task.getException() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        return root;
    }


}