package cn.mrdear.asyncload;

import lombok.Getter;

import cn.mrdear.asyncload.util.Assert;

import java.util.concurrent.ExecutorService;

/**
 * 针对一个模板的配置类
 *
 * @author Quding Ding
 * @since 2018/5/5
 */
public final class AsyncLoadConfig {
  /**
   * 模板执行任务所需要的线程池
   */
  @Getter
  private ExecutorService executorService;
  /**
   * 单个方法默认超时时间
   */
  @Getter
  private Long defaultTimeout;

  private AsyncLoadConfig(ExecutorService executorService, Long defaultTimeout) {
    this.executorService = executorService;
    this.defaultTimeout = defaultTimeout;
  }

  /**
   * 相关配置的建造器
   */
  public static AsyncLoadConfigBuilder builder() {
    return new AsyncLoadConfigBuilder();
  }

  public static class AsyncLoadConfigBuilder {

    private Long defaultTimeout;

    private ExecutorService executorService;

    public AsyncLoadConfigBuilder defaultTimeout(Long defaultTimeout) {
      this.defaultTimeout = defaultTimeout;
      return this;
    }

    public AsyncLoadConfigBuilder executorService(ExecutorService executorService) {
      this.executorService = executorService;
      return this;
    }

    public AsyncLoadConfig build() {
      Assert.notNull(executorService, "executorService can't be null");
      Assert.notNull(defaultTimeout, "defaultTimeout can't be null");
      return new AsyncLoadConfig(executorService, defaultTimeout);
    }
  }
}
