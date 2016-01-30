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
package net.pkhsolutions.pecsapp.boundary;

import net.pkhsolutions.pecsapp.control.PageRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * TODO Document me
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class PageServiceBean implements PageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageServiceBean.class);

    private final PageRepository pageRepository;

    @Autowired
    PageServiceBean(@NotNull PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Override
    @NotNull
    public Optional<Page> findById(@NotNull Long id) {
        LOGGER.debug("Looking for a page with ID {}", id);
        return Optional.ofNullable(pageRepository.findOne(id));
    }

    @Override
    @NotNull
    public Page save(@NotNull Page page) {
        LOGGER.debug("Saving page {}", page);
        return pageRepository.saveAndFlush(page);
    }
}
