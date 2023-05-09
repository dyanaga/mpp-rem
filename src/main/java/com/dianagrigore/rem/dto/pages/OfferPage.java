package com.dianagrigore.rem.dto.pages;

import com.dianagrigore.rem.dto.OfferDto;
import com.dianagrigore.rem.model.Offer;
import com.dianagrigore.rem.web.paging.PageResult;
import org.springframework.data.domain.Page;

import java.util.List;

public class OfferPage extends PageResult<OfferDto> {

    public static final OfferPage EMPTY = new OfferPage(Page.empty());

    public OfferPage(Page<OfferDto> page) {
        super(page);
    }

    public OfferPage(Page<Offer> page, List<OfferDto> content) {
        super(page, content);
    }
}
