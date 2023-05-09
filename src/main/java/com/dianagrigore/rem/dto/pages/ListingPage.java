package com.dianagrigore.rem.dto.pages;

import com.dianagrigore.rem.dto.ListingDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.web.paging.PageResult;
import org.springframework.data.domain.Page;

import java.util.List;

public class ListingPage extends PageResult<ListingDto> {

    public static final ListingPage EMPTY = new ListingPage(Page.empty());

    public ListingPage(Page<ListingDto> page) {
        super(page);
    }

    public ListingPage(Page<Listing> page, List<ListingDto> content) {
        super(page, content);
    }
}
