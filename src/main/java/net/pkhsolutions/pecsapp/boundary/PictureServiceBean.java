package net.pkhsolutions.pecsapp.boundary;

import com.vaadin.server.Resource;
import net.pkhsolutions.pecsapp.entity.Library;
import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.entity.PictureDescriptor;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.io.InputStream;
import java.util.List;

@Service
class PictureServiceBean implements PictureService {

    @Override
    public PictureDescriptor uploadPicture(InputStream rawData, MimeType mimeType) {
        return null;
    }

    @Override
    public Resource downloadPictureForLayout(PictureDescriptor pictureDescriptor, PageLayout layout) {
        return null;
    }

    @Override
    public PictureDescriptor updateDescriptor(PictureDescriptor pictureDescriptor) {
        return null;
    }
}
