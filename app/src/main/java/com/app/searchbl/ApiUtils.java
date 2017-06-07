package com.app.searchbl;

/**
 * Created by Genflix on 5/31/17.
 */

public class ApiUtils {

    public static final String BASE_URL = "https://api.bukalapak.com/v2/";

    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}