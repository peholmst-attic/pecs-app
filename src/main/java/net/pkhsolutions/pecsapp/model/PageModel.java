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
package net.pkhsolutions.pecsapp.model;

import com.vaadin.data.util.ObjectProperty;
import net.pkhsolutions.pecsapp.entity.PageLayout;
import org.javatuples.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO document me!
 */
public class PageModel implements Serializable {

    private final ObjectProperty<PageLayout> layout = new ObjectProperty<>(PageLayout.A4_PORTRAIT_3_BY_4, PageLayout.class);

    private final Map<Pair<Integer, Integer>, PictureModel> pictureModelMap = new HashMap<>();

    public ObjectProperty<PageLayout> getLayout() {
        return layout;
    }

    public PictureModel getPictureModel(int col, int row) {
        if (col < 0 || col > layout.getValue().getColumns()) {
            throw new IndexOutOfBoundsException("Invalid column index");
        }
        if (row < 0 || row > layout.getValue().getRows()) {
            throw new IndexOutOfBoundsException("Invalid row index");
        }

        Pair<Integer, Integer> coordinates = Pair.with(col, row);
        PictureModel model = pictureModelMap.get(coordinates);
        if (model == null) {
            model = new PictureModel(this);
            pictureModelMap.put(coordinates, model);
        }
        return model;
    }
}
