package com.pos.leaders.leaderspossystem.Tools;

import android.text.format.Time;

/**
 * Created by KARAM on 29/05/2018.
 */

public interface OnClockTickListner {
    public void OnSecondTick(Time currentTime);
    public void OnMinuteTick(Time currentTime);
}
