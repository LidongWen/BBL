package com.beibeilian.beibeilian.circle.spannable;


import android.text.SpannableString;
import android.widget.Toast;

import com.beibeilian.beibeilian.me.application.BBLApplication;


public class NameClickListener implements ISpanClick {
    private SpannableString userName;
    private String userId;

    public NameClickListener(SpannableString name, String userid) {
        this.userName = name;
        this.userId = userid;
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(BBLApplication.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
    }
}
