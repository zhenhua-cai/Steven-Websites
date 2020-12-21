package net.stevencai.blog.backend.clientResource;

import lombok.Data;

@Data
public class ResponsePage {

    private int size;
    private int number;
    private long totalElements;
    private int totalPages;
}
