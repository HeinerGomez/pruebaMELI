package com.avility.pruebameli.activity.productsList.adapters;

import android.app.Application;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avility.pruebameli.R;
import com.avility.pruebameli.activity.detailProduct.view.DetailProductActivityImpl;
import com.avility.pruebameli.entities.Product;
import com.avility.pruebameli.helpers.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsListRecyclerViewAdapter extends RecyclerView.Adapter<ProductsListRecyclerViewAdapter.ProductHolder> {

    private Application application;
    private int resource;
    private List<Product> productsList;

    public ProductsListRecyclerViewAdapter(Application application, int resource, List<Product> productsList) {
        this.application = application;
        this.resource = resource;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = productsList.get(position);

        // este linear layout hace de contenedor, como si fuera un card view
        holder.linearLayout.setOnClickListener(l -> {
            Intent intent = new Intent(application, DetailProductActivityImpl.class);
            intent.putExtra("product", product);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            application.startActivity(intent);
        });

        // cargamos la imagen
        Glide.with(application)
                .load(product.getThumbnail())
                .into(holder.image);

        holder.title.setText(product.getTitle()); // establezco el titulo al item actual iterado del recycler view
        holder.price.setText(Utils.transformToCurrency(product.getPrice())); // establezco el precio al item actual iterado del recycler view
        holder.availableQuantity.setText(
                application.getString(R.string.product_item_available_quantity_text,
                        product.getAvailableQuantity())); // establezco la cantidad disponible al item actual iterado del recycler view

        // como la condicion llega en ingles hay que hacer la respectiva traducccion al español
        String translateCondition = Product.CONDITION_KEY_WORD.equals(product.getCondition()) ?
                application.getString(R.string.product_item_condition_text_new) :
                application.getString(R.string.product_item_condition_text_used);

        holder.condition.setText(translateCondition); // establezco la condicion al item actual iterado del recycler view
    }

    @Override
    public int getItemCount() {
        return productsList.size(); // cantidad de productos actuales
    }

    /**
     * Nos ayudará para cargar mas items al recycler view
     */
    public void addMoreItems(List<Product> products) {
        productsList.addAll(products);
        notifyDataSetChanged();
    }

    /**
     * Esta clase es para representar la vista de un item del recycler view
     */
    public class ProductHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_item_container_linear_layout)
        LinearLayout linearLayout;

        @BindView(R.id.product_item_image_view)
        ImageView image;

        @BindView(R.id.product_item_title)
        TextView title;

        @BindView(R.id.product_item_price)
        TextView price;

        @BindView(R.id.product_item_available_quantity)
        TextView availableQuantity;

        @BindView(R.id.product_item_condition)
        TextView condition;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
