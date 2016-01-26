package net.pkhsolutions.pecsapp.entity;

import net.pkhsolutions.pecsapp.model.Picture;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.MimeType;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class PictureDescriptor extends AbstractPersistable<Long> {

    private String title;

    private String mimeType;

    PictureDescriptor() {
    }

    public PictureDescriptor(String title, MimeType mimeType) {
        setTitle(title);
        setMimeType(mimeType);
    }

    public PictureDescriptor(Long id, String title, MimeType mimeType) {
        this(title, mimeType);
        setId(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    public MimeType getMimeType() {
        return MimeType.valueOf(mimeType);
    }

    public void setMimeType(@NotNull  MimeType mimeType) {
        this.mimeType = Objects.requireNonNull(mimeType).toString();
    }
}
