package com.pos.leaders.leaderspossystem.Tools;

import java.util.List;

/**
 * Created by KARAM on 30/03/2017.
 */

public abstract class StaticList<S> implements List {

    public static int saleNumber = 0;
    @Override
    public boolean add(Object o) {
        saleNumber++;
        return true;
    }

    public StaticList() {
        saleNumber = 0;
    }
}
