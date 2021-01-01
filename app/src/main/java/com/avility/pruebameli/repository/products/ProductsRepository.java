package com.avility.pruebameli.repository.products;

import com.avility.pruebameli.api.RestApiAdapter;
import com.avility.pruebameli.api.Service;
import com.avility.pruebameli.entities.Product;
import com.avility.pruebameli.helpers.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Repositorio para controlar las peticiones de los productos, en este punto si tuvieramos una bd interna,
 * lo que se deberia de hacer es separar los repositorios, para que solo manejen una unica
 * responsabilidad ejemplo: ProductsRemoteRepository y ProductsLocalRepository. Para este caso solo
 * será necesario el repositorio por lo cual no es necesario especificar a que fuente se refiere el
 * repositorio ( Local o Remota ), siguiendo este concepto el nombre de este repositorio será
 * ProductsRepository
 */
public class ProductsRepository {

    /**
     * Se encargará de buscar los productos remotamente en base a un termino de busqueda
     * @return Observable<Response<JsonObject>>
     */
    public Observable<List<Product>> searchProducts(String termSearch, int offset) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Service service = restApiAdapter.getPublicServiceAPI();

        return service.searchProductsAPI(termSearch, offset)
            .map(response -> {
                List<Product> products = new ArrayList<>();
                if (response.isSuccessful()) {
                    JsonObject body = response.body();

                    if (body != null && body.has("results")) {
                        JsonArray results = body.getAsJsonArray("results");

                        Type productsType = new TypeToken<List<Product>>() {
                        }.getType();

                        products = Utils.getGsonBuilder().create().fromJson(results, productsType);
                    }
                }
                return products;
            }).observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io());
    }

}
