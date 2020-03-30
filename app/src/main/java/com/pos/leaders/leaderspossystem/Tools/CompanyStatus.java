package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;

import com.pos.leaders.leaderspossystem.R;

/**
 * Created by Win8.1 on 3/29/2020.
 */

public enum CompanyStatus {
    BO_COMPANY(R.string.company),
    BO_AUTHORIZED_DEALER(R.string.authorized_dealer),
    BO_EXEMPT_DEALER(R.string.exempt_dealer);



    private final int strId;

    CompanyStatus(int strId) {
        this.strId = strId;
    }

    public String getString(Context context) {
        return context.getString(strId);
    }
}
