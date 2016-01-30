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
package net.pkhsolutions.pecsapp.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.pecsapp.boundary.PageService;
import net.pkhsolutions.pecsapp.boundary.PictureService;
import net.pkhsolutions.pecsapp.model.PageModel;
import net.pkhsolutions.pecsapp.ui.components.PageLayoutComboBox;
import net.pkhsolutions.pecsapp.ui.components.PagePanel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * TODO Document me
 */
@SpringView(name = PageView.VIEW_NAME)
public class PageView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "page";

    @Autowired
    PageService pageService;

    @Autowired
    PictureService pictureService;

    @PostConstruct
    void init() {
        setSizeFull();

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setSpacing(true);
        addComponent(toolbar);

        PageModel pageModel = new PageModel(pictureService, pageService);

        PageLayoutComboBox pageLayout = new PageLayoutComboBox("Layout");
        pageLayout.setPropertyDataSource(pageModel.getLayout());
        toolbar.addComponent(pageLayout);

        PagePanel pagePanel = new PagePanel(pageModel);
        addComponent(pagePanel);
        setExpandRatio(pagePanel, 1);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
