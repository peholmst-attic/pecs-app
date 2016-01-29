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
package net.pkhsolutions.pecsapp.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.MimeType;

import javax.persistence.Entity;

/**
 * TODO document me
 */
@Entity
public class PictureDescriptor extends AbstractPersistable<Long> {

    private String title;

    private String mimeType;

    PictureDescriptor() {
    }

    public PictureDescriptor(@NotNull MimeType mimeType) {
        this("", mimeType);
    }

    public PictureDescriptor(@NotNull String title, @NotNull MimeType mimeType) {
        setTitle(title);
        setMimeType(mimeType);
    }

    public PictureDescriptor(@Nullable Long id, @NotNull String title, @NotNull MimeType mimeType) {
        this(title, mimeType);
        setId(id);
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @NotNull
    public MimeType getMimeType() {
        return MimeType.valueOf(mimeType);
    }

    public void setMimeType(@NotNull MimeType mimeType) {
        this.mimeType = mimeType.toString();
    }

    @NotNull
    public String getFileName() {
        return String.format("%d_%s.%s", getId(), getTitle(), getMimeType().getSubtype());
    }
}
