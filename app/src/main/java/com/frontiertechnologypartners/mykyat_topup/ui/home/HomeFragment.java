package com.frontiertechnologypartners.mykyat_topup.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.common.ViewModelFactory;
import com.frontiertechnologypartners.mykyat_topup.model.PreTopUpResponse;
import com.frontiertechnologypartners.mykyat_topup.model.Snc;
import com.frontiertechnologypartners.mykyat_topup.model.TotalSalesTotalCommissionResponse;
import com.frontiertechnologypartners.mykyat_topup.model.UserData;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.top_up.PreTopupFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.top_up.TopUpFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.transaction.TransactionFragment;
import com.frontiertechnologypartners.mykyat_topup.util.Util;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import javax.inject.Inject;

import static com.frontiertechnologypartners.mykyat_topup.model.Status.ERROR;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.UPDATE_AMOUNT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.USER_DATA;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    @BindView(R.id.tv_balance)
    TextView tvBalance;

    @BindView(R.id.tv_total_sales)
    TextView tvTotalSales;

    @BindView(R.id.tv_total_commission)
    TextView tvTotalCommission;

    private UserData userData;
    private String updateAmount = "";

    @Inject
    ViewModelFactory viewModelFactory;

    private HomeViewModel homeViewModel;

    public static HomeFragment newInstance(String updateAmount) {

        Bundle args = new Bundle();
        args.putString(UPDATE_AMOUNT, updateAmount);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            updateAmount = getArguments().getString(UPDATE_AMOUNT);
        }

        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);

        //get login user data
        Gson gson = new Gson();
        String loginUserData = prefs.fromPreference(USER_DATA, "");
        userData = gson.fromJson(loginUserData, UserData.class);

        String[] balance = Util.convertAmountWithFormat(userData.getBalance()).split("\\.");

        tvUserName.setText(getResources().getString(R.string.user_name, userData.getUser().getUsername()));
        if (updateAmount.equals("")) {
            tvBalance.setText(getResources().getString(R.string.user_balance, balance[0]));
        } else {
            tvBalance.setText(getResources().getString(R.string.user_balance, updateAmount));
        }

        homeViewModel.totalSalesAndCommission(userData.getId());
        //observe total sales and commission
        observeTotalSaleAndCommission();
    }

    private void observeTotalSaleAndCommission() {
        homeViewModel.totalSalesAndCommissionResponse.observe(this, this::consumeTotalSaleAndCommission);
    }

    private void consumeTotalSaleAndCommission(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                TotalSalesTotalCommissionResponse totalSalesTotalCommissionResponse = (TotalSalesTotalCommissionResponse) apiResponse.data;
                if (totalSalesTotalCommissionResponse != null) {
                    Snc snc = totalSalesTotalCommissionResponse.getSnc();
                    String[] totalSale = Util.convertAmountWithFormat(snc.getTotalTodaySales()).split("\\.");
                    String[] totalCommission = Util.convertAmountWithFormat(snc.getTotalTodayCommissions()).split("\\.");
                    tvTotalSales.setText(getResources().getString(R.string.total_sales, totalSale[0]));
                    tvTotalCommission.setText(getResources().getString(R.string.total_commissions, totalCommission[0]));
                }
                break;
            case ERROR:
                loadingDialog.dismiss();
                if (apiResponse.message != null) {
                    messageDialog.show();
                    messageDialog.loadingMessage(apiResponse.message);
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.btn_topup)
    void onClickTopUp() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().replace(R.id.frame, new PreTopupFragment()).commit();
        }
    }

    @OnClick(R.id.btn_transaction)
    void onClickTransaction() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().replace(R.id.frame, new TransactionFragment()).commit();
        }
    }
}