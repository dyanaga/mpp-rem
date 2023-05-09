package com.dianagrigore.rem.web.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Optional;

public final class PageUtils {

    /**
     * Returns a pageable but there is no default sort only default page and pageSize
     *
     * @param page     - desired page number, 0 default
     * @param pageSize - desired page size, 100 default
     * @param sort     - desired sort, unsorted default
     * @return - the desired pageable
     */
    public static Pageable getPageable(@Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort) {
        return getPageable(page, pageSize, sort, null);
    }

    /**
     * Returns a pageable but there is no default sort only default page and pageSize
     *
     * @param page        - desired page number, 0 default
     * @param pageSize    - desired page size, 100 default
     * @param sort        - desired sort, unsorted default
     * @param defaultSort - desired default sort, becomes the sort if the main sort field is null
     * @return - the desired pageable
     */
    public static Pageable getPageable(@Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort, @Nullable String defaultSort) {

        int selectedPage = Optional.ofNullable(page).orElse(0);
        int selectedPageSize = Optional.ofNullable(pageSize).orElse(100);

        Sort orders = Optional.ofNullable(sort)
                .map(PageUtils::getSort)
                .orElseGet(() ->
                        Optional.ofNullable(defaultSort)
                                .map(PageUtils::getSort)
                                .orElse(Sort.unsorted())
                );

        return PageRequest.of(selectedPage, selectedPageSize, orders);
    }

    /**
     * Based on the given sort string, it will create a JPA compatible Sort class
     *
     * @param sortString - a sort string ( i.e. "name", "+name"(asc name), "-name"(desc name), "+name,+email"(asc name, asc email) ), unsorted by default.
     * @return - the desired sort object
     */
    public static Sort getSort(@NonNull String sortString) {
        return Arrays.stream(sortString.split(","))
                .map(s -> {
                    Sort.Direction direction = s.contains("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
                    String withoutDirection = s.replaceAll("[-+]", "");
                    return Sort.by(direction, withoutDirection);
                })
                .reduce(Sort::and)
                .orElse(Sort.unsorted());
    }

}
