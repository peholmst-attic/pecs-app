package net.pkhsolutions.pecsapp.entity;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class PagePicture implements Serializable {

    private int col;

    private int row;

    @ManyToOne(optional = false)
    private PictureDescriptor picture;

    public PagePicture() {
    }

    public PagePicture(int col, int row, PictureDescriptor picture) {
        this.col = col;
        this.row = row;
        this.picture = picture;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public PictureDescriptor getPicture() {
        return picture;
    }

    public void setPicture(PictureDescriptor picture) {
        this.picture = picture;
    }
}
