package net.pkhsolutions.pecsapp.entity;

public enum PageSize {

    A4(210, 297);

    private final int widthMm;
    private final int heightMm;

    PageSize(int widthMm, int heightMm) {
        this.widthMm = widthMm;
        this.heightMm = heightMm;
    }

    public int getWidthMm() {
        return widthMm;
    }

    public int getHeightMm() {
        return heightMm;
    }
}
