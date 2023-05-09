package com.dianagrigore.rem.dto.pages;

import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.web.paging.PageResult;
import org.springframework.data.domain.Page;

import java.util.List;

public class UserPage extends PageResult<UserDto> {

    public static final UserPage EMPTY = new UserPage(Page.empty());

    public UserPage(Page<UserDto> page) {
        super(page);
    }

    public UserPage(Page<User> page, List<UserDto> content) {
        super(page, content);
    }
}
