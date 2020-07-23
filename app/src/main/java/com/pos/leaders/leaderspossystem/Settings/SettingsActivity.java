package com.pos.leaders.leaderspossystem.Settings;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.BackupActivity;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosSettingDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.LogInActivity;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.Clock;
import com.pos.leaders.leaderspossystem.Tools.OnClockTickListner;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.pos.leaders.leaderspossystem.SettingsTab.BOPOSVersionSettings.BO_SETTING;
import static com.pos.leaders.leaderspossystem.SettingsTab.PinpadTap.PINPAD_PREFERENCES;
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

    public final static String POS_Management = "POS_Management";
    public final static String POS_Company_status="Company_Status";
    private ActionBar actionBar;
    private static Clock clock=null;
    private static ImageView ivInternet;
    private static ImageView ivSync ;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupActionBar();

        //region titlebar

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.title_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // Set up your ActionBar
        actionBar = getSupportActionBar();
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBarLayout.setLayoutParams(params);
        actionBar.setCustomView(actionBarLayout);
        if (SESSION._EMPLOYEE == null) {
            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            SESSION._LogOut();
            this.startActivity(intent);
            //terminal stop
            //System.exit(0);
        }

        TypedValue tv = new TypedValue();
        if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }


        long date;
        final Calendar ca = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        // You customization
        final int actionBarColor = getResources().getColor(R.color.primaryColor);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));

        final TextView tvDate = (TextView) findViewById(R.id.titleBar_tvClock);
        tvDate.setText(format.format(ca.getTime()));

        if (clock == null) {
            clock = new Clock(this);
            clock.AddClockTickListner(new OnClockTickListner() {
                @Override
                public void OnSecondTick(Time currentTime) {

                }

                @Override
                public void OnMinuteTick(Time currentTime) {
                    tvDate.setText(format.format(currentTime.toMillis(true)).toString());
                }
            });
        }



        final TextView tvTerminalID = (TextView) findViewById(R.id.titleBar_tvTerminalID);
        tvTerminalID.setText("Terminal "+ SETTINGS.posID);



        final TextView tvUsername = (TextView) findViewById(R.id.titleBar_tvUsername);
        if (SESSION._EMPLOYEE == null){
            tvUsername.setText("");
        } else {
            tvUsername.setText(SESSION._EMPLOYEE.getFullName());
        }

        final TextView tvActivityTitle = (TextView) findViewById(R.id.titleBar_tvActivityLabel);
        PackageManager packageManager = getPackageManager();

        try {
            ActivityInfo info = packageManager.getActivityInfo(getComponentName(), 0);
            tvActivityTitle.setText(getString(info.labelRes));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {

        }


        ivInternet = (ImageView) findViewById(R.id.titleBar_ivInternetStatus);

        ivInternet.setVisibility(View.GONE);


        ivSync = (ImageView) findViewById(R.id.titleBar_ivSync);

        ivSync.setVisibility(View.GONE);

    }
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
       ActionBar actionBar = getSupportActionBar();
        Log.e("Actionbar", "Settings");
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);

            Log.e("Actionbar", "Settings");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
        setTheme(R.style.SettingsFragmentStyle);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || POSManagmentPreferenceFragment.class.getName().equals(fragmentName)
                || PinPadPreferenceFragment.class.getName().equals(fragmentName) || BackUpPreferenceFragment.class.getName().equals(fragmentName)
                || CompanyStatusPreferenceFragment.class.getName().equals(fragmentName);
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

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValue(findPreference("example_text"));
            //bindPreferenceSummaryToValue(findPreference("example_list"));

            findPreference("pref_title_about_company_name").setSummary(SETTINGS.companyName);
            findPreference("private_company").setSummary(SETTINGS.companyID);
            findPreference("tax").setSummary(Util.makePrice(SETTINGS.tax) + " %");
            findPreference("terminal_number").setSummary(getString(R.string.terminal_number) + " " + SETTINGS.posID);
            findPreference("invoice_note").setSummary(SETTINGS.returnNote);
            findPreference("currency_code").setSummary(SETTINGS.currencyCode);
            findPreference("currency_symbol").setSummary(SETTINGS.currencySymbol);
            findPreference("country").setSummary(SETTINGS.country);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class POSManagmentPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getPreferenceManager().setSharedPreferencesName(POS_Management);

            addPreferencesFromResource(R.xml.pref_pos_managment);
            setHasOptionsMenu(true);
            if(SETTINGS.enableAllBranch){
            SETTINGS.branchId=0;
            }else {
                SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(getContext());
                settingsDBAdapter.open();
                settingsDBAdapter.readSetting();

            }

          final   PosSettingDbAdapter posSettingDbAdapter=new PosSettingDbAdapter(getContext());
            posSettingDbAdapter.open();
            SwitchPreference enableDuplicateInvoice= (SwitchPreference) findPreference("LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_DUPLICATE_INVOICE");

            enableDuplicateInvoice.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    //   if (newValue.toString().equals("LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY")) {
                    boolean test = (Boolean) newValue;
                    if (test) {
                        long idPosSetting=posSettingDbAdapter.getPosSettingID().getPosSettingId();
                        SETTINGS.enableDuplicateInvoice=true;
                        posSettingDbAdapter.updateDuplicateInvoice(SETTINGS.enableDuplicateInvoice,idPosSetting);

                    } else {
                        long idPosSetting=posSettingDbAdapter.getPosSettingID().getPosSettingId();
                        SETTINGS.enableDuplicateInvoice=false;
                        posSettingDbAdapter.updateDuplicateInvoice(SETTINGS.enableDuplicateInvoice,idPosSetting);
                    }

                    // }
                    return true;
                }
            });

            SwitchPreference enableFoodStamp= (SwitchPreference) findPreference("LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FOOd_STAMP");

            enableFoodStamp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    //   if (newValue.toString().equals("LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY")) {
                    boolean test = (Boolean) newValue;
                    if (test) {
                        SETTINGS.enableFoodStamp=true;

                    } else {
                        SETTINGS.enableFoodStamp=false;
                    }




                    // }
                    return true;
                }
            });

            final MultiSelectListPreference multiSelectListPreference=(MultiSelectListPreference) findPreference("LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY_CODE_LIST");

            SwitchPreference enabledCurrency= (SwitchPreference) findPreference("LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY");
           final Set<Set<String>> entries=null;

           if (enabledCurrency.isChecked()){
               multiSelectListPreference.setEnabled(false);
           }
           else {
               multiSelectListPreference.setEnabled(false);
           }


            enabledCurrency.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                 //   if (newValue.toString().equals("LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY")) {
                        boolean test = (Boolean) newValue;
                        if (test) {
                            multiSelectListPreference.setEnabled(true);

                        } else {
                            multiSelectListPreference.setEnabled(false);
                            ;
                        }

                   // }
                    return true;
                }
            });
           final CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(getContext());
            currencyTypeDBAdapter.open();
            List<CurrencyType> currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            final List<String> currenciesNames;

            currenciesNames = new ArrayList<String>();

            for (int i = 0; i < currencyTypesList.size(); i++) {
                currenciesNames.add(currencyTypesList.get(i).getType());
            }

            final int[] correct = {0};

            multiSelectListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                  List<String>  list=convertObjectToList(newValue);

                    if (list.size()==3){
                        List<String>  currenciesChoose=new ArrayList<String>();
                        currenciesChoose.add(SETTINGS.currencyCode);
                        for (int i=0;i<list.size();i++)  {
                            if (list.get(i).equals(SETTINGS.currencyCode)) {

                            }
                            else {
                                   currenciesChoose.add(list.get(i));
                            }
                        }

                             currencyTypeDBAdapter.delete();
                              Log.d("currencyTypeDBAdapterSize",currencyTypeDBAdapter.getAllCurrencyType().size()+"");
                              for (int i=0;i<currenciesChoose.size();i++)     {
                                currencyTypeDBAdapter.insertEntry(new CurrencyType(i,currenciesChoose.get(i)));     }
                              //    currencyTyhhhpeDBAdapter.insertEntry(currenciesChoose.get(i));   }
                                   Log.d("currencyTypeDBAdapterFirstItem",currencyTypeDBAdapter.getAllCurrencyType().get(0).getType()+" "+
                                   currencyTypeDBAdapter.getAllCurrencyType().get(1).getType()+""+
                                   currencyTypeDBAdapter.getAllCurrencyType().get(2).getType()+""+
                                   currencyTypeDBAdapter.getAllCurrencyType().get(3).getType()+"");

                       Log.d("currencirsChoose",currenciesChoose.toString());
                        multiSelectListPreference.setEnabled(false);
                   }
                   else {
                       Toast.makeText(getContext(), R.string.please_choose_three_currencies, Toast.LENGTH_SHORT).show();
                   }
                   return false;
               }
            });



        }
        public static List<String> convertObjectToList(Object obj) {
            List<?> list = new ArrayList<>();
            if (obj.getClass().isArray()) {
                list = Arrays.asList((Object[])obj);
            } else if (obj instanceof Collection) {
                list = new ArrayList<>((Collection<?>)obj);
            }
            return (List<String>) list;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
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
        private static String codeName = "";
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName(BO_SETTING);
            addPreferencesFromResource(R.xml.pref_data_sync);

            setHasOptionsMenu(true);

            findPreference("pos_db_version_no").setSummary(DbHelper.DATABASE_VERSION+"");

            findPreference("pos_version_no").setSummary(codeName);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
                codeName = pInfo.versionName;
                Log.i("TAG", "onAttach: "+codeName);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(getClass().getName(), e.getMessage());
            } catch (NoSuchMethodError emr) {
                Log.e(getClass().getName(), emr.getMessage());
            }

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class PinPadPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getPreferenceManager().setSharedPreferencesName(PINPAD_PREFERENCES);

            addPreferencesFromResource(R.xml.pref_pinpad);
            setHasOptionsMenu(true);


            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CompanyStatusPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName(POS_Company_status);
            addPreferencesFromResource(R.xml.pref_pos_company_status);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class BackUpPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValue(findPreference("example_text"));
            //bindPreferenceSummaryToValue(findPreference("example_list"));
            startActivity(new Intent(getActivity(), BackupActivity.class));


        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
