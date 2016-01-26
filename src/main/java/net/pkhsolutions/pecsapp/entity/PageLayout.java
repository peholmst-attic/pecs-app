package net.pkhsolutions.pecsapp.entity;

public enum PageLayout {

    A4_PORTRAIT_3_BY_4(PageOrientation.PORTRAIT, PageSize.A4, 3, 4, 60, 45),
    A4_LANDSCAPE_4_BY_3(PageOrientation.LANDSCAPE, PageSize.A4, 4, 3, 60, 45),
    A4_PORTRAIT_2_BY_2(PageOrientation.PORTRAIT, PageSize.A4, 2, 2, 60, 45);

    private PageOrientation orientation;
    private PageSize pageSize;
    private int columns;
    private int rows;
    private int pictureWidthMm;
    private int pictureHeightMm;

    PageLayout(PageOrientation orientation, PageSize pageSize, int columns, int rows, int pictureWidthMm, int pictureHeightMm) {
        this.orientation = orientation;
        this.pageSize = pageSize;
        this.columns = columns;
        this.rows = rows;
        this.pictureWidthMm = pictureWidthMm;
        this.pictureHeightMm = pictureHeightMm;
    }

    public PageOrientation getOrientation() {
        return orientation;
    }

    public PageSize getPageSize() {
        return pageSize;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getPictureWidthMm() {
        return pictureWidthMm;
    }

    public int getPictureHeightMm() {
        return pictureHeightMm;
    }
}
