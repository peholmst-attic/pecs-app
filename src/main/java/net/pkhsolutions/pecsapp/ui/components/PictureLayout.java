package net.pkhsolutions.pecsapp.ui.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.*;
import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.model.Picture;
import org.springframework.util.MimeType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class PictureLayout extends VerticalLayout {

    private final PageLayout pageLayout;

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

    PictureLayout(PageLayout pageLayout) {
        this.pageLayout = pageLayout;
        //addLayoutClickListener(this::clickLayout);

        noImage = new Label("Klicka för att ladda upp en bild");
        noImage.addStyleName("preview");
        noImage.addStyleName("no-image");
        noImage.setWidth(pageLayout.getPictureWidthMm(), Unit.MM);
        noImage.setHeight(pageLayout.getPictureHeightMm(), Unit.MM);
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
