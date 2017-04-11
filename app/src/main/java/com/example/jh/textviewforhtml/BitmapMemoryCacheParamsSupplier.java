package com.example.jh.textviewforhtml;

import android.app.ActivityManager;
import android.os.Build;

import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.cache.MemoryCacheParams;

/**
 * Created by jh on 2017/1/16.
 */
public class BitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {
    private final ActivityManager mActivityManager;

    public BitmapMemoryCacheParamsSupplier(ActivityManager activityManager) {
        mActivityManager = activityManager;
    }

    @Override
    public MemoryCacheParams get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new MemoryCacheParams(getMaxCacheSize(), // 内存缓存中总图片的最大大小,以字节为单位。
                    56,                                     // 内存缓存中图片的最大数量。
                    Integer.MAX_VALUE,                      // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                    Integer.MAX_VALUE,                      // 内存缓存中准备清除的总图片的最大数量。
                    Integer.MAX_VALUE);                     // 内存缓存中单个图片的最大大小。
        } else {
            return new MemoryCacheParams(
                    getMaxCacheSize(),
                    256,
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
        }
    }

    private int getMaxCacheSize() {
        final int maxMemory = Math.min(mActivityManager.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
       // MLog.i(String.format("Fresco Max memory [%d] MB", (maxMemory/ByteConstants.MB)));
        if (maxMemory < 32 * ByteConstants.MB) {
            return 4 * ByteConstants.MB;
        } else if (maxMemory < 64 * ByteConstants.MB) {
            return 6 * ByteConstants.MB;
        } else {
            // We don't want to use more ashmem on Gingerbread for now, since it doesn't respond well to
            // native memory pressure (doesn't throw exceptions, crashes app, crashes phone)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                return 8 * ByteConstants.MB;
            } else {
                return maxMemory / 4;
            }
        }
    }
}
