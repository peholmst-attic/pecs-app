package net.pkhsolutions.pecsapp.model;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Picture implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Picture.class);

    private BufferedImage image;
    private MimeType mimeType;
    private String title = "picture";

    public Picture(InputStream rawData, MimeType mimeType) throws IOException {
        //checkThatMimeTypeIsSupported(mimeType);
        image = ImageIO.read(rawData);
        this.mimeType = mimeType;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void rotateLeft() {

    }

    public void rotateRight() {

    }

    public BufferedImage scaleIfNecessary(int maxHeightInPx, int maxWidthInPx) {
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

    private Resource toResource(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, mimeType.getSubtype(), baos);
        return new StreamResource((StreamResource.StreamSource) () -> new ByteArrayInputStream(baos.toByteArray()), String.format("%s.%s", title, mimeType.getSubtype()));
    }

    public Resource toResource(int maxHeightInPx, int maxWidthInPx) throws IOException {
        return toResource(scaleIfNecessary(maxHeightInPx, maxWidthInPx));
    }

    public static boolean isMimeTypeSupported(MimeType mimeType) {
        return MimeTypeUtils.IMAGE_JPEG.equals(mimeType) || MimeTypeUtils.IMAGE_PNG.equals(mimeType);
    }
}
