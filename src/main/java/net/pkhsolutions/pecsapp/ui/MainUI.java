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

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.pecsapp.ui.theme.PecsAppTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO Document me
 */
@SpringUI
@Theme(PecsAppTheme.THEME_NAME)
public class MainUI extends UI {

    @Autowired
    SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);

        Navigator navigator = new Navigator(this, layout);
        navigator.addProvider(viewProvider);
    }

}
