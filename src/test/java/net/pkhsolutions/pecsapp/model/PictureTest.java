package net.pkhsolutions.pecsapp.model;

import org.junit.Test;
import org.springframework.util.MimeTypeUtils;

/**
 * Created by peholmst on 04-01-2016.
 */
public class PictureTest {

    @Test
    public void readJpg() throws Exception {
        Picture picture = new Picture(getClass().getResourceAsStream("test.jpg"), MimeTypeUtils.IMAGE_JPEG);
        picture.scaleIfNecessary(300, 400);
    }
}
