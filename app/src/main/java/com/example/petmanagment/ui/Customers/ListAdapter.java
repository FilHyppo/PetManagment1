package com.example.petmanagment.ui.Customers;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmanagment.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    //creo un adapter per la recycle view
    FirebaseUser user;
    FirebaseFirestore db;
    ArrayList<String> list;
    Customer customer;
    StorageReference reference;
    FirebaseStorage storage;


    public ListAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvNameSurname.setText(list.get(position));
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        customer = new Customer();
        db.collection(user.getEmail().toString()).document(list.get(position)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                customer = documentSnapshot.toObject(Customer.class);
                holder.tvPhone.setText(customer.getPhone());
                String path = String.format("%s/customer_icon", customer.getName() + customer.getLastName());
                System.out.println(path);
                reference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        holder.imcustomer_icon.setImageURI(uri);
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameSurname;
        TextView tvPhone;
        ImageView imcustomer_icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSurname = itemView.findViewById(R.id.tvnamesurname);
            tvPhone = itemView.findViewById(R.id.tvphone);
            imcustomer_icon = itemView.findViewById(R.id.imageView2);
        }
    }
}
