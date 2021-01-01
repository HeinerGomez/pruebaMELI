package com.avility.pruebameli.api;

import com.avility.pruebameli.BuildConfig;

public class Constants {

    // Configuracion del endpoint base de la api
    public static final String API_URL = BuildConfig.API_URL;
    public static final String API_URL_MELI_COLOMBIA = API_URL + "sites/MCO/";

    // endpoints a usar
    public static final String ENDPOINT_SEARCH = API_URL_MELI_COLOMBIA + "search";

}
