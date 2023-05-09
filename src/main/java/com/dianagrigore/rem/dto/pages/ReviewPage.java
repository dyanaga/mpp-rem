package com.dianagrigore.rem.dto.pages;

import com.dianagrigore.rem.dto.ReviewDto;
import com.dianagrigore.rem.model.Review;
import com.dianagrigore.rem.web.paging.PageResult;
import org.springframework.data.domain.Page;

import java.util.List;

public class ReviewPage extends PageResult<ReviewDto> {

    public static final ReviewPage EMPTY = new ReviewPage(Page.empty());

    public ReviewPage(Page<ReviewDto> page) {
        super(page);
    }

    public ReviewPage(Page<Review> page, List<ReviewDto> content) {
        super(page, content);
    }
}
