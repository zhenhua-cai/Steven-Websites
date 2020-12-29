package net.stevencai.blog.backend.response;

import lombok.Data;

@Data
public class ActionStatusResponse {
    private boolean status;

    public ActionStatusResponse() {
    }

    public ActionStatusResponse(boolean status) {
        this.status = status;
    }
}
