package com.frontiertechnologypartners.mykyat_topup.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.common.ViewModelFactory;
import com.frontiertechnologypartners.mykyat_topup.model.Language;
import com.frontiertechnologypartners.mykyat_topup.model.Logout;
import com.frontiertechnologypartners.mykyat_topup.model.UserData;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.change_language.LanguageChangeAdapter;
import com.frontiertechnologypartners.mykyat_topup.ui.change_password.ChangePasswordFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.logout.LogoutViewModel;
import com.frontiertechnologypartners.mykyat_topup.ui.top_up.PreTopupFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.transaction.TransactionFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.mmtextview.components.MMTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.ENGLISH;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.ENGLISH_EN;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MYANMAR;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MYANMAR_MM;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.UPDATE_AMOUNT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.USER_DATA;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, LanguageChangeAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Inject
    ViewModelFactory viewModelFactory;

    private String updateAmount = "";
    private LogoutViewModel logoutViewModel;
    private SparseIntArray mFragmentSelectedMap;
    private String defLanguage;
    private BottomSheetDialog languageDialog;
    private int langCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoutViewModel = ViewModelProviders.of(this, viewModelFactory).get(LogoutViewModel.class);

        //init
        ButterKnife.bind(this);
//        toolbar.setBackground(getResources().getDrawable(R.drawable.ic_toolbar_background));
//        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        defLanguage = Locale.getDefault().getLanguage();
        //change language
        if (defLanguage.equals(MYANMAR)) {
            langCheck = 0;
        } else if (defLanguage.equals(ENGLISH)) {
            langCheck = 1;
        }

        //update balance amount
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            updateAmount = bundle.getString(UPDATE_AMOUNT);
        }
        setTitleMap();
        navView.setNavigationItemSelectedListener(this);
        setTitleFragment(mFragmentSelectedMap.get(R.id.nav_topUp));
        displaySelectedScreen(R.id.nav_topUp);
        observeLogout();
    }

    private void setTitleMap() {
        mFragmentSelectedMap = new SparseIntArray();
        mFragmentSelectedMap.append(R.id.nav_topUp, R.string.top_up);
        mFragmentSelectedMap.append(R.id.nav_transaction, R.string.transaction);
        mFragmentSelectedMap.append(R.id.nav_change_password, R.string.change_password);
    }

    private void observeLogout() {
        logoutViewModel.logoutResponse.observe(this, this::consumeLogoutResponse);
    }

    private void consumeLogoutResponse(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                Logout logout = (Logout) apiResponse.data;
                if (logout != null) {
                    if (logout.getResponseMessage().getCode() == 1) {
                        prefs.clearPreferences();
                        finishAffinity();
                        Toast.makeText(this, R.string.success_logout, Toast.LENGTH_SHORT).show();
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(logout.getResponseMessage().getMessage());
                    }
                }
                break;
            case ERROR:
                if (apiResponse.message != null) {
                    messageDialog.show();
                    messageDialog.loadingMessage(apiResponse.message);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.language_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem changeLangMenuItem = menu.findItem(R.id.menu_lang);
        LinearLayout rootView = (LinearLayout) changeLangMenuItem.getActionView();

        ImageView langLogo = rootView.findViewById(R.id.img_language_logo);
        MMTextView langName = rootView.findViewById(R.id.tv_language);

        //change language
        if (defLanguage.equals(MYANMAR)) {
            langLogo.setImageResource(R.drawable.ic_myanmar_flag);
            langName.setText(MYANMAR_MM);
        } else if (defLanguage.equals(ENGLISH)) {
            langLogo.setImageResource(R.drawable.ic_english_flag);
            langName.setText(ENGLISH_EN);
        }

        rootView.setOnClickListener(v -> onOptionsItemSelected(changeLangMenuItem));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_lang) {
            languageChangeDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void languageChangeDialog() {
        View view = getLayoutInflater().inflate(R.layout.language_change_layout, null);
        RecyclerView rvLanguage = view.findViewById(R.id.rv_language);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvLanguage.setLayoutManager(linearLayoutManager);
        rvLanguage.setHasFixedSize(true);
        rvLanguage.setItemAnimator(new DefaultItemAnimator());
        LanguageChangeAdapter languageAdapter = new LanguageChangeAdapter(this, this);
        rvLanguage.setAdapter(languageAdapter);

        List<Language> languages = Language();
        languageAdapter.setItems(languages, langCheck);

        languageDialog = new BottomSheetDialog(this);
        languageDialog.setContentView(view);
        languageDialog.show();
    }

    public List<Language> Language() {
        List<Language> languages = new ArrayList<>();

        languages.add(new Language(R.drawable.ic_myanmar_flag, getResources().getString(R.string.myanmar)));
        languages.add(new Language(R.drawable.ic_english_flag, getResources().getString(R.string.english)));

        return languages;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        setTitleFragment(mFragmentSelectedMap.get(id));
        displaySelectedScreen(id);
        return true;
    }

    private void setTitleFragment(int title) {
        if (title != 0)
            setTitle(title);
    }

    private void displaySelectedScreen(int itemId) {
        navView.setCheckedItem(itemId);
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_topUp:
                fragment = PreTopupFragment.newInstance(updateAmount);
                break;
            case R.id.nav_transaction:
                fragment = new TransactionFragment();
                break;
            case R.id.nav_change_password:
                fragment = new ChangePasswordFragment();
                break;
            case R.id.nav_logout:
                logout();
                break;
            default:
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment)
                    .commit();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logout() {
        //get login user data
        Gson gson = new Gson();
        String loginUserData = prefs.fromPreference(USER_DATA, "");
        UserData userData = gson.fromJson(loginUserData, UserData.class);
        logoutViewModel.logout(userData.getUser().getId());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment currentFragment = getSupportFragmentManager()
                    .findFragmentById(R.id.frame);
            if (currentFragment instanceof PreTopupFragment) {
                prefs.clearPreferences();
                finishAffinity();
            } else {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    navView.getMenu().getItem(0).setChecked(true);
                    setTitleFragment(mFragmentSelectedMap.get(R.id.nav_topUp));
                    displaySelectedScreen(R.id.nav_topUp);
                }
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0) {
            setNewLocale(MYANMAR);
        } else {
            setNewLocale(ENGLISH);
        }
        languageDialog.dismiss();
    }
}
