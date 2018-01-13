package com.yao.viewbind;

import android.app.Activity;
import android.support.annotation.IdRes;

public enum Finder {
    ACTIVITY {
        @Override
        public Object findView(Object activity, @IdRes int id) {
            return ((Activity) activity).findViewById(id);
        }
    };

    public Object findView(Object object, int id) {
        return null;
    }

}
