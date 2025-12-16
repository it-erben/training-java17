package tech.erben.assignments.fulfillment.rest;

public class ApiError {
    private final String message;
    private final String path;
    private final int status;

    public ApiError(String message, String path, int status) {
        this.message = message;
        this.path = path;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }
}
