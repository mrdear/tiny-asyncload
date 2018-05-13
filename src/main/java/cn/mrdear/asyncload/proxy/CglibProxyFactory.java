package cn.mrdear.asyncload.proxy;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

import cn.mrdear.asyncload.exception.AsyncLoadException;
import cn.mrdear.asyncload.model.AsyncLoadResult;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Cglib代理工厂
 *
 * @author Quding Ding
 * @since 2018/5/6
 */
public class CglibProxyFactory implements ProxyFactory {

  public static final ProxyFactory FACTORY = new CglibProxyFactory();

  /**
   * 可以考虑加缓存降低动态代理的消耗
   * @param result 调用结果封装
   */
  @SuppressWarnings("unchecked")
  @Override
  public <R> R createProxy(AsyncLoadResult<R> result) {
    Enhancer enhancer = new Enhancer();
    Class<R> returnClazz = result.getReturnClazz();
    if (returnClazz.isInterface()) {
      enhancer.setInterfaces(new Class[]{returnClazz});
    } else {
      enhancer.setSuperclass(returnClazz);
    }
    enhancer.setCallbackType(AsyncLoadResultInterceptor.class);
    enhancer.setCallback(new AsyncLoadResultInterceptor<>(result.getFuture(),result.getTimeout()));
    return (R) enhancer.create();
  }


  class AsyncLoadResultInterceptor<R> implements LazyLoader {

    private Future<R> future;

    private Long timeout;

    AsyncLoadResultInterceptor(Future<R> future, Long timeout) {
      this.future = future;
      this.timeout = timeout;
    }

    @Override
    public Object loadObject() throws Exception {
      return loadFuture();
    }

    private Object loadFuture() throws TimeoutException {
      try {
        // 使用cglib lazyLoader，避免每次调用future
        // <=0处理，不进行超时控制
        if (timeout <= 0) {
          return future.get();
        } else {
          return future.get(timeout, TimeUnit.MILLISECONDS);
        }
      } catch (TimeoutException e) {
        future.cancel(true);
        throw e;
      } catch (Exception e) {
        throw new AsyncLoadException(e);
      }
    }

  }

}
