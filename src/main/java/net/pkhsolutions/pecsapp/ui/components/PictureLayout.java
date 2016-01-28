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
package net.pkhsolutions.pecsapp.ui.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.*;
import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.model.Picture;
import net.pkhsolutions.pecsapp.model.PictureModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.MimeType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO document me
 */
public class PictureLayout extends VerticalLayout {

    private final PictureModel model;
    private MimeType mimeType;
    private ByteArrayOutputStream uploadStream;

    private Upload upload;
    private Image preview;
    private TextField title;
    private ProgressBar progressBar;
    private String fileName;
    private String name;
    private Label noImage;
    private Picture picture;

    public PictureLayout(@NotNull PictureModel model) {
        this.model = model;
        //addLayoutClickListener(this::clickLayout);

        noImage = new Label("Klicka för att ladda upp en bild");
        noImage.addStyleName("preview");
        noImage.addStyleName("no-image");
        //noImage.setWidth(pageLayout.getPictureWidthMm(), Unit.MM);
        //noImage.setHeight(pageLayout.getPictureHeightMm(), Unit.MM);
        addComponent(noImage);
        setExpandRatio(noImage, 1f);
        setComponentAlignment(noImage, Alignment.MIDDLE_CENTER);

        /*preview = new Image();
        preview.addStyleName("preview");
        preview.setWidth(pageLayout.getPictureWidthMm(), Unit.MM);
        preview.setHeight(pageLayout.getPictureHeightMm(), Unit.MM);
        preview.setVisible(false);
        addComponent(preview);
        setExpandRatio(preview, 1f);
        setComponentAlignment(preview, Alignment.MIDDLE_CENTER);

        upload = new Upload();
        upload.setVisible(true);
        upload.setButtonCaption(null);
        upload.setImmediate(true);
        upload.setReceiver(this::receiveUpload);
        upload.addStartedListener(this::uploadStarted);
        upload.addFinishedListener(this::uploadFinished);
        addComponent(upload);

        progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        addComponent(progressBar);
        progressBar.setVisible(false);     */

        title = new TextField();
        title.setInputPrompt("Skriv namnet här");
        title.setWidth("100%");
        title.setPropertyDataSource(model.getTitle());
        addComponent(title);
        setComponentAlignment(title, Alignment.BOTTOM_LEFT);
    }

    private void clickLayout(LayoutEvents.LayoutClickEvent event) {
        /*if (event.getClickedComponent() == preview || event.getClickedComponent() == noImage) {
            upload.submitUpload();
        } */
    }

    private OutputStream receiveUpload(String fileName, String mimeType) {
        // TODO Validate mime type
        uploadStream = new ByteArrayOutputStream();
        this.fileName = fileName;
        this.mimeType = MimeType.valueOf(mimeType);
        return uploadStream;
    }

    private void uploadStarted(Upload.StartedEvent event) {
        progressBar.setVisible(true);
        getUI().setPollInterval(200);
    }

    private void uploadFinished(Upload.FinishedEvent event) {
        progressBar.setVisible(false);
        getUI().setPollInterval(0);
        if (event instanceof Upload.SucceededEvent) {
            try {
                picture = new Picture(new ByteArrayInputStream(uploadStream.toByteArray()), mimeType);
                preview.setSource(picture.toResource(150, 200));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            preview.setVisible(true);
            noImage.setVisible(false);
        }
    }

}
