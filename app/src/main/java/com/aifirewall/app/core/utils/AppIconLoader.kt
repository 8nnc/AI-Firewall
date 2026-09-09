package com.aifirewall.app.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AppIconLoader {
    // Cache to prevent reloading icons and avoid OOM
    private val iconCache: LruCache<String, ImageBitmap>

    init {
        // Use 1/8th of the available memory for this memory cache.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        iconCache = object : LruCache<String, ImageBitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: ImageBitmap): Int {
                // The cache size will be measured in kilobytes rather than number of items.
                // An ImageBitmap does not expose byteCount directly, so we estimate based on the typical
                // format (ARGB_8888 -> 4 bytes per pixel)
                return (bitmap.width * bitmap.height * 4) / 1024
            }
        }
    }

    suspend fun getAppIcon(context: Context, packageName: String): ImageBitmap? = withContext(Dispatchers.IO) {
        iconCache.get(packageName)?.let { return@withContext it }

        try {
            val packageManager = context.packageManager
            val drawable = packageManager.getApplicationIcon(packageName)
            val bitmap = drawableToBitmap(drawable)
            
            if (bitmap != null) {
                val imageBitmap = bitmap.asImageBitmap()
                iconCache.put(packageName, imageBitmap)
                return@withContext imageBitmap
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // App might have been uninstalled
        } catch (e: Exception) {
            // Ignore corrupted drawables or other exceptions
        }
        null
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            // AdaptiveIconDrawable or similar where intrinsic size might be <= 0
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
