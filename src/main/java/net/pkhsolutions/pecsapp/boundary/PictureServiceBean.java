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
package net.pkhsolutions.pecsapp.boundary;

import net.pkhsolutions.pecsapp.control.PictureDescriptorRepository;
import net.pkhsolutions.pecsapp.control.ScalingPictureFileStorage;
import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.entity.PictureDescriptor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * TODO Document me
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class PictureServiceBean implements PictureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureServiceBean.class);

    private final PictureDescriptorRepository pictureDescriptorRepository;
    private final ScalingPictureFileStorage scalingPictureFileStorage;

    @Autowired
    PictureServiceBean(@NotNull PictureDescriptorRepository pictureDescriptorRepository, @NotNull ScalingPictureFileStorage scalingPictureFileStorage) {
        this.pictureDescriptorRepository = pictureDescriptorRepository;
        this.scalingPictureFileStorage = scalingPictureFileStorage;
    }

    @NotNull
    @Override
    public PictureDescriptor uploadPicture(@NotNull InputStream rawData, @NotNull MimeType mimeType) {
        LOGGER.debug("Uploading picture of type {}", mimeType);
        try {
            PictureDescriptor descriptor = pictureDescriptorRepository.saveAndFlush(new PictureDescriptor("", mimeType));
            BufferedImage image = ImageIO.read(rawData);
            scalingPictureFileStorage.storeForAllLayouts(descriptor, image);
            return descriptor;
        } catch (IOException ex) {
            LOGGER.error("Error uploading picture", ex);
            throw new UploadPictureException("Could not upload picture", ex);
        }
    }

    @Override
    public @NotNull Optional<InputStream> downloadPictureForLayout(@NotNull PictureDescriptor descriptor, @NotNull PageLayout layout) {
        LOGGER.debug("Downloading picture for descriptor {} and layout {}", descriptor, layout);
        try {
            Optional<BufferedImage> image = scalingPictureFileStorage.loadForLayout(descriptor, layout);
            if (image.isPresent()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image.get(), descriptor.getMimeType().getSubtype(), baos);
                return Optional.of(new ByteArrayInputStream(baos.toByteArray()));
            } else {
                return Optional.empty();
            }
        } catch (IOException ex) {
            LOGGER.error("Error downloading picture", ex);
            throw new DownloadPictureException("Could not download picture", ex);
        }
    }

    @NotNull
    @Override
    public PictureDescriptor updateDescriptor(@NotNull PictureDescriptor pictureDescriptor) {
        LOGGER.debug("Updating descriptor {}", pictureDescriptor);
        return pictureDescriptorRepository.saveAndFlush(pictureDescriptor);
    }
}
