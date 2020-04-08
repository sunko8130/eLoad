package com.frontiertechnologypartners.mykyat_topup.ui.top_up;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.common.ViewModelFactory;
import com.frontiertechnologypartners.mykyat_topup.model.Operators;
import com.frontiertechnologypartners.mykyat_topup.model.PreTopUpResponse;
import com.frontiertechnologypartners.mykyat_topup.model.ProvidersResponse;
import com.frontiertechnologypartners.mykyat_topup.model.UserData;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseFragment;
import com.frontiertechnologypartners.mykyat_topup.util.Util;
import com.frontiertechnologypartners.mykyat_topup.widget.GridSpacingItemDecoration;
import com.frontiertechnologypartners.mykyat_topup.widget.MMPhoneChecker.MyanmarPhoneNumber;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import javax.inject.Inject;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.CONTACT_REQUEST_CODE;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.DRAWABLE_RIGHT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.UPDATE_AMOUNT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.USER_DATA;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreTopupFragment extends BaseFragment implements View.OnClickListener, OperatorCheckboxAdapter.OnItemClickListener {

    @BindView(R.id.et_ph_no)
    TextInputEditText etPhoneNo;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.operator_recycler_view)
    RecyclerView operatorRv;

    @BindView(R.id.btn_1000)
    Button btnAmount1000;

    @BindView(R.id.btn_3000)
    Button btnAmount3000;

    @BindView(R.id.btn_5000)
    Button btnAmount5000;

    @BindView(R.id.btn_10000)
    Button btnAmount10000;

    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    @BindView(R.id.tv_balance)
    TextView tvBalance;

    @Inject
    ViewModelFactory viewModelFactory;
    private TopupViewModel topupViewModel;
    private String updateAmount = "";

    private OperatorCheckboxAdapter operatorCheckboxAdapter;
    private String selectedProvider = "";
    private String selectedAmount = "";
    private String mobileNumber = "";
    private Button selectAmountButton;
    private UserData userData;
    private String amount;

    public static PreTopupFragment newInstance(String updateAmount) {

        Bundle args = new Bundle();
        args.putString(UPDATE_AMOUNT, updateAmount);
        PreTopupFragment fragment = new PreTopupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init
        ButterKnife.bind(this, view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        topupViewModel = ViewModelProviders.of(this, viewModelFactory).get(TopupViewModel.class);

        //select operator
        radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton radioButton = radioGroup.findViewById(checkedId);
            if (null != radioButton && checkedId > -1) {
                Toast.makeText(getContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        //read phone contact
        etPhoneNo.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getX() >= (etPhoneNo.getWidth() - etPhoneNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    readPhoneContact();
                    return true;
                }
            }
            return false;
        });

        //select amount
        chipGroup.setOnCheckedChangeListener((chipGroup, i) -> {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip != null)
                Toast.makeText(mContext, "Chip is " + chip.getChipText(), Toast.LENGTH_SHORT).show();
        });

        //top up available amount
        operatorRv.setLayoutManager(new GridLayoutManager(mContext, 2));
        operatorRv.addItemDecoration(new GridSpacingItemDecoration(2, Util.dpToPx(mContext, 16), true));
        operatorRv.setHasFixedSize(true);
        operatorRv.setItemAnimator(new DefaultItemAnimator());
        operatorCheckboxAdapter = new OperatorCheckboxAdapter(getActivity(), this);
        operatorRv.setAdapter(operatorCheckboxAdapter);

