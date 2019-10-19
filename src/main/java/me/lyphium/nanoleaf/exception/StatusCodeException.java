package me.lyphium.nanoleaf.exception;

public class StatusCodeException extends RuntimeException {

    private static final long serialVersionUID = -581920985293820233L;

    public StatusCodeException() {}

    public StatusCodeException(String message) {
        super(message);
    }


    public static class BadRequestException extends StatusCodeException {

        private static final long serialVersionUID = 9169016367053850691L;

        public BadRequestException() {
            super("400 Bad Request");
        }

    }

    public static class UnauthorizedException extends StatusCodeException {

        private static final long serialVersionUID = -2605892606627648241L;

        public UnauthorizedException() {
            super("401 Unauthorized");
        }
    }

    public static class ForbiddenException extends StatusCodeException {

        private static final long serialVersionUID = -501572260560838561L;

        public ForbiddenException() {
            super("403 Forbidden");
        }
    }

    public static class PageNotFoundException extends StatusCodeException {

        private static final long serialVersionUID = 7737221076899695074L;

        public PageNotFoundException() {
            super("404 Page Not Found");
        }
    }

    public static class UnprocessableEntityException extends StatusCodeException {

        private static final long serialVersionUID = -9105215715995648519L;

        public UnprocessableEntityException() {
            super("422 Unprocessable Entity");
        }
    }

    public static class InternalServerErrorException extends StatusCodeException {
        private static final long serialVersionUID = 6971015997576808654L;

        public InternalServerErrorException() {
            super("500 Internal Server Error");
        }
    }

}