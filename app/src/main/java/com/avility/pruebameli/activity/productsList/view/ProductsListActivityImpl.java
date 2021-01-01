package com.avility.pruebameli.activity.productsList.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.avility.pruebameli.R;
import com.avility.pruebameli.activity.productsList.adapters.ProductsListRecyclerViewAdapter;
import com.avility.pruebameli.activity.productsList.viewModel.ProductsListViewModel;
import com.avility.pruebameli.databinding.ActivityProductsListBinding;
import com.avility.pruebameli.helpers.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class ProductsListActivityImpl extends AppCompatActivity implements ProductsListActivity,
        SearchView.OnQueryTextListener {

    @BindView(R.id.products_list_search_view)
    SearchView searchView;

    @BindView(R.id.products_list_recycler_view)
    RecyclerView recyclerView;

    Disposable disposableSearchProducts;

    // propiedad que nos dará una referencia hacia el view model correspondiente a esta vista
    private ProductsListViewModel productsListViewModel;
    // referencia hacia nuestro adapter recycler view
    private ProductsListRecyclerViewAdapter productsListRecyclerViewAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // conectamos nuestro activity con nuestro view model
        productsListViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(ProductsListViewModel.class);

        /*
         * hacemos el uso del binding para conectar nuestro view model con la vista xml, para esto
         * es importante configurar el xml para que reciba el view model y configurar nuestro
         * build.gradle para habilitar el binding en nuestra aplicación.
         */
        ActivityProductsListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_products_list);
        binding.setViewmodel(productsListViewModel);

        /*
         * habilitamos el butterKnife para esta vista, esto nos ayudará a hacer referencia a los
         * elementos de la vista de una manera más facíl por medio de anotaciones.
         */
        ButterKnife.bind(this);

        // llamo a la funcion encargada de configurar la vista
        configView();

        // configuramos la clase que nos ayudará a manejar la paginación del recycler view
        ManagerPagingRecyclerView.getInstance(getApplication(),
                recyclerView, productsListRecyclerViewAdapter, productsListViewModel)
                .initScrollListener();
    }

    /**
     * Se encargará de configurar los componentes necesarios para que la vista funcione
     */
    @Override
    public void configView() {
        // Obtenemos el toolbar y lo configuramos ( agregando el titulo y si es que necesita un back button )
        Toolbar toolbar = findViewById(R.id.products_list_toolbar);
        Utils.showToolbar(this, toolbar, getString(R.string.products_list_title_toolbar), false);

        // configuramos el search view para recibir los terminos de busqueda que ejecuta el usuario
        searchView.setOnQueryTextListener(this);

        // configuracion inicial del recycler view
        LinearLayoutManager productsListLinearLayout = new LinearLayoutManager(this);
        productsListLinearLayout.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(productsListLinearLayout);
    }

    /**
     * Nos ayudará a obtener los terminos de busqueda realizados por el usuario
     * @param s termino de busqueda
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String s) {
        ManagerPagingRecyclerView managerPagingRecyclerView = ManagerPagingRecyclerView.getInstance(getApplication(),
                recyclerView, productsListRecyclerViewAdapter, productsListViewModel);

        managerPagingRecyclerView.setLastSearchTerm(s);

        productsListViewModel.loading.set(true); // para mostrar el loading
        productsListViewModel.withoutSearch.set(false); // para ocultar la img placeholder de busqueda

        disposableSearchProducts = this.productsListViewModel.searchProducts(s, 0)
                .subscribe(products -> {
                    productsListRecyclerViewAdapter = new ProductsListRecyclerViewAdapter(getApplication(), R.layout.product_item, products);
                    recyclerView.setAdapter(productsListRecyclerViewAdapter);

                    managerPagingRecyclerView.setProductsListRecyclerViewAdapter(productsListRecyclerViewAdapter);
                    productsListViewModel.loading.set(false); // para ocultar el loading
                }, error -> {
                    Utils.showToast(getApplicationContext(), getString(R.string.products_list_error_search_products));
                    productsListViewModel.loading.set(false); // para ocultar el loading
                });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposableSearchProducts != null) {
            disposableSearchProducts.dispose();
            disposableSearchProducts = null;
        }

        ManagerPagingRecyclerView.getInstance(getApplication(),
                recyclerView, productsListRecyclerViewAdapter, productsListViewModel)
                .onDestroy();
    }
}