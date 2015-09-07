package yuval.sundawn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    protected ArrayAdapter<String> mForecastAdapter;

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

        mForecastAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.forecast_text_view,
                (temp_data));

        ListView listview = (ListView)rootView.findViewById(R.id.forecast_list_view);

        listview.setAdapter(mForecastAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, parent.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateWeather() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        FetchWeatherTask fetcher = new FetchWeatherTask(getActivity(), mForecastAdapter);
        fetcher.execute(preferences.getString(
                getString(R.string.pref_location_key), getString(R.string.pref_location_default)) );
    }
}
