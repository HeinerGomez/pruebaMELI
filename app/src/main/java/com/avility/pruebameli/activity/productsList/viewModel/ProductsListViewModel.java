package com.avility.pruebameli.activity.productsList.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import com.avility.pruebameli.entities.Product;
import com.avility.pruebameli.repository.products.ProductsRepository;

import java.util.List;

import io.reactivex.Observable;

public class ProductsListViewModel extends AndroidViewModel {

    private ProductsRepository productsRepository;

    public ObservableBoolean withoutSearch;
    public ObservableBoolean loading;
    public ObservableBoolean loadingMoreProducts;

    public ProductsListViewModel(@NonNull Application application) {
        super(application);
        this.productsRepository = new ProductsRepository();

        this.withoutSearch = new ObservableBoolean(true);
        this.loading = new ObservableBoolean(false);
        this.loadingMoreProducts = new ObservableBoolean(false);
    }

    public Observable<List<Product>> searchProducts(String termSearch, int offset) {
        return this.productsRepository.searchProducts(termSearch, offset);
    }

}
