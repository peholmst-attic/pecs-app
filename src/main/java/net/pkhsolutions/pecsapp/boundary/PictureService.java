package net.pkhsolutions.pecsapp.boundary;

import com.vaadin.server.Resource;
import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.entity.PictureDescriptor;
import org.springframework.util.MimeType;

import java.io.InputStream;

public interface PictureService {

    PictureDescriptor uploadPicture(InputStream rawData, MimeType mimeType);

    Resource downloadPictureForLayout(PictureDescriptor pictureDescriptor, PageLayout layout);

    PictureDescriptor updateDescriptor(PictureDescriptor pictureDescriptor);
}
