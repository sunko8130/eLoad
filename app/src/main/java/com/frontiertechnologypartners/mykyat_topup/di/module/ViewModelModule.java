package com.frontiertechnologypartners.mykyat_topup.di.module;

import com.frontiertechnologypartners.mykyat_topup.common.ViewModelFactory;
import com.frontiertechnologypartners.mykyat_topup.di.keys.ViewModelKey;
import com.frontiertechnologypartners.mykyat_topup.model.Transaction;
import com.frontiertechnologypartners.mykyat_topup.ui.change_password.ChangePassViewModel;
import com.frontiertechnologypartners.mykyat_topup.ui.forgot_password.ForgotPassViewModel;
import com.frontiertechnologypartners.mykyat_topup.ui.home.HomeViewModel;
import com.frontiertechnologypartners.mykyat_topup.ui.login.LoginViewModel;
import com.frontiertechnologypartners.mykyat_topup.ui.logout.LogoutViewModel;
import com.frontiertechnologypartners.mykyat_topup.ui.top_up.TopupViewModel;
import com.frontiertechnologypartners.mykyat_topup.ui.transaction.TransactionViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TopupViewModel.class)
    abstract ViewModel bindTopupViewModel(TopupViewModel topupViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransactionViewModel.class)
    abstract ViewModel bindTransactionViewModel(TransactionViewModel transactionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChangePassViewModel.class)
    abstract ViewModel bindChangePassViewModel(ChangePassViewModel changePassViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LogoutViewModel.class)
    abstract ViewModel bindLogoutViewModel(LogoutViewModel logoutViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPassViewModel.class)
    abstract ViewModel bindForgotPassViewModel(ForgotPassViewModel forgotPassViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
