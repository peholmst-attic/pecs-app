package net.pkhsolutions.pecsapp.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.pecsapp.ui.components.PageLayoutComboBox;
import net.pkhsolutions.pecsapp.ui.components.PagePanel;
import net.pkhsolutions.pecsapp.ui.themes.PecsAppTheme;

@SpringUI
@Theme(PecsAppTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setSpacing(true);
        layout.addComponent(toolbar);

        PageLayoutComboBox pageLayout = new PageLayoutComboBox("Layout");
        toolbar.addComponent(pageLayout);

        PagePanel pagePanel = new PagePanel();
        layout.addComponent(pagePanel);
        layout.setExpandRatio(pagePanel, 1);

        pageLayout.setValue(pagePanel.getLayout());
        pageLayout.addValueChangeListener((evt) -> pagePanel.setLayout(pageLayout.getValue()));
    }

}
