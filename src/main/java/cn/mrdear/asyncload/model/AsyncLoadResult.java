package cn.mrdear.asyncload.model;


import lombok.Data;

import cn.mrdear.asyncload.proxy.CglibProxyFactory;

import java.util.concurrent.Future;

/**
 * @author Quding Ding
 * @since 2018/5/9
 */
@Data
public class AsyncLoadResult<R> {

  private Class<R> returnClazz;

  private Future<R> future;

  private Long timeout;


  public AsyncLoadResult(Class<R> returnClazz, Future<R> future, Long timeout) {
    this.returnClazz = returnClazz;
    this.future = future;
    this.timeout = timeout;
  }


  public Object getProxy() {
    return CglibProxyFactory.FACTORY.createProxy(this);
  }



}
