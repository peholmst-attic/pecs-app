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

import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.entity.PictureDescriptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.MimeType;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * TODO Document me
 */
public interface PictureService {

    @NotNull PictureDescriptor uploadPicture(@NotNull InputStream rawData, @NotNull MimeType mimeType);

    @NotNull Optional<InputStream> downloadPictureForLayout(@NotNull PictureDescriptor pictureDescriptor, @NotNull PageLayout layout);

    @NotNull PictureDescriptor updateDescriptor(@NotNull PictureDescriptor pictureDescriptor);

    class UploadPictureException extends RuntimeException {
        public UploadPictureException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    class DownloadPictureException extends RuntimeException {
        public DownloadPictureException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
