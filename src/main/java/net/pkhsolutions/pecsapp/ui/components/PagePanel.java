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
package net.pkhsolutions.pecsapp.ui.components;

import com.vaadin.data.Property;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.entity.PageOrientation;
import net.pkhsolutions.pecsapp.model.PageModel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * TODO document me
 */
public class PagePanel extends Panel {

    private final PageModel model;
    private CssLayout page;
    private CssLayout[] rows;
    private PictureLayout[][] cells;

    public PagePanel(@NotNull PageModel model) {
        this.model = Objects.requireNonNull(model);
        addStyleName("page-panel");
        setSizeFull();
        page = new CssLayout();
        page.addStyleName("page");
        setContent(page);
        model.getLayout().addValueChangeListener(this::updateLayout);
        updateLayout(null);
    }

    @NotNull
    public PageModel getModel() {
        return model;
    }

    private void updateLayout(Property.ValueChangeEvent event) {
        final PageLayout layout = Objects.requireNonNull(model.getLayout().getValue());
        if (layout.getOrientation().equals(PageOrientation.PORTRAIT)) {
            page.setWidth(layout.getPageSize().getWidthMm(), Unit.MM);
            page.setHeight(layout.getPageSize().getHeightMm(), Unit.MM);
        } else {
            page.setWidth(layout.getPageSize().getHeightMm(), Unit.MM);
            page.setHeight(layout.getPageSize().getWidthMm(), Unit.MM);
        }
        page.removeAllComponents();
        final float rowHeight = page.getHeight() / layout.getRows();
        final float cellWidth = page.getWidth() / layout.getColumns();
        rows = new CssLayout[layout.getRows()];
        cells = new PictureLayout[layout.getRows()][layout.getColumns()];
        for (int i = 0; i < layout.getRows(); ++i) {
            CssLayout row = new CssLayout();
            row.addStyleName("page-row");
            row.setWidth("100%");
            row.setHeight(rowHeight, page.getHeightUnits());
            page.addComponent(row);
            rows[i] = row;
            for (int j = 0; j < layout.getColumns(); ++j) {
                PictureLayout cell = new PictureLayout(model.getPictureModel(j, i));
                cell.addStyleName("page-row-cell");
                cell.setWidth(cellWidth, page.getWidthUnits());
                cell.setHeight("100%");
                row.addComponent(cell);
                cells[i][j] = cell;
            }
        }
    }
}
