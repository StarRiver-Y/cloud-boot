package cn.kio.cloud.boot.db.exception;

/**
 * @author kio
 */
public class DataSourceNotFoundException extends RuntimeException {

    public DataSourceNotFoundException(String message) {
        super(message);
    }

    public DataSourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
