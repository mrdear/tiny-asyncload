package cn.mrdear.asyncload.exception;

/**
 * @author Quding Ding
 * @since 2018/5/10
 */
public class AsyncLoadException extends RuntimeException {
  private static final long serialVersionUID = 7477515660069042975L;

  public AsyncLoadException(Throwable cause) {
    super(cause);
  }

  public AsyncLoadException(String message) {
    super(message);
  }
}
