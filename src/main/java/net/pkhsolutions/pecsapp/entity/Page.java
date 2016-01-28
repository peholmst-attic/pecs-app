package net.pkhsolutions.pecsapp.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Page extends AbstractPersistable<Long> {

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private PageLayout layout;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<PagePicture> pictures = new HashSet<>();

    protected Long getVersion() {
        return version;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    public PageLayout getLayout() {
        return layout;
    }

    public void setLayout(PageLayout layout) {
        this.layout = layout;
    }


}
