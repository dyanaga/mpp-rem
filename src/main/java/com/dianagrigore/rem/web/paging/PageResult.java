package com.dianagrigore.rem.web.paging;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

/**
 * Custom conversion of the default Page from spring data. Used as responses for the services.
 *
 * @param <T> The type of the page
 */
public class PageResult<T> {
    private final int page;
    private final int pageSize;
    private final int pageResultSize;
    private final int pages;
    private final long totalElements;
    private final boolean hasMoreContent;
    private final List<T> content;

    /**
     * Converts the default page into a less memory heavy object with the most useful information
     *
     * @param page the returned value from the JPA findAll
     */
    public PageResult(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.pageSize = page.getSize();
        this.pageResultSize = page.getNumberOfElements();
        this.pages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.hasMoreContent = (this.page < (this.pages - 1));
    }

    /**
     * Converts the default page and the given content into a less memory heavy object with the most useful information
     *
     * @param page the returned value from the JPA findAll
     * @param content the content that should be used instead of the page result
     */
    public PageResult(Page<?> page, List<T> content) {
        this.content = content;
        this.page = page.getNumber();
        this.pageSize = page.getSize();
        this.pageResultSize = page.getNumberOfElements();
        this.pages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.hasMoreContent = (this.page < (this.pages - 1));
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageResultSize() {
        return pageResultSize;
    }

    public int getPages() {
        return pages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public boolean hasMoreContent() {
        return hasMoreContent;
    }

    public boolean isEmpty() {
        return Objects.isNull(content) || content.isEmpty();
    }
}
