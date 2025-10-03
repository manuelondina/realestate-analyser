package com.springter.realestate.analyser.domain.common;

import lombok.Builder;
import lombok.Value;

/**
 * Domain-specific pagination request object.
 * Independent of any framework-specific pagination classes.
 */
@Value
@Builder
public class PageRequest {
    
    /**
     * The page number (0-based)
     */
    @Builder.Default
    int page = 0;
    
    /**
     * The number of elements per page
     */
    @Builder.Default
    int size = 20;
    
    /**
     * Gets the offset for database queries
     */
    public long getOffset() {
        return (long) page * size;
    }
    
    /**
     * Creates a page request with default values
     */
    public static PageRequest defaultRequest() {
        return builder().build();
    }
    
    /**
     * Creates a page request with specified page and size
     */
    public static PageRequest of(int page, int size) {
        return builder()
                .page(Math.max(0, page))
                .size(Math.max(1, size))
                .build();
    }
}