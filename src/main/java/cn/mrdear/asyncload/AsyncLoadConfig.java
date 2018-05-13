package cn.mrdear.asyncload;

import lombok.Getter;

import java.util.concurrent.ExecutorService;

/**
 * 针对一个模板的配置类
 * @author Quding Ding
 * @since 2018/5/5
 */
public class AsyncLoadConfig implements Cloneable {
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

  public AsyncLoadConfig(ExecutorService executorService, Long defaultTimeout) {
    this.executorService = executorService;
    this.defaultTimeout = defaultTimeout;
  }

  @Override
  public AsyncLoadConfig clone() {
    return new AsyncLoadConfig(this.executorService,this.defaultTimeout);
  }

  // --尽量保持类的不可变性,因此set方法不对外开放

  void setDefaultTimeout(Long defaultTimeout) {
    this.defaultTimeout = defaultTimeout;
  }

  void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }
}
