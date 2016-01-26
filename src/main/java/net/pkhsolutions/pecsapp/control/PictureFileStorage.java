package net.pkhsolutions.pecsapp.control;

import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.entity.PictureDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 *
 */
@Service
public class PictureFileStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureFileStorage.class);

    private Path directory;

    @Autowired
    PictureFileStorage(PictureFileStorageProperties properties) throws IOException {
        directory = Paths.get(properties.getDirectory());
        LOGGER.info("Storing picture files in {}", directory);
    }

    /**
     * @param descriptor
     * @param layout
     * @param image
     * @throws IOException
     */
    public void store(PictureDescriptor descriptor, PageLayout layout, BufferedImage image) throws IOException {
        final Path path = getDirectoryForLayout(layout)
                .resolve(String.format("%d.%s", descriptor.getId(), descriptor.getMimeType().getSubtype()));
        LOGGER.info("Storing image in {}", path);
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            ImageIO.write(image, descriptor.getMimeType().getSubtype(), outputStream);
        }
    }

    /**
     * @param descriptor
     * @param layout
     * @return
     * @throws IOException
     */
    public Optional<BufferedImage> load(PictureDescriptor descriptor, PageLayout layout) throws IOException {
        final Path path = getDirectoryForLayout(layout)
                .resolve(String.format("%d.%s", descriptor.getId(), descriptor.getMimeType().getSubtype()));
        LOGGER.info("Loading image from {}", path);
        if (!Files.exists(path)) {
            LOGGER.info("File {} does not exist", path);
            return Optional.empty();
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            return Optional.of(ImageIO.read(inputStream));
        }
    }

    private Path getDirectoryForLayout(PageLayout layout) throws IOException {
        final Path path = directory.resolve(layout.name().toLowerCase());
        try {
            return Files.createDirectories(path);
        } catch (FileAlreadyExistsException ex) {
            return path;
        }
    }

}
