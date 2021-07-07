package tsp.util;

public class CSVFormatException extends Exception {

  private static final long serialVersionUID = 1L;

  public CSVFormatException() {
    super();
  }

  public CSVFormatException(String message) {
    super(message);
  }

  public CSVFormatException(Throwable cause) {
    super(cause);
  }

  public CSVFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public CSVFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
