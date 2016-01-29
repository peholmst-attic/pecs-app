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
import java.util.Optional;

/**
 * TODO document me
 */
public class PictureModel implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureModel.class);

    private final PictureService pictureService;

    private PictureDescriptor descriptor;

    private final PageModel pageModel;

    private final ObjectProperty<String> title = new ObjectProperty<>("", String.class);

    private final ObjectProperty<Resource> image = new ObjectProperty<>(null, Resource.class);

    public PictureModel(@NotNull PageModel pageModel, @NotNull PictureService pictureService) {
        this.pageModel = Objects.requireNonNull(pageModel);
        this.pictureService = Objects.requireNonNull(pictureService);
        this.pageModel.getLayout().addValueChangeListener(event -> updateProperties());
        this.title.addValueChangeListener(event -> titleChanged());
    }

    @NotNull
    public PageModel getPageModel() {
        return pageModel;
    }

    @NotNull
    public ObjectProperty<String> getTitle() {
        return title;
    }

    @NotNull
    public ObjectProperty<Resource> getImage() {
        return image;
    }

    public boolean isSupportedType(@NotNull MimeType mimeType) {
        LOGGER.debug("Checking if {} is a supported type", mimeType);
        return true; // TODO Implement me!
    }

    public void upload(@NotNull InputStream rawData, @NotNull MimeType mimeType) {
        LOGGER.debug("Uploading image of type {}", mimeType);
        setDescriptor(Optional.of(pictureService.uploadPicture(rawData, mimeType)));
    }

    public void setDescriptor(@NotNull Optional<PictureDescriptor> descriptor) {
        LOGGER.debug("Setting descriptor {}", descriptor);
        this.descriptor = Objects.requireNonNull(descriptor).orElse(null);
        updateProperties();
    }

    private void updateProperties() {
        if (descriptor == null) {
            title.setValue("");
            image.setValue(null);
        } else {
            title.setValue(descriptor.getTitle());
            image.setValue(pictureService.downloadPictureForLayout(descriptor, pageModel.getLayout().getValue()).map(this::toResource).orElse(null));
        }
    }

    private void titleChanged() {
        if (descriptor != null && !descriptor.getTitle().equals(title.getValue())) {
            descriptor.setTitle(title.getValue());
            descriptor = pictureService.updateDescriptor(descriptor);
        }
    }

    private Resource toResource(InputStream inputStream) {
        return new StreamResource((StreamResource.StreamSource) () -> inputStream, descriptor.getFileName());
    }
}
