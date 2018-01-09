package com.yaopaine.annotationpractise.annotation.util;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.yaopaine.annotationpractise.BuildConfig;
import com.yaopaine.annotationpractise.annotation.ContentView;
import com.yaopaine.annotationpractise.annotation.OnClick;
import com.yaopaine.annotationpractise.annotation.ViewInject;

import java.lang.reflect.*;

public class ViewInjectUtils {
    private final static boolean debug = BuildConfig.DEBUG;

    public static void inject(AppCompatActivity activity) {
        injectContentView(activity);
        injectView(activity);
        injectClickEvent(activity);
    }

    private static void injectClickEvent(final AppCompatActivity activity) {
        Class<? extends AppCompatActivity> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (final Method method2 : methods) {
            OnClick methodAnnotation = method2.getAnnotation(OnClick.class);
            if (methodAnnotation == null) continue;
            int[] viewId = methodAnnotation.value();
            method2.setAccessible(true);
            Object listener = Proxy.newProxyInstance(View.OnClickListener.class.getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                    return method2.invoke(activity, args);
                }
            });
            for (int id : viewId) {
                View view = activity.findViewById(id);
                try {
                    Method click = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
                    click.invoke(view, listener);
                } catch (NoSuchMethodException e) {

                } catch (IllegalAccessException e) {

                } catch (InvocationTargetException e) {
                }
                /*view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            method2.invoke(activity, view);
                        } catch (IllegalAccessException e) {

                        } catch (InvocationTargetException e) {
                        }
                    }
                });*/
            }
        }
    }

    private static void injectView(AppCompatActivity activity) {
        Class<? extends AppCompatActivity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();//获取activity的所有成员变量
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int viewId = viewInject.value();
                View view = activity.findViewById(viewId);
                try {
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    if (debug) Log.e("TAG", "injectView: " + e);
                }
            }
        }
    }

    private static void injectContentView(AppCompatActivity activity) {
        Class<? extends AppCompatActivity> activityClass = activity.getClass();
        ContentView contentView = activityClass.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            try {
                Method method = activityClass.getMethod("setContentView", int.class);
                method.invoke(activity, layoutId);
            } catch (NoSuchMethodException e) {

            } catch (InvocationTargetException e) {

            } catch (IllegalAccessException e) {

            }
        }
    }
}
