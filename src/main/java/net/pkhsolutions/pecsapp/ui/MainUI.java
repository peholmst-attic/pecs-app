package net.pkhsolutions.pecsapp.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.pecsapp.boundary.PictureService;
import net.pkhsolutions.pecsapp.model.PageModel;
import net.pkhsolutions.pecsapp.ui.components.PageLayoutComboBox;
import net.pkhsolutions.pecsapp.ui.components.PagePanel;
import net.pkhsolutions.pecsapp.ui.themes.PecsAppTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme(PecsAppTheme.THEME_NAME)
public class MainUI extends UI {

    @Autowired
    PictureService pictureService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setSpacing(true);
        layout.addComponent(toolbar);

        PageModel pageModel = new PageModel(pictureService);

        PageLayoutComboBox pageLayout = new PageLayoutComboBox("Layout");
        pageLayout.setPropertyDataSource(pageModel.getLayout());
        toolbar.addComponent(pageLayout);

        PagePanel pagePanel = new PagePanel(pageModel);
        layout.addComponent(pagePanel);
        layout.setExpandRatio(pagePanel, 1);
    }

}
