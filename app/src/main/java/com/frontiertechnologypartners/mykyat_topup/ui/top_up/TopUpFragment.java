package com.frontiertechnologypartners.mykyat_topup.ui.top_up;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.common.ViewModelFactory;
import com.frontiertechnologypartners.mykyat_topup.model.SVAResponse;
import com.frontiertechnologypartners.mykyat_topup.model.TopUpResponse;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.ui.MainActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseFragment;
import com.frontiertechnologypartners.mykyat_topup.util.Util;
import com.frontiertechnologypartners.mykyat_topup.widget.MessageDialog;

import javax.inject.Inject;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.DETECT_AMOUNT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MOBILE_NO;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MPT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MYTEL;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.OOREDOO;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.PROVIDER;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.TELENOL;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.TOP_UP_AMOUNT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.UPDATE_AMOUNT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopUpFragment extends BaseFragment implements MessageDialog.OnClickListener {

    @BindView(R.id.tv_mobile)
    TextView tvMobile;

    @BindView(R.id.tv_top_up_amount)
    TextView tvTopUpAmount;

    @BindView(R.id.tv_detect_amount)
    TextView tvDetectAmount;

    @BindView(R.id.iv_operator)
    ImageView operatorLogo;

    private double detectAmount;
    private String mobile;
    private String amount;
    private String provider;
    private String userId;
    private String topUpSuccess = "";

    @Inject
    ViewModelFactory viewModelFactory;
    private TopupViewModel topupViewModel;
    private int updateAmount;


    static TopUpFragment newInstance(String mobile, String amount,
                                     double detectAmount, String provider,
                                     String userId) {

        Bundle args = new Bundle();
        TopUpFragment fragment = new TopUpFragment();
        args.putString(MOBILE_NO, mobile);
        args.putString(TOP_UP_AMOUNT, amount);
        args.putString(PROVIDER, provider);
        args.putString(USER_ID, userId);
        args.putDouble(DETECT_AMOUNT, detectAmount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_top_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        topupViewModel = ViewModelProviders.of(this, viewModelFactory).get(TopupViewModel.class);

        if (getArguments() != null) {
            mobile = getArguments().getString(MOBILE_NO);
            amount = getArguments().getString(TOP_UP_AMOUNT);
            detectAmount = getArguments().getDouble(DETECT_AMOUNT);
            provider = getArguments().getString(PROVIDER);
            userId = getArguments().getString(USER_ID);

            //show operator logo
            switch (provider) {
                case MPT:
                    operatorLogo.setImageResource(R.drawable.ic_mpt);
                    break;
                case OOREDOO:
                    operatorLogo.setImageResource(R.drawable.ic_ooredoo);
                    break;
                case TELENOL:
                    operatorLogo.setImageResource(R.drawable.ic_telenol);
                    break;
                case MYTEL:
                    operatorLogo.setImageResource(R.drawable.ic_mytel);
                    break;
            }
        }

        //top up info
        tvMobile.setText(getString(R.string.top_up_mobile, mobile));
        tvTopUpAmount.setText(getString(R.string.top_up_amount, Util.convertAmountWithFormat(Double.parseDouble(amount))));
        String[] detectTopUpAmount = Util.convertAmountWithFormat(detectAmount).split("\\.");
        tvDetectAmount.setText(getString(R.string.top_up_detect_amount, detectTopUpAmount[0]));

        observeTopUp();
        observeRefreshAmount();

    }

    private void observeTopUp() {
        topupViewModel.topUpResponse.observe(this, this::consumeTopUpResponse);
    }

    private void consumeTopUpResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                TopUpResponse topUpResponse = (TopUpResponse) apiResponse.data;
                if (topUpResponse != null) {
                    if (topUpResponse.getResponseMessage().getCode() == 1) {
                        topUpSuccess = topUpResponse.getResponseMessage().getMessage();
                        topupViewModel.refreshAmount(userId);
                        Toast.makeText(mContext, R.string.success_top_up, Toast.LENGTH_SHORT).show();
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(topUpResponse.getResponseMessage().getMessage());
                    }
                }
                break;
            case ERROR:
                loadingDialog.dismiss();
                if (apiResponse.message != null) {
                    messageDialog.show();
                    messageDialog.loadingMessage(apiResponse.message.getMessage());
                }
                break;
            default:
                break;
        }
    }

    private void observeRefreshAmount() {
        topupViewModel.svaResponse.observe(this, this::consumeSVAResponse);
    }

    private void consumeSVAResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                SVAResponse refreshAmount = (SVAResponse) apiResponse.data;
                if (refreshAmount != null) {
                    updateAmount = refreshAmount.getSva();
                    messageDialog.setListener(this);
                    messageDialog.show();
                    messageDialog.loadingMessage(topUpSuccess);
                }
                break;
            case ERROR:
                if (apiResponse.message != null) {
                    messageDialog.show();
                    messageDialog.loadingMessage(apiResponse.message.getMessage());
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.btn_continue)
    void onClickContinue() {
        topupViewModel.topUp(userId, provider, mobile, amount);
    }

    @Override
    public void onOkButtonClick() {
        Intent mainIntent = new Intent(mContext, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(UPDATE_AMOUNT, String.valueOf(updateAmount));
        mainIntent.putExtras(bundle);
        if (getActivity() != null) {
            getActivity().overridePendingTransition(0, 0);
            startActivity(mainIntent);
            getActivity().overridePendingTransition(0, 0);
        }
    }
}
