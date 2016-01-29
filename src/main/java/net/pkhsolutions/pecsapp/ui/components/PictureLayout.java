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

import com.vaadin.data.Property;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.ServerSideCriterion;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.*;
import net.pkhsolutions.pecsapp.model.PictureModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.MimeType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * TODO document me
 */
public class PictureLayout extends VerticalLayout implements DropHandler {

    private final PictureModel model;
    private final AcceptCriterion acceptCriterion = new ServerSideCriterion() {
        @Override
        public boolean accept(DragAndDropEvent dragEvent) {
            return isUploadEvent(dragEvent);
        }
    };
    private Image image;
    private TextField title;
    private ProgressBar progressBar;
    private final Label infoLabel;
    private final VerticalLayout dropPane;

    public PictureLayout(@NotNull PictureModel model) {
        this.model = model;
        setSpacing(true);

        infoLabel = new Label("Drag och släpp en bild här");
        infoLabel.setSizeUndefined();

        dropPane = new VerticalLayout();
        dropPane.setSizeFull();
        dropPane.addComponent(infoLabel);
        dropPane.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);

        image = new Image();
        image.setSizeUndefined();
        dropPane.addComponent(image);
        dropPane.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

        progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        dropPane.addComponent(progressBar);
        dropPane.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);

        DragAndDropWrapper dragAndDropWrapper = new DragAndDropWrapper(dropPane);
        dragAndDropWrapper.setDropHandler(this);
        dragAndDropWrapper.setSizeFull();
        addComponent(dragAndDropWrapper);
        setExpandRatio(dragAndDropWrapper, 1f);

        title = new TextField();
        title.setInputPrompt("Skriv namnet här");
        title.setWidth("100%");
        title.setPropertyDataSource(model.getTitle());
        title.setImmediate(true);
        addComponent(title);
        setComponentAlignment(title, Alignment.BOTTOM_LEFT);

        model.getImage().addValueChangeListener(this::imageChanged);
        imageChanged(null);
    }

    private void imageChanged(Property.ValueChangeEvent event) {
        Resource resource = model.getImage().getValue();
        image.setSource(resource);
        image.setVisible(resource != null);
        title.setVisible(image.isVisible());
        infoLabel.setVisible(resource == null);
        if (infoLabel.isVisible()) {
            dropPane.addStyleName("drop-box");
        } else {
            dropPane.removeStyleName("drop-box");
        }
    }

    @Override
    public void drop(DragAndDropEvent event) {
        extractFile(event).filter(this::isSupportedFile).ifPresent(this::uploadFile);
    }

    private boolean isUploadEvent(DragAndDropEvent event) {
        return extractFile(event).map(this::isSupportedFile).orElse(false);
    }

    private Optional<Html5File> extractFile(DragAndDropEvent event) {
        if (event.getTransferable() instanceof DragAndDropWrapper.WrapperTransferable) {
            DragAndDropWrapper.WrapperTransferable tr = (DragAndDropWrapper.WrapperTransferable) event.getTransferable();
            if (tr.getFiles() != null && tr.getFiles().length == 1) {
                return Optional.of(tr.getFiles()[0]);
            }
        }
        return Optional.empty();
    }

    private boolean isSupportedFile(Html5File file) {
        return model.isSupportedType(MimeType.valueOf(file.getType())); // TODO also check max size
    }

    private void uploadFile(Html5File file) {
        final MimeType mimeType = MimeType.valueOf(file.getType());
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final StreamVariable streamVariable = new StreamVariable() {
            @Override
            public OutputStream getOutputStream() {
                return baos;
            }

            @Override
            public boolean listenProgress() {
                return false;
            }

            @Override
            public void onProgress(StreamingProgressEvent event) {
            }

            @Override
            public void streamingStarted(StreamingStartEvent event) {
            }

            @Override
            public void streamingFinished(StreamingEndEvent event) {
                hideProgressBar();
                model.upload(new ByteArrayInputStream(baos.toByteArray()), mimeType);
            }

            @Override
            public void streamingFailed(StreamingErrorEvent event) {
                hideProgressBar();
            }

            @Override
            public boolean isInterrupted() {
                return false;
            }
        };
        showProgressBar();
        file.setStreamVariable(streamVariable);
    }

    private void showProgressBar() {
        progressBar.setVisible(true);
        getUI().setPollInterval(200);
    }

    private void hideProgressBar() {
        progressBar.setVisible(false);
        getUI().setPollInterval(-1);
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();// acceptCriterion;
    }
}
