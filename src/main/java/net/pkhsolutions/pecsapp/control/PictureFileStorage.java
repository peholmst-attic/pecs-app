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
 * Control class for storing and loading picture files from the file system.
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
     * TODO Document me
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
     * TODO Document me
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
