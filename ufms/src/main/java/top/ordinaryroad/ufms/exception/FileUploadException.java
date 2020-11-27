package top.ordinaryroad.ufms.exception;

/**
 * @author 19622
 */
public class FileUploadException extends RuntimeException {

    public FileUploadException() {

    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
