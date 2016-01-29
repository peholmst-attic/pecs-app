package net.pkhsolutions.pecsapp.control;

import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.entity.PictureDescriptor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.MimeTypeUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Integration test for {@link PictureFileStorage}.@
 */
public class PictureFileStorageTest {

    PictureFileStorage pictureFileStorage;
    Path tempDirectory;
    BufferedImage exampleImage;

    @Before
    public void setUp() throws Exception {
        PictureFileStorageProperties properties = new PictureFileStorageProperties();
        tempDirectory = Files.createTempDirectory(getClass().getSimpleName());
        properties.setDirectory(tempDirectory.toString());
        pictureFileStorage = new PictureFileStorage(properties);
        exampleImage = ImageIO.read(getClass().getResourceAsStream("/test.jpg"));
    }

    @Test
    public void storeAndLoad_imageDoesNotExistYet_imageFileIsCreatedAndLoaded() throws Exception {
        PictureDescriptor descriptor = new PictureDescriptor(1L, "test", MimeTypeUtils.IMAGE_JPEG);
        Path destinationPath = tempDirectory.resolve(PageLayout.A4_LANDSCAPE_4_BY_3.name().toLowerCase()).resolve("1.jpeg");

        assertFalse(Files.exists(destinationPath));
        pictureFileStorage.store(descriptor, Optional.of(PageLayout.A4_LANDSCAPE_4_BY_3), exampleImage);
        assertTrue(Files.exists(destinationPath));

        Optional<BufferedImage> loadedImage = pictureFileStorage.load(descriptor, Optional.of(PageLayout.A4_LANDSCAPE_4_BY_3));
        assertTrue(loadedImage.isPresent());
        assertEquals(exampleImage.getWidth(), loadedImage.get().getWidth());
        assertEquals(exampleImage.getHeight(), loadedImage.get().getHeight());
    }

    @Test
    public void load_imageDoesNotExist_emptyOptionalReturned() throws Exception {
        PictureDescriptor descriptor = new PictureDescriptor(1L, "test", MimeTypeUtils.IMAGE_JPEG);
        Optional<BufferedImage> loadedImage = pictureFileStorage.load(descriptor, Optional.of(PageLayout.A4_PORTRAIT_2_BY_2));
        assertFalse(loadedImage.isPresent());
    }
}
