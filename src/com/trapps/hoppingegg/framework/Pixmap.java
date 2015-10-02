package com.trapps.hoppingegg.framework;

import com.trapps.hoppingegg.framework.Graphics.PixmapFormat;

public interface Pixmap {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}
