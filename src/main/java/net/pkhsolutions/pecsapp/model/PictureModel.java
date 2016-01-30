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
package net.pkhsolutions.pecsapp.model;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import net.pkhsolutions.pecsapp.boundary.PictureService;
import net.pkhsolutions.pecsapp.entity.PictureDescriptor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeType;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * TODO document me
 */
public class PictureModel implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureModel.class);

    private final PictureService pictureService;

    private final PageModel pageModel;

    private final ObjectProperty<String> title = new ObjectProperty<>("", String.class);

    private final ObjectProperty<Resource> image = new ObjectProperty<>(null, Resource.class);

    private final ObjectProperty<PictureDescriptor> descriptor = new ObjectProperty<>(null, PictureDescriptor.class);

    public PictureModel(@NotNull PageModel pageModel, @NotNull PictureService pictureService) {
        this.pageModel = Objects.requireNonNull(pageModel);
        this.pictureService = Objects.requireNonNull(pictureService);
        // PictureModel always has same scope as PageModel, no need to remove the listener
        this.pageModel.getLayout().addValueChangeListener(event -> updateProperties());
        this.title.addValueChangeListener(this::titleChanged);
        this.descriptor.addValueChangeListener(this::descriptorChanged);
    }

    @NotNull
    public ObjectProperty<String> getTitle() {
        return title;
    }

    @NotNull
    public ObjectProperty<Resource> getImage() {
        return image;
    }

    @NotNull
    public ObjectProperty<PictureDescriptor> getDescriptor() {
        return descriptor;
    }

    /**
     * @param mimeType
     * @return
     */
    public boolean isSupportedType(@NotNull MimeType mimeType) {
        LOGGER.debug("Checking if {} is a supported type", mimeType);
        return pictureService.isSupportedMimeType(mimeType);
    }

    /**
     * @param rawData
     * @param mimeType
     */
    public void upload(@NotNull InputStream rawData, @NotNull MimeType mimeType) {
        LOGGER.debug("Uploading image of type {}", mimeType);
        descriptor.setValue(pictureService.uploadPicture(rawData, mimeType));
    }

    /**
     * @param source
     */
    public void drop(@NotNull PictureModel source) {
        final PictureDescriptor thisDescriptor = descriptor.getValue();
        final PictureDescriptor sourceDescriptor = source.descriptor.getValue();
        if (sourceDescriptor != null) {
            descriptor.setValue(sourceDescriptor);
            source.descriptor.setValue(thisDescriptor);
        }
    }

    private void updateProperties() {
        final PictureDescriptor thisDescriptor = descriptor.getValue();
        if (thisDescriptor == null) {
            title.setValue("");
            image.setValue(null);
        } else {
            title.setValue(thisDescriptor.getTitle());
            image.setValue(pictureService.downloadPictureForLayout(thisDescriptor,
                    pageModel.getLayout().getValue()).map(stream -> toResource(stream, thisDescriptor.getFileName()))
                    .orElse(null));
        }
    }

    private void titleChanged(Property.ValueChangeEvent event) {
        final PictureDescriptor thisDescriptor = descriptor.getValue();
        if (thisDescriptor != null && !thisDescriptor.getTitle().equals(title.getValue())) {
            thisDescriptor.setTitle(title.getValue());
            descriptor.setValue(pictureService.save(thisDescriptor));
        }
    }

    private void descriptorChanged(Property.ValueChangeEvent event) {
        updateProperties();
    }

    private Resource toResource(InputStream inputStream, String fileName) {
        return new StreamResource((StreamResource.StreamSource) () -> inputStream, fileName);
    }
}
