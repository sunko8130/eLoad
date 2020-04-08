package com.frontiertechnologypartners.mykyat_topup.di.module;

import com.frontiertechnologypartners.mykyat_topup.ui.MainActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.change_password.ChangePasswordActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.forgot_password.ForgotPasswordActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.login.LoginActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector()
    abstract ForgotPasswordActivity bindForgotPasswordActivity();

    @ContributesAndroidInjector()
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector()
    abstract ChangePasswordActivity bindChangePasswordActivity();
}
