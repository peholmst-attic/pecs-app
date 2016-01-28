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
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * TODO document me
 */
public class PictureModel implements Serializable {

    private final PageModel pageModel;

    private final ObjectProperty<String> title = new ObjectProperty<>("", String.class);

    private final ObjectProperty<Resource> image = new ObjectProperty<>(null, Resource.class);

    public PictureModel(@NotNull PageModel pageModel) {
        this.pageModel = Objects.requireNonNull(pageModel);
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
}
