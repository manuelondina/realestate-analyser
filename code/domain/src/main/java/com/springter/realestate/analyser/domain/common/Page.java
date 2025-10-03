package com.springter.realestate.analyser.domain.common;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Domain-specific page response object.
 * Independent of any framework-specific pagination classes.
 */
@Value
@Builder
public class Page<T> {
    
    /**
     * The content of this page
     */
    List<T> content;
    
    /**
     * The current page number (0-based)
     */
    int page;
    
    /**
     * The number of elements requested per page
     */
    int size;
    
    /**
     * The total number of elements across all pages
     */
    long totalElements;
    
    /**
     * Gets the total number of pages
     */
    public int getTotalPages() {
        if (size == 0) {
            return totalElements > 0 ? 1 : 0;
        }
        return (int) Math.ceil((double) totalElements / size);
    }
    
    /**
     * Gets the number of elements in the current page
     */
    public int getNumberOfElements() {
        return content != null ? content.size() : 0;
    }
    
    /**
     * Checks if this is the first page
     */
    public boolean isFirst() {
        return page == 0;
    }
    
    /**
     * Checks if this is the last page
     */
    public boolean isLast() {
        return page >= getTotalPages() - 1;
    }
    
    /**
     * Checks if there's a next page
     */
    public boolean hasNext() {
        return !isLast();
    }
    
    /**
     * Checks if there's a previous page
     */
    public boolean hasPrevious() {
        return page > 0;
    }
    
    /**
     * Creates an empty page
     */
    public static <T> Page<T> empty(PageRequest pageRequest) {
        return Page.<T>builder()
                .content(List.of())
                .page(pageRequest.getPage())
                .size(pageRequest.getSize())
                .totalElements(0)
                .build();
    }
    
    /**
     * Creates a page with content
     */
    public static <T> Page<T> of(List<T> content, PageRequest pageRequest, long totalElements) {
        return Page.<T>builder()
                .content(content)
                .page(pageRequest.getPage())
                .size(pageRequest.getSize())
                .totalElements(totalElements)
                .build();
    }
}