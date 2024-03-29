package com.example.omr.TOAST;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class CUSTOM_TOAST {
    private Context mcontext;

    public CUSTOM_TOAST(Context context){
        this.mcontext=context;
    }
    public void ToastError(String Error){
        Toasty.error(mcontext,Error, Toast.LENGTH_LONG).show();
    }
    public void ToastSuccess(String Success){
        Toasty.success(mcontext,Success,Toast.LENGTH_SHORT).show();
    }
    public void ToastInfo(String Info){
        Toasty.info(mcontext,Info,Toast.LENGTH_SHORT).show();
    }
    public void ToastWarning(String Warning){
        Toasty.warning(mcontext,Warning,Toast.LENGTH_SHORT).show();
    }
}
