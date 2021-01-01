package com.avility.pruebameli.activity.productsList.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avility.pruebameli.R;
import com.avility.pruebameli.activity.productsList.adapters.ProductsListRecyclerViewAdapter;
import com.avility.pruebameli.activity.productsList.viewModel.ProductsListViewModel;
import com.avility.pruebameli.helpers.Utils;

import io.reactivex.disposables.Disposable;

public class ManagerPagingRecyclerView {

    private static ManagerPagingRecyclerView mInstance;

    private Application application;
    RecyclerView recyclerView;
    private ProductsListRecyclerViewAdapter productsListRecyclerViewAdapter;
    private ProductsListViewModel productsListViewModel;

    // nos ayudará a conocer la pagina actual
    private int currentOffset = 0;
    // constante que nos indicará de cuanto en cuanto debe incrementarse el offset
    private final int INCREMENT_OFFSET = 50;
    // nos ayudará para conocer la ultima busqueda
    private String lastSearchTerm = "";

    protected Disposable disposableMoreProducts;

    // para obtener una unica instancia
    public static ManagerPagingRecyclerView getInstance(@NonNull Application application,
                                     RecyclerView recyclerView,
                                     ProductsListRecyclerViewAdapter productsListRecyclerViewAdapter,
                                     ProductsListViewModel productsListViewModel) {
        if (mInstance == null) {
            synchronized (ManagerPagingRecyclerView.class) {
                if (mInstance == null) {
                    mInstance = new ManagerPagingRecyclerView(application, recyclerView,
                            productsListRecyclerViewAdapter, productsListViewModel);
                }
            }
        }
        return mInstance;
    }

    // constructor
    private ManagerPagingRecyclerView(Application application, RecyclerView recyclerView,
                                      ProductsListRecyclerViewAdapter productsListRecyclerViewAdapter,
                                      ProductsListViewModel productsListViewModel) {
        this.application = application;
        this.recyclerView = recyclerView;
        this.productsListRecyclerViewAdapter = productsListRecyclerViewAdapter;
        this.productsListViewModel = productsListViewModel;
    }

    /**
     * Configuramos los listener para el scroll del recyclerview
     */
    protected void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (productsListRecyclerViewAdapter != null && !productsListViewModel.loading.get()) {
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() == productsListRecyclerViewAdapter.getItemCount() - 1) {
                        // cargamos mas items
                        loadMoreProducts();
                    }
                }
            }
        });
    }

    /**
     * Nos ayudara con la logica para cargar mas registros
     */
    public void loadMoreProducts() {
        productsListViewModel.loadingMoreProducts.set(true);

        currentOffset += INCREMENT_OFFSET;

        disposableMoreProducts = productsListViewModel.searchProducts(lastSearchTerm, currentOffset)
                .subscribe(products -> {
                    if (products.size() > 0) {
                        productsListRecyclerViewAdapter.addMoreItems(products);
                    }

                    productsListViewModel.loadingMoreProducts.set(false);
                }, error -> {
                    Utils.showToast(application, application.getString(R.string.products_list_error_search_products));
                    productsListViewModel.loadingMoreProducts.set(false);
                });
    }

    protected void setLastSearchTerm(String lastSearchTerm) {
        this.lastSearchTerm = lastSearchTerm;
    }

    protected void setProductsListRecyclerViewAdapter(ProductsListRecyclerViewAdapter productsListRecyclerViewAdapter) {
        this.productsListRecyclerViewAdapter = productsListRecyclerViewAdapter;
    }

    protected void onDestroy() {
        if (disposableMoreProducts != null) {
            disposableMoreProducts.dispose();
            disposableMoreProducts = null;
        }
    }
}
