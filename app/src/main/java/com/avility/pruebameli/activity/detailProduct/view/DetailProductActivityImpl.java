package com.avility.pruebameli.activity.detailProduct.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.avility.pruebameli.R;
import com.avility.pruebameli.activity.detailProduct.viewModel.DetailProductViewModel;
import com.avility.pruebameli.activity.productsList.viewModel.ProductsListViewModel;
import com.avility.pruebameli.databinding.ActivityDetailProductBinding;
import com.avility.pruebameli.databinding.ActivityProductsListBinding;
import com.avility.pruebameli.entities.Product;
import com.avility.pruebameli.helpers.Utils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailProductActivityImpl extends AppCompatActivity implements DetailProductActivity {

    @BindView(R.id.detail_product_tv_condition_and_sold_quantity)
    TextView conditionAndSoldQuantity;

    @BindView(R.id.detail_product_tv_title)
    TextView title;

    @BindView(R.id.detail_product_image_view)
    ImageView productImageView;

    @BindView(R.id.detail_product_tv_price)
    TextView price;

    @BindView(R.id.detail_product_available_quantity)
    TextView availableQuantity;

    private DetailProductViewModel detailProductViewModel;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // conectamos nuestro activity con nuestro view model
        detailProductViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(DetailProductViewModel.class);


        /*
         * hacemos el uso del binding para conectar nuestro view model con la vista xml, para esto
         * es importante configurar el xml para que reciba el view model y configurar nuestro
         * build.gradle para habilitar el binding en nuestra aplicación.
         */
        ActivityDetailProductBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_product);
        binding.setViewmodel(detailProductViewModel);

        /*
         * habilitamos el butterKnife para esta vista, esto nos ayudará a hacer referencia a los
         * elementos de la vista de una manera más facíl por medio de anotaciones.
         */
        ButterKnife.bind(this);

        // obtenemos el producto seleccionado
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");

        configView();
    }

    @Override
    public void configView() {

        // Obtenemos el toolbar y lo configuramos ( agregando el titulo y si es que necesita un back button )
        Toolbar toolbar = findViewById(R.id.detail_product_toolbar);
        Utils.showToolbar(this, toolbar, getString(R.string.detail_product_title_toolbar), false);

        // como la condicion llega en ingles hay que hacer la respectiva traducccion al español
        String translateCondition = Product.CONDITION_KEY_WORD.equals(product.getCondition()) ?
                getString(R.string.product_item_condition_text_new) :
                getString(R.string.product_item_condition_text_used);

        String textConditionAndSoldQuantity = translateCondition + " | " + getString(R.string.detail_product_sold_quantity, product.getSoldQuantity());

        conditionAndSoldQuantity.setText(textConditionAndSoldQuantity);

        title.setText(product.getTitle());

        // cargamos la imagen
        Glide.with(getApplication())
                .load(product.getThumbnail())
                .into(productImageView);

        price.setText(Utils.transformToCurrency(product.getPrice()));

        availableQuantity.setText(getString(R.string.product_item_available_quantity_text, product.getAvailableQuantity()));

    }
}