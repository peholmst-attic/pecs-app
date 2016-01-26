/*
 * Copyright (C) 2016 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.pkhsolutions.pecsapp.control;

import net.pkhsolutions.pecsapp.entity.PageLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Control class for scaling {@link BufferedImage}s.
 */
@Service
public class PictureScale {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureScale.class);

    PictureScale() {
    }

    /**
     * TODO Document me
     *
     * @param image
     * @param pageLayout
     * @return
     */
    public BufferedImage scaleIfNecessary(BufferedImage image, PageLayout pageLayout) {
        return scaleIfNecessary(image, convertMmToPx(pageLayout.getPictureHeightMm()),
                convertMmToPx(pageLayout.getPictureWidthMm()));
    }

    /**
     * TODO Document me
     *
     * @param image
     * @param maxHeightInPx
     * @param maxWidthInPx
     * @return
     */
    public BufferedImage scaleIfNecessary(BufferedImage image, int maxHeightInPx, int maxWidthInPx) {
        int w = image.getWidth();
        int h = image.getHeight();

        int newW;
        int newH;

        if (w <= maxWidthInPx && h <= maxHeightInPx) {
            return image;
        } else if (w > h) {
            newW = maxWidthInPx;
            newH = newW * h / w;
        } else {
            newH = maxHeightInPx;
            newW = newH * w / h;
        }
        LOGGER.info("Original size was w{} x h{}, new size is w{} x h{}", w, h, newW, newH);
        Image tmp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage img = new BufferedImage(newW, newH, image.getType());
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return img;
    }

    /**
     * TODO Document me
     *
     * @param mm
     * @return
     */
    public int convertMmToPx(int mm) {
        return (int) (3.779527559 * mm);
    }
}
