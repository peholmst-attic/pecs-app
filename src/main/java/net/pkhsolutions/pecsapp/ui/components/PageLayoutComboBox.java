package net.pkhsolutions.pecsapp.ui.components;

import com.vaadin.ui.ComboBox;
import net.pkhsolutions.pecsapp.entity.PageLayout;

import java.util.Arrays;

public class PageLayoutComboBox extends ComboBox {

    public PageLayoutComboBox() {
        this(null);
    }

    public PageLayoutComboBox(String caption) {
        super(caption, Arrays.asList(PageLayout.values()));
    }

    @Override
    public PageLayout getValue() {
        return (PageLayout) super.getValue();
    }
}
