package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fiuba.gaff.comohoy.comparators.CommerceLocationComparator;
import com.fiuba.gaff.comohoy.comparators.CommerceRatingComparator;
import com.fiuba.gaff.comohoy.filters.Filter;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.CategoryUsageData;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Day;
import com.fiuba.gaff.comohoy.model.Extra;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.model.OpeningTime;
import com.fiuba.gaff.comohoy.model.Opinion;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.model.TimeInterval;
import com.fiuba.gaff.comohoy.networking.DownloadCallback;
import com.fiuba.gaff.comohoy.networking.HttpMethodType;
import com.fiuba.gaff.comohoy.networking.NetworkFragment;
import com.fiuba.gaff.comohoy.networking.NetworkObject;
import com.fiuba.gaff.comohoy.networking.NetworkResult;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.facebook.FacebookService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCommercesService implements CommercesService {

    private static final String REQUEST_COMMERCES_URL = "http://34.237.197.99:9000/api/v1/commerces";
    private static final String REQUEST_COMMERCES_WITH_LOC_FORMAT = "http://34.237.197.99:9000/api/v1/commerces?lat=%f&lng=%f";
    private Context mContext;

    private Map<Integer, Commerce> mCommerces;
    private List<Filter> mFilters;
    private boolean mDownloading;

    public BaseCommercesService(Context context){
        mDownloading = false;
        mContext = context;
        mFilters = new ArrayList<>();
    }

    @Override
    public void updateCommercesData(Activity activity, final UpdateCommercesCallback callback) {
        NetworkObject updateCommercesNetworkObject = createUpdateCommercesNetworkObject(REQUEST_COMMERCES_URL);
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), updateCommercesNetworkObject);
        downloadCommerces(networkFragment, callback);
    }

    @Override
    public void updateCommercesWithLocation(Activity activity, final UpdateCommercesCallback callback, Location location) {
        final String uri = String.format(REQUEST_COMMERCES_WITH_LOC_FORMAT, location.getLatitud(), location.getLongitud());
        NetworkObject updateCommercesNetworkObject = createUpdateCommercesNetworkObject(uri);
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), updateCommercesNetworkObject);
        downloadCommerces(networkFragment, callback);
    }

    @Override
    public boolean isDownloading() {
        return mDownloading;
    }

    @Override
    public List<Commerce> getCommerces() {
        List<Commerce> commerces = new ArrayList<>(mCommerces.values());
        return filterCommerces(commerces);
    }

    @Override
    public List<Commerce> getFavouritesCommerces() {
        return new ArrayList<>(mCommerces.values());
    }

    @Override
    public List<Commerce> getCommercesSortedBy(Context context, SortCriteria sortCriteria) {
        List<Commerce> commerces = new ArrayList<>(mCommerces.values());
        switch (sortCriteria) {
            case Closeness:
                Collections.sort(commerces, new CommerceLocationComparator(context));
                break;
            case Rating:
                Collections.sort(commerces, new CommerceRatingComparator());
            default:
                Collections.sort(commerces, new CommerceLocationComparator(context));
        }
        return filterCommerces(commerces);
    }

    @Override
    public List<CategoryUsageData> getUsedCategoriesUsageData() {
        HashMap<String, CategoryUsageData> categoriesUsageMap = new HashMap<>();
        List<Commerce> commerces = getCommerces();
        for (Commerce commerce : commerces) {
            List<Category> categories = commerce.getCategories();

            for (Category category : categories) {
                String categoryName = category.getName();
                // Si existe suma una a la cantidad de uso, si no lo crea e inicializa en 1
                if (categoriesUsageMap.containsKey(categoryName)) {
                    CategoryUsageData categoryData = categoriesUsageMap.get(categoryName);
                    int uses = categoryData.getUsesAmount() + 1;
                    categoryData.setUsesAmount(uses);
                } else {
                    CategoryUsageData categoryData = new CategoryUsageData(category, 1);
                    categoriesUsageMap.put(category.getName(), categoryData);
                }
            }
        }
        return new ArrayList<>(categoriesUsageMap.values());
    }

    @Override
    public Commerce getCommerce(int id) {
        if (mCommerces.containsKey(id)) {
            return mCommerces.get(id);
        }
        Log.w("CommercesService", "There is no commerce with id " + id);
        return null;
    }

    @Override
    public void addFilter(Filter filter) {
        mFilters.add(filter);
    }

    @Override
    public void clearFilters() {
        mFilters.clear();
    }

    public List<Opinion> getOpiniones(JSONObject commerceJson) {
        List<Opinion> opiniones = new ArrayList<Opinion>();

        Opinion opinion1 = new Opinion((long)1);
        opinion1.setNameOpinion("Lionel Messi");
        opinion1.setDescription("Esta es una descripcion corta.");
        opinion1.setPuntuation(3);
        opinion1.setReplica("OK.");

        Opinion opinion2 = new Opinion((long)2);
        opinion2.setNameOpinion("Juan Perez");
        opinion2.setDescription("Esta es una descripcion un poco mas larga que cuenta como le fue en la comida y si disfruto la estadia.");
        opinion2.setPuntuation(0);
        opinion2.setReplica("Lo sentimos mucho, pero no pudimos hacer nada mas al respecto.");

        Opinion opinion3 = new Opinion((long)3);
        opinion3.setNameOpinion("Jorge Rodriguez");
        opinion3.setDescription("Esta es una descripcion super larga de como le fue, ademas el tipo colgo y conto la descripcion de siga la vaca. Exelentes platos de parrilla, elaborados en base a la major materia prima que tiene para ofrecer este país.Con una sala amplia que puede albergar una gran cantidad de comensales, el restaurante Siga La Vaca cuenta con una gran variedad de carnes a la parrilla. Su propuesta de tenedor libre, todo includo hace que este sea uno de los lugares privilegiados a la hora de elegir un lugar a donde ir a disfrutar de un buen asado.");
        opinion3.setPuntuation(4);
        opinion3.setReplica("Gracias, vuelva pronto.");

        opiniones.add(opinion1);
        opiniones.add(opinion2);
        opiniones.add(opinion3);
        /*try {
            JSONArray opinionesArray = commerceJson.getJSONArray("opiniones");
            for (int i = 0; i < opinionesArray.length(); ++i) {
                JSONObject opinionJson = opinionesArray.getJSONObject(i);
                Opinion opinion = new Opinion(opinionJson.getLong("id"));
                opinion.setNameOpinion(opinionJson.getString("name"));
                opinion.setDescription(opinionJson.getString("description"));
                opinion.setPuntuation(opinionJson.getInt("puntuation"));
                opinion.setReplica(opinionJson.getString("replica"));
                opiniones.add(opinion);
            }
        } catch (JSONException e) {
            Log.e("CommerceService", "Error parsing opinions: " + e.getMessage());
            e.printStackTrace();
        }*/
        return opiniones;
    }

    private List<Commerce> filterCommerces(List<Commerce> commerces) {
        List<Commerce> filteredCommerces = commerces;
        for (Filter filer : mFilters) {
            filteredCommerces = filer.apply(filteredCommerces);
        }
        return filteredCommerces;
    }

    private NetworkObject createUpdateCommercesNetworkObject(String url) {
        NetworkObject updateCommercesNetworkObject = new NetworkObject(url, HttpMethodType.GET);
        String authToken = ServiceLocator.get(FacebookService.class).getAuthToken();
        updateCommercesNetworkObject.setAuthToken(authToken);
        return updateCommercesNetworkObject;
    }

    private void getCommercesFromResponse(String response) throws JSONException {
        JSONArray commercesJson = new JSONArray(response);
        for(int i = 0; i < commercesJson.length(); i++) {
            JSONObject commerceJson = commercesJson.getJSONObject(i);

            int commerceId = commerceJson.getInt("id");
            Commerce commerce = new Commerce(commerceId);
            commerce.setName(commerceJson.getString("name"));
            commerce.setBusinessName(commerceJson.getString("businessName"));
            commerce.setCategories(getCommerceCategories(commerceJson));
            commerce.setPlates(getCommercePlates(commerceJson));
            commerce.setLocation(getCommerceLocation(commerceJson));
            commerce.setOpeningTimes(getOpeningTimes(commerceJson));
            commerce.setOpiniones(getOpiniones(commerceJson));

            mCommerces.put(commerceId, commerce);
        }
    }

    private Location getCommerceLocation(JSONObject commerceJson) throws  JSONException {
        JSONObject locationObject = commerceJson.getJSONObject("location");
        double lat = locationObject.getDouble("lat");
        double lon = locationObject.getDouble("lng");
        return new Location(lat, lon);
    }

    private List<Category> getCommerceCategories(JSONObject commerceJson) throws  JSONException {
        List<Category> categories = new ArrayList<>();
        JSONArray categoriesArray = commerceJson.getJSONArray("categories");
        for(int i = 0; i < categoriesArray.length(); i++) {
            JSONObject categoryObject = categoriesArray.getJSONObject(i);
            Long id = categoryObject.getLong("id");
            String name = categoryObject.getString("name");
            Category category = new Category(id, name);
            categories.add(category);
        }
        return categories;
    }

    private HashMap<Long, Plate> getCommercePlates(JSONObject commerceJson) {
        HashMap<Long, Plate> plates = new HashMap<>();
        try {
            JSONArray platesArray = commerceJson.getJSONArray("plates");
            int a = platesArray.length();
            for (int i = 0; i < platesArray.length(); ++i) {
                JSONObject plateJson = platesArray.getJSONObject(i);
                Plate plate = new Plate(plateJson.getLong("id"));
                plate.setName(plateJson.getString("name"));
                plate.setPrice(plateJson.getDouble("price"));
                plate.setSuitableForCeliac(plateJson.getBoolean("glutenFree"));
                plate.setDescription(plateJson.getString("description"));
                plate.setCategories(getPlateCategories(plateJson));
                plate.setExtras(getPlateExtras(plateJson));

                plates.put(plate.getId(), plate);
            }
        } catch (JSONException e) {
            Log.e("CommerceService", "Error parsing plates: " + e.getMessage());
            e.printStackTrace();
        }
        return plates;
    }

    private List<Category> getPlateCategories(JSONObject plateJson) throws JSONException {
        List<Category> categories = new ArrayList<>();
        JSONObject categotyObject = plateJson.getJSONObject("category");
        Long id = categotyObject.getLong("id");
        String name = categotyObject.getString("name");
        Category category = new Category(id, name);
        categories.add(category);
        return categories;
    }

    private List<Extra> getPlateExtras(JSONObject plateJson) {
        List<Extra> extras = new ArrayList<>();
        try {
            JSONArray extrasArray = plateJson.getJSONArray("optionals");
            for(int i = 0; i < extrasArray.length(); i++) {
                JSONObject extraObject = extrasArray.getJSONObject(i);
                Long id = extraObject.getLong("id");
                Extra extra = new Extra(id);
                extra.setName(extraObject.getString("name"));
                extra.setPrice(extraObject.getDouble("price"));
                extras.add(extra);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return extras;
    }

    private List<OpeningTime> getOpeningTimes(JSONObject commerceJson) throws JSONException {
        HashMap<String, OpeningTime> openingTimesMap = new HashMap<>();
        JSONArray timesArray = commerceJson.getJSONArray("times");
        for(int i = 0; i < timesArray.length(); i++) {
            JSONObject openingTimeObject = timesArray.getJSONObject(i);
            addOpeningTimeToMap(openingTimeObject, openingTimesMap);
        }
        return new ArrayList<>(openingTimesMap.values());
    }

    private OpeningTime addOpeningTimeToMap(JSONObject openingTimeJson, HashMap<String, OpeningTime> openingTimesMap) {
        OpeningTime openingTime = new OpeningTime();
        try {
            Day day = Day.fromString(openingTimeJson.getString("day"));
            if (openingTimesMap.containsKey(day.toString())) {
                openingTime = openingTimesMap.get(day.toString());
            } else {
                openingTime.setDay(day);
            }
            Long fromHour = openingTimeJson.getLong("fromHour");
            Long toHour = openingTimeJson.getLong("toHour");
            TimeInterval timeInterval = new TimeInterval(new Timestamp(fromHour), new Timestamp(toHour));
            openingTime.addTimeInterval(timeInterval);
            openingTimesMap.put(openingTime.getDay().toString(), openingTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return openingTime;
    }

    private void downloadCommerces(final NetworkFragment networkFragment, final UpdateCommercesCallback callback) {
        mCommerces = new HashMap<>();
        mDownloading = true;
        networkFragment.startDownload(new DownloadCallback<NetworkResult>() {

            @Override
            public void onResponseReceived(NetworkResult result) {
                if (result.mException == null) {
                    try {
                        getCommercesFromResponse(result.mResultValue);
                        callback.onCommercesUpdated();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.e("CommercesService", result.mException.getMessage());
                    callback.onError("No se pudieron cargar comercios. Verifique su conección de internet");
                }
            }

            @Override
            public NetworkInfo getActiveNetworkInfo(Context context) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo;
            }

            @Override
            public void onProgressUpdate(int progressCode, int percentComplete) {

            }

            @Override
            public void onFinishDownloading() {
                mDownloading = false;
            }
        });
    }
}
