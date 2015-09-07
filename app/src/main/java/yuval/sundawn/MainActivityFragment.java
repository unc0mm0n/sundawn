package yuval.sundawn;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import yuval.sundawn.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    protected ForecastAdapter mForecastAdapter;

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
        Log.v(LOG_TAG,"start");
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> temp_data = new ArrayList<>();
        for (int i=0; i < 7; i++) {
            temp_data.add(String.valueOf(Math.random() * 50 - 20));
        }

        String locationSetting = Utility.getPreferredLocation(getActivity());

        //Sort order: date, ascending
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Cursor cur = getActivity().getContentResolver()
                .query(weatherForLocationUri, null, null, null, sortOrder);

        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);

        ListView listview = (ListView)rootView.findViewById(R.id.forecast_list_view);

        listview.setAdapter(mForecastAdapter);

        return rootView;
    }

    public void updateWeather() {

        String location = Utility.getPreferredLocation(getActivity());
        FetchWeatherTask fetcher = new FetchWeatherTask(getActivity());
        fetcher.execute(location);
    }
}
