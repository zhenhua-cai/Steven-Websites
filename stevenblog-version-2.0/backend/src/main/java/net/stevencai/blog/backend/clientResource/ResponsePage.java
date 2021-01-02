package net.stevencai.blog.backend.clientResource;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponsePage implements Serializable {

    private int size;
    private int number;
    private long totalElements;
    private int totalPages;
}
