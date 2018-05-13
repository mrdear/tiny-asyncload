package cn.mrdear.asyncload.proxy;


import cn.mrdear.asyncload.model.AsyncLoadResult;

/**
 * @author Quding Ding
 * @since 2018/5/6
 */
public interface ProxyFactory {

  /**
   * 创建代理
   * @param result 调用结果封装
   * @return 代理对象
   */
  <R> R createProxy(AsyncLoadResult<R> result);

}
