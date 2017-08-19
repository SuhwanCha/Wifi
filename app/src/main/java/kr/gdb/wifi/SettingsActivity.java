package kr.gdb.wifi;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

    public SettingsActivity(){

    }
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            getPreferenceScreen().setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (preference.getKey() == "email") sendMail();
                    return false;
                }
            });

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == android.R.id.home) {
                getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }



        public void sendMail(){
            Intent it = new Intent(Intent.ACTION_SEND);
            String[] mailaddr = {"suhwan@gdb.kr"};

            it.setType("plaine/text");
            it.putExtra(Intent.EXTRA_EMAIL, mailaddr);
            startActivity(it);

        }


    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)

    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        String 강원도 = "강릉시,고성군,동해시,삼척시,속초시,양구군,양양군,영월군,원주시,인제시,정선군,철원군,춘천시,태백시,평창군,홍천군,화천군,횡성군";
        String 경기도 = "가평군,고양시 덕양구,고양시 일산동구,고양시 일산서구,과천시,광명시,광주시,구리시,군포시,김포시,남양주시,동두천시,부천시 오정구,성남시 분당구,성남시 수정구,성남시 중원구,수원시 권선구,수원시 영통구,수원시 장안구,수원시 팔달구,시흥시,안산시 단원구,안산시 상록구,안성시,양평군,연천군,용인시 수지구,의왕시,평택시,포천시,하남시,부천시 소사구,부천시 원미구,안양시 동안구,안양시 만안구,양주시,여주시,오산시,용인시 기흥구,용인시 처인구,의정부시,이천시,파주시,화성시";
        String 경상남도 = "거제시,거창군,고성군,김해시,남해군,밀양시,사천시,산청군,양산시,의령군,진주시,창녕군,창원시 마산합포구,창원시 마산회원구,창원시 성산구,창원시 의창구,창원시 진해구,통영시,하동군,함안군,함양군,합천군";
        String 경상북도 = "경산시,경주시,고령군,구미시,군위군,김천시,문경시,봉화군,상주시,성주군,안동시,영덕군,영양군,영주시,영천시,예천군,울릉군,울진군,의성군,청도군,청송군,칠곡군,포항시 남구,포항시 북구";
        String 광주광역시 = "광산구,남구,동구,북구,서구";
        String 대구광역시 = "남구,달서구,달성군,동구,북구,서구,수성구,중구";
        String 대전광역시 = "대덕구,동구,서구,유성구,중구";
        String 부산광역시 = "강서구,금정구,기장군,남구,동구,동래구,부산진구,북구,사상구,사하구,서구,수영구,연제구,영도구,중구,해운대구";
        String 서울특별시 = "강남구,강동구,강북구,강서구,관악구,광진구,구로구,금천구,노원구,도봉구,동대문구,동작구,마포구,서대문구,서초구,성동구,성북구,송파구,양천구,영등포구,용산구,은평구,종로구,중구,중랑구";
        String 세종특별자치시 = "";
        String 울산광역시 = "남구,동구,북구,울주군,중구";
        String 인천광역시 = "강화군,계양구,남구,남동구,동구,부평구,서구,연수구,옹진군,중구";
        String 전라남도 = "강진군,고흥군,곡성군,광양시,구례군,나주시,담양군,목포시,무안군,보성군,순천시,신안군,여수시,영광군,영암군,완도군,장성군,장흥군,진도군,함평군,해남군,화순군";
        String 전라북도 = "고창군,군산시,김제시,남원시,무주군,부안군,순창군,완주군,익산시,임실군,장수군,전주시 덕진구,전주시 완산구,정읍시,진안군";
        String 제주특별자치도 = "서귀포시,제주시";
        String 충청북도 = "계룡시,공주시,금산군,논산시,당진시,보령시,부여군,서산시,서천군,아산시,예산군,천안시 동남구,천안시 서북구,청양군,태안군,홍성군";
        String 충청남도 = "괴산군,단양군,보은군,영동군,옥천군,음성군,제천시,증평군,진천군,청주시 상당구,청주시 서원구,청주시 청원구,청주시 흥덕구,충주시";
        HashMap<String,String> locate = new HashMap<>();

        public void putmap(){
            locate.put("강원도",강원도);
            locate.put("경기도",경기도);
            locate.put("경상남도",경상남도);
            locate.put("경상북도",경상북도);
            locate.put("광구광역시",광주광역시);
            locate.put("대구광역시",대구광역시);
            locate.put("대전광역시",대전광역시);
            locate.put("부산광역시",부산광역시);
            locate.put("서울특별시",서울특별시);
            locate.put("세종특별자치시",세종특별자치시);
            locate.put("울관광역시",울산광역시);
            locate.put("인천광역시",인천광역시);
            locate.put("전라남도",전라남도);
            locate.put("전라북도",전라북도);
            locate.put("제주특별자치도",제주특별자치도);
            locate.put("충청북도",충청북도);
            locate.put("충청남도",충청남도);
        }
        String addr1="", addr2="";



        @Override
        public void onCreate(Bundle savedInstanceState) {
            putmap();
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            addPreferencesFromResource(R.xml.user_settings);

            ListPreference selected_theme = (ListPreference) getPreferenceManager().findPreference("sync_frequency");
            ListPreference selected_theme2 = (ListPreference) getPreferenceManager().findPreference("sync_frequency2");
            try {
                selected_theme2.setEntries(locate.get(selected_theme.getValue()).split(","));
                selected_theme2.setEntryValues(locate.get(selected_theme.getValue()).split(","));
            }catch (Exception e){

            }
            selected_theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ListPreference selected_theme = (ListPreference) getPreferenceManager().findPreference("sync_frequency2");
                    selected_theme.setEntries(locate.get(newValue).split(","));
                    selected_theme.setEntryValues(locate.get(newValue).split(","));
                    addr1 = (String) newValue;
//                    bindPreferenceSummaryToValue(findPreference("sync_frequency"));
//                    bindPreferenceSummaryToValue(findPreference("sync_frequency2"));
                    return true;
                }
            });
            selected_theme2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    addr2 = (String)newValue;
//                    bindPreferenceSummaryToValue(findPreference("sync_frequency"));
//                    bindPreferenceSummaryToValue(findPreference("sync_frequency2"));
                    return true;
                }
            });


        }




        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