//        List<Operator> availableOperator = availableOperator();

        //top up available amount
        btnAmount1000.setOnClickListener(this);
        btnAmount3000.setOnClickListener(this);
        btnAmount5000.setOnClickListener(this);
        btnAmount10000.setOnClickListener(this);

        if (getArguments() != null) {
            updateAmount = getArguments().getString(UPDATE_AMOUNT);
        }

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
        //load available providers
        topupViewModel.getAvailableProviders();

        observeProvidersData();
        observePreTopUpData();

    }

    private void observeProvidersData() {
        topupViewModel.providersResponse.observe(this, this::consumeResponse);
    }

    private void observePreTopUpData() {
        topupViewModel.preTopUpResponse.observe(this, this::consumePreTopResponse);
    }

    private void consumePreTopResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                PreTopUpResponse preTopUpResponse = (PreTopUpResponse) apiResponse.data;
                if (preTopUpResponse != null) {
                    int statusCode = preTopUpResponse.getResponseMessage().getCode();
                    if (statusCode == 1) {

                        //pre top-up fragment
                        double detectAmount = preTopUpResponse.getData();
//                        String provider = preTopUpResponse.getResponseMessage().getMessage();
                        Fragment topUpFragment = TopUpFragment.newInstance(mobileNumber,
                                amount,
                                detectAmount,
                                selectedProvider,
                                String.valueOf(userData.getUser().getId()));

                        if (getFragmentManager() != null) {
                            getFragmentManager().beginTransaction().replace(R.id.frame, topUpFragment)
                                    .commit();
                        }

                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(preTopUpResponse.getResponseMessage().getMessage());
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

    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                ProvidersResponse providersResponse = (ProvidersResponse) apiResponse.data;
                if (providersResponse != null) {
                    List<Operators> availableProviders = providersResponse.getData();
                    operatorCheckboxAdapter.setItems(availableProviders);
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

    @OnClick(R.id.btn_continue)
    void onClickContinue() {
        //mobile number
        if (!TextUtils.isEmpty(etPhoneNo.getText())) {
            String phone = etPhoneNo.getText().toString().trim();
            mobileNumber = phone.replaceAll("\\s+", "");
        }

        //pre top up
        if (selectedProvider.equals("")) {
            messageDialog.show();
            messageDialog.loadingMessage(getString(R.string.operator_require_msg));
        } else if (mobileNumber.equals("")) {
            messageDialog.show();
            messageDialog.loadingMessage(getString(R.string.mobile_require_msg));
        } else if (selectedAmount.equals("")) {
            messageDialog.show();
            messageDialog.loadingMessage(getString(R.string.amount_require_msg));
        } else if (mobileNumber.length() < 9) {
            messageDialog.show();
            messageDialog.loadingMessage(getString(R.string.invalid_mobile_no));
        } else {
            MyanmarPhoneNumber myanmarPhoneNumber = new MyanmarPhoneNumber();
            String operatorName = myanmarPhoneNumber.getTelecomName(mobileNumber);
            if (operatorName.equals("Unknown")) {
                messageDialog.show();
                messageDialog.loadingMessage(getString(R.string.invalid_mobile_no));
            } else if (!operatorName.equalsIgnoreCase(selectedProvider)) {
                messageDialog.show();
                messageDialog.loadingMessage(getString(R.string.mobile_and_provider_not_match));
            } else {
                amount = selectAmountButton.getText().toString().trim().replace(",", "");
                topupViewModel.getPreTopUpData(String.valueOf(userData.getUser().getId()),
                        selectedProvider,
                        mobileNumber,
                        amount
                );
            }
        }
    }

//    private List<Operator> availableOperator() {
//        String[] operatorList = getResources().getStringArray(R.array.operator_list);
//        List<Operator> operators = new ArrayList<>();
//        for (String operator : operatorList) {
//            Operator topUpOperator = new Operator();
//            topUpOperator.setOperator(operator);
//            operators.add(topUpOperator);
//        }
//        return operators;
//    }

    private void readPhoneContact() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                        startActivityForResult(intent, CONTACT_REQUEST_CODE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            Util.showSettingsDialog(getActivity());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uriContact = data.getData();
                retrieveContactNumber(uriContact);
            }
        }
    }

    private void retrieveContactNumber(Uri uriContact) {
        String phNumber = "";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        if (uriContact != null) {
            if (getActivity() != null) {
                Cursor cursorPhone = getActivity().getContentResolver().query(
                        uriContact,
                        projection, null, null, null
                );
                if (cursorPhone != null && cursorPhone.moveToFirst()) {
                    phNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    cursorPhone.close();
                }
                etPhoneNo.setText("");
                etPhoneNo.setText(phNumber);
            }

        }
    }

    @Override
    public void onClick(View view) {
        selectAmountButton = (Button) view;

        // clear state
        btnAmount1000.setSelected(false);
        btnAmount1000.setPressed(false);
        btnAmount3000.setSelected(false);
        btnAmount3000.setPressed(false);
        btnAmount5000.setSelected(false);
        btnAmount5000.setPressed(false);
        btnAmount10000.setSelected(false);
        btnAmount10000.setPressed(false);

        // change state
        selectAmountButton.setSelected(true);
        selectAmountButton.setPressed(false);

        selectedAmount = selectAmountButton.getText().toString();

        Toast.makeText(getActivity(), "amount: " + selectAmountButton.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        Operators operator = operatorCheckboxAdapter.getItem(position);
        selectedProvider = operator.getOperator();
        Toast.makeText(getContext(), "click " + operator.getOperator(), Toast.LENGTH_SHORT).show();
    }
}
