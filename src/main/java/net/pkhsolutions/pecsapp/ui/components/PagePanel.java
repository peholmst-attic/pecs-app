package net.pkhsolutions.pecsapp.ui.components;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import net.pkhsolutions.pecsapp.entity.PageLayout;
import net.pkhsolutions.pecsapp.entity.PageOrientation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PagePanel extends Panel {

    private PageLayout layout;
    private CssLayout page;
    private CssLayout[] rows;
    private PictureLayout[][] cells;

    public PagePanel() {
        addStyleName("page-panel");
        setSizeFull();
        page = new CssLayout();
        page.addStyleName("page");
        setContent(page);
        setLayout(PageLayout.A4_PORTRAIT_3_BY_4);
    }

    public void setLayout(@NotNull PageLayout layout) {
        if (Objects.requireNonNull(layout).equals(this.layout)) {
            return;
        }
        this.layout = layout;
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
                PictureLayout cell = new PictureLayout(layout);
                cell.addStyleName("page-row-cell");
                cell.setWidth(cellWidth, page.getWidthUnits());
                cell.setHeight("100%");
                row.addComponent(cell);
                cells[i][j] = cell;
            }
        }
    }

    @NotNull
    public PageLayout getLayout() {
        return layout;
    }
}
