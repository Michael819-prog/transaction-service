package org.example;

public class ErrorResponse {
    private ErrorDetail error;

    public ErrorResponse(String message, String status) {
        this.error = new ErrorDetail(message, status);
    }

    private static class ErrorDetail {
        private String message;
        private String status;

        public ErrorDetail(String message, String status) {
            this.message = message;
            this.status = status;
        }
    }
}
