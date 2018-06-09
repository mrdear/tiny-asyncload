package cn.mrdear.asyncload;


import cn.mrdear.asyncload.model.AsyncLoadResult;
import cn.mrdear.asyncload.util.Assert;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 具体提供模板式调用的类
 * @author Quding Ding
 * @since 2018/5/5
 */
public class AsyncLoadTemplate {

  /**
   * 该模板对应的配置类,一旦初始化不允许更改
   */
  private final AsyncLoadConfig config;

  public AsyncLoadTemplate(AsyncLoadConfig config) {
    Assert.notNull(config, "AsyncLoadConfig can'ot be null !");
    this.config = config;
  }

  /**
   * 执行一个任务
   * @param callable 带有返回值的任务
   * @param <R> 真实返回值类型
   * @return 返回值对应的代理类
   */
  public <R> R execute(Callable<R> callable) {
    final AsyncLoadConfig config = this.config;
    // 获取返回类型,为了动态代理
    Type type = callable.getClass().getGenericInterfaces()[0];
    Assert.state(type instanceof ParameterizedType, "return class does not exist");

    // 获取到返回值类型
    @SuppressWarnings("unchecked") Class<R> returnClassType = (Class<R>) getGenericClass((ParameterizedType) type);

    // 执行任务并返回动态代理类
    return execute(config, returnClassType, callable);
  }

  /**
   * 针对lambda类是无法获取到对应的返回值信息
   * @param returnClass 返回值类型
   * @return 调用返回值
   */
  public <R> R execute(Callable<R> callable,Class<R> returnClass) {
    final AsyncLoadConfig config = this.config;
    // 执行任务并返回动态代理类
    return execute(config, returnClass, callable);
  }

  @SuppressWarnings("unchecked")
  private <R> R execute(AsyncLoadConfig config, Class<R> returnClassType, Callable<R> callable) {
    ExecutorService executorService = config.getExecutorService();
    Future<R> future = executorService.submit(callable);
    // 封装结果
    AsyncLoadResult<R> result = new AsyncLoadResult<>(returnClassType, future, config.getDefaultTimeout());
    // 创建代理
    return (R) result.getProxy();
  }

  /**
   * 针对类可以拿到对应的泛型信息,对 lambda 产生的内部类则失效.
   */
  private Class<?> getGenericClass(ParameterizedType parameterizedType) {
    Object genericClass = parameterizedType.getActualTypeArguments()[0];
    if (genericClass instanceof ParameterizedType) { // 处理多级泛型
      return (Class<?>) ((ParameterizedType) genericClass).getRawType();
    } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
      return (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType();
    } else {
      return (Class<?>) genericClass;
    }
  }


}
