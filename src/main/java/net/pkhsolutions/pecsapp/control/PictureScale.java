package net.pkhsolutions.pecsapp.control;

import net.pkhsolutions.pecsapp.entity.PageLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 */
@Service
public class PictureScale {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureScale.class);

    PictureScale() {
    }

    /**
     * @param image
     * @param pageLayout
     * @return
     */
    public BufferedImage scaleIfNecessary(BufferedImage image, PageLayout pageLayout) {
        return scaleIfNecessary(image, convertMmToPx(pageLayout.getPictureHeightMm()),
                convertMmToPx(pageLayout.getPictureWidthMm()));
    }

    /**
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
     * @param mm
     * @return
     */
    public int convertMmToPx(int mm) {
        return (int) (3.779527559 * mm);
    }
}
