package com.frontiertechnologypartners.mykyat_topup.di.module;

import com.frontiertechnologypartners.mykyat_topup.ui.change_password.ChangePasswordFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.home.HomeFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.login.LoginFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.top_up.TopUpFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.top_up.PreTopupFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.transaction.TransactionFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract PreTopupFragment provideTopupFragment();

    @ContributesAndroidInjector
    abstract TopUpFragment providePreTopUpFragment();

    @ContributesAndroidInjector
    abstract TransactionFragment provideTransactionFragment();

    @ContributesAndroidInjector
    abstract ChangePasswordFragment provideChangeFragment();

    @ContributesAndroidInjector
    abstract LoginFragment provideLoginFragment();

    @ContributesAndroidInjector
    abstract HomeFragment provideHomeFragment();

}
