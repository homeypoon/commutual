package com.example.commutual;

import android.view.View;

public interface PostClickListener<T> {

    void onClick(View view, T data, int position);
}
