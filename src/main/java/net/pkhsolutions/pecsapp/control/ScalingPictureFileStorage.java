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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

/**
 * TODO Document me
 */
@Service
public class ScalingPictureFileStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScalingPictureFileStorage.class);

    private final PictureTransformer pictureTransformer;
    private final PictureFileStorage pictureFileStorage;

    @Autowired
    ScalingPictureFileStorage(@NotNull PictureTransformer pictureTransformer, @NotNull PictureFileStorage pictureFileStorage) {
        this.pictureTransformer = pictureTransformer;
        this.pictureFileStorage = pictureFileStorage;
    }

    public void storeForAllLayouts(@NotNull PictureDescriptor descriptor, @NotNull BufferedImage original) throws IOException {
        for (PageLayout layout : PageLayout.values()) {
            LOGGER.debug("Storing image for descriptor {} and layout {}", descriptor, layout);
            pictureFileStorage.store(descriptor, Optional.of(layout), pictureTransformer.scaleIfNecessary(original, layout));
        }
        // Finally, also store the raw image
        LOGGER.debug("Storing raw image for descriptor {}", descriptor);
        pictureFileStorage.store(descriptor, Optional.empty(), original);
    }

    @NotNull
    public Optional<BufferedImage> loadForLayout(@NotNull PictureDescriptor descriptor, @NotNull PageLayout layout) throws IOException {
        @NotNull Optional<BufferedImage> image = pictureFileStorage.load(descriptor, Optional.of(layout));
        if (!image.isPresent()) {
            LOGGER.debug("Could not find image for descriptor {} and layout {}, trying to find raw image", descriptor, layout);
            // Try with the raw image
            image = pictureFileStorage.load(descriptor, Optional.empty());
            if (image.isPresent()) {
                LOGGER.debug("Found raw image, scaling it to layout {} and storing it", layout);
                // We don't have a size for this layout, so let's create one
                image = image.map(img -> pictureTransformer.scaleIfNecessary(img, layout));
                pictureFileStorage.store(descriptor, Optional.of(layout), image.get());
            }
        }
        return image;
    }
}
