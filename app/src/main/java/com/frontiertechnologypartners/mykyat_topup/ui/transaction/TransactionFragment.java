package com.frontiertechnologypartners.mykyat_topup.ui.transaction;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.common.ViewModelFactory;
import com.frontiertechnologypartners.mykyat_topup.model.Operators;
import com.frontiertechnologypartners.mykyat_topup.model.ProvidersResponse;
import com.frontiertechnologypartners.mykyat_topup.model.Transaction;
import com.frontiertechnologypartners.mykyat_topup.model.TransactionResponse;
import com.frontiertechnologypartners.mykyat_topup.model.UserData;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.delegate.OnRecyclerItemClickListener;
import com.frontiertechnologypartners.mykyat_topup.util.Util;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.CONTACT_REQUEST_CODE;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.DRAWABLE_RIGHT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.USER_DATA;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends BaseFragment implements OnRecyclerItemClickListener {

    @BindView(R.id.transaction_rv)
    RecyclerView mTransactionRv;

    @BindView(R.id.tv_no_transaction)
    TextView mNoTransaction;

    @BindView(R.id.tv_transaction_label)
    TextView mTransactionLabel;

    @BindView(R.id.tv_from_date)
    TextView mFromDate;

    @BindView(R.id.tv_to_date)
    TextView mToDate;

    @BindView(R.id.tv_phone_no)
    TextView mPhoneNo;

    @BindView(R.id.tv_operator)
    TextView mOperator;

    @BindView(R.id.btn_from_date)
    TextView btnFromDate;

    @BindView(R.id.btn_to_date)
    TextView btnToDate;

    @BindView(R.id.tv_total_Amount)
    TextView tvTotalAmount;

    @BindView(R.id.tv_total_commission)
    TextView tvTotalCommssion;

    @BindView(R.id.operator_spinner)
    AppCompatSpinner mOperatorSpinner;

    @BindView(R.id.et_ph_no)
    TextInputEditText etPhoneNo;

    @Inject
    ViewModelFactory viewModelFactory;
    private TransactionViewModel transactionViewModel;
    private TransactionAdapter transactionAdapter;
    private String userId = "";
    private String fromDate = "";
    private String toDate = "";
    private String operatorType = "All";
    private String phoneNumber = "";
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener fromDateListener;
    private DatePickerDialog.OnDateSetListener toDateListener;
    private Date startDate = null, endDate = null;

    public static TransactionFragment newInstance() {

        Bundle args = new Bundle();

        TransactionFragment fragment = new TransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity.setTitle(R.string.transaction);
        ButterKnife.bind(this, view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        transactionViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionViewModel.class);

        //get login user data
        Gson gson = new Gson();
        String loginUserData = prefs.fromPreference(USER_DATA, "");
        UserData userData = gson.fromJson(loginUserData, UserData.class);
        userId = String.valueOf(userData.getUser().getId());


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

        //choose date
        chooseDate();

        //top up available amount
        mTransactionRv.setLayoutManager(new LinearLayoutManager(mContext));
        mTransactionRv.setHasFixedSize(true);
        mTransactionRv.setItemAnimator(new DefaultItemAnimator());
        mTransactionRv.setNestedScrollingEnabled(false);
        transactionAdapter = new TransactionAdapter(mContext, this);


        ArrayAdapter<String> operatorAdapter = new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, R.id.spinner_text,
                Arrays.asList(getResources().getStringArray(R.array.operator_list)));
        mOperatorSpinner.setAdapter(operatorAdapter);

        //load all operators
//        transactionViewModel.getAllOperators();
//        observeOperators();

        transactionHistory();
        observeTransaction();

    }

    private void chooseDate() {
        //from date
        btnFromDate.setOnClickListener(v -> new DatePickerDialog(mContext, fromDateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        //to date
        btnToDate.setOnClickListener(v -> new DatePickerDialog(mContext, toDateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        fromDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            fromDate();
        };

        toDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            toDate();
        };
    }

    private void fromDate() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fromDate = sdf.format(calendar.getTime());
        startDate = calendar.getTime();
        btnFromDate.setText(sdf.format(calendar.getTime()));
    }

    private void toDate() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        toDate = sdf.format(calendar.getTime());
        endDate = calendar.getTime();
        btnToDate.setText(sdf.format(calendar.getTime()));
    }

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

    private void observeOperators() {
        transactionViewModel.allOperatorsResponse.observe(this, this::consumeAllOperators);
    }

    private void consumeAllOperators(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                ProvidersResponse providersResponse = (ProvidersResponse) apiResponse.data;
                if (providersResponse != null) {
                    if (providersResponse.getResponseMessage().getCode() == 1) {
                        List<Operators> allOperators = providersResponse.getData();
                        OperatorsAdapter operatorAdapter = new OperatorsAdapter(mContext,
                                R.layout.dropdown_menu_popup_item, R.id.spinner_text, allOperators);
                        mOperatorSpinner.setAdapter(operatorAdapter);
                        //load transaction
                        Operators operators = (Operators) mOperatorSpinner.getSelectedItem();
                        operatorType = operators.getOperatorName();

                        transactionHistory();
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(providersResponse.getResponseMessage().getMessage());
                    }
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

    private void observeTransaction() {
        transactionViewModel.transactionResponse.observe(this, this::consumeTransactionResponse);
    }

    private void consumeTransactionResponse(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                mTransactionRv.setAdapter(null);
                transactionAdapter.clear();

                TransactionResponse transaction = (TransactionResponse) apiResponse.data;
                if (transaction != null) {
                    if (transaction.getResponseMessage().getCode() == 1) {
                        List<Transaction> transactions = transaction.getData();
                        if (transactions != null && transactions.size() > 0) {
                            //clear date
                            btnFromDate.setText("");
                            btnToDate.setText("");
                            //show total amount
                            double totalAmount = 0;
                            double totalCommission = 0;
                            for (Transaction transaction1 : transactions) {
                                totalAmount += transaction1.getAmount();
                                totalCommission += transaction1.getCommissionAmount();
                            }
                            String[] topUpAmount = Util.convertAmountWithFormat(totalAmount).split("\\.");
                            tvTotalAmount.setText(getString(R.string.total_amount, topUpAmount[0]));
                            String[] commission = Util.convertAmountWithFormat(totalCommission).split("\\.");
                            tvTotalCommssion.setText(getString(R.string.total_commission, commission[0]));

                            mTransactionRv.setAdapter(transactionAdapter);
                            transactionAdapter.setItems(transactions);
                            mNoTransaction.setVisibility(View.GONE);
                            tvTotalAmount.setVisibility(View.VISIBLE);
                            tvTotalCommssion.setVisibility(View.VISIBLE);
                        } else {
                            tvTotalAmount.setVisibility(View.GONE);
                            tvTotalCommssion.setVisibility(View.GONE);
                            mNoTransaction.setVisibility(View.VISIBLE);
                        }

                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(transaction.getResponseMessage().getMessage());
                    }
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

    @OnClick(R.id.btn_search)
    void btnSearch() {

        if (!TextUtils.isEmpty(etPhoneNo.getText())) {
            String phone = etPhoneNo.getText().toString().trim();
            phoneNumber = phone.replaceAll("\\s+", "");
        } else {
            phoneNumber = "";
        }
//        Operators operators = (Operators) mOperatorSpinner.getSelectedItem();
//        operatorType = operators.getOperatorName();

        operatorType = mOperatorSpinner.getSelectedItem().toString();

        //date compare
        if (startDate != null && endDate != null) {
            if (startDate.compareTo(endDate) > 0) {
                messageDialog.show();
                messageDialog.loadingMessage(getString(R.string.date_compare));
            } else {
                transactionHistory();
            }
        } else {
            transactionHistory();
        }

    }

    private void transactionHistory() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("fromDate", fromDate);
        parameters.put("toDate", toDate);
        parameters.put("phoneNo", phoneNumber);
        parameters.put("usecase", operatorType);
        transactionViewModel.transactionHistory(parameters);
    }

    @Override
    public void onItemClick(int position) {
        //TODO: item click action
    }
}
