package com.example.aghiad_pc.get.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aghiad_pc.get.Model.Product;
import com.example.aghiad_pc.get.R;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {


    private List<Product> productList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, name, salary;
        ImageView image;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.product_title);
            name = view.findViewById(R.id.product_name);
            salary = view.findViewById(R.id.product_price);
            image= view.findViewById(R.id.product_image);
        }
    }

    public ProductsAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.title.setText(product.getTitle());
        holder.name.setText(product.getName());
        holder.salary.setText(product.getSalary());
        holder.image.setBackgroundResource(Integer.parseInt(product.getImage()));
    }

    @Override
    public int getItemCount() {
        return productList.size();

    }


}
