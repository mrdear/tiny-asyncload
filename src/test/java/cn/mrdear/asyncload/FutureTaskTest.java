package cn.mrdear.asyncload;


import org.junit.Assert;
import org.junit.Test;

import cn.mrdear.asyncload.model.User;
import cn.mrdear.asyncload.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Quding Ding
 * @since 2018/5/6
 */
public class FutureTaskTest {

  private ExecutorService executorService = Executors.newCachedThreadPool();

  private UserService userService = new UserService();
// ---------旧方式是显式创建提交并获取结果
  @Test
  public void testOldFindById() throws ExecutionException, InterruptedException {
    long startTime = System.currentTimeMillis();
    // 1显式创建
    FutureTask<User> userTask = new FutureTask<>(() -> userService.findById(1L));
    // 2显式提交
    executorService.submit(userTask);
    // 3显式获取
    final User user = userTask.get();
    System.out.println(user);
    Assert.assertTrue(System.currentTimeMillis() - startTime > 2000L);
    Assert.assertTrue(System.currentTimeMillis() - startTime < 3000L);
  }


  @Test
  public void testOldQueryByIds() throws ExecutionException, InterruptedException {
    long startTime = System.currentTimeMillis();
    FutureTask<List<User>> userTask = new FutureTask<>(() -> userService.queryByIds(Collections.emptyList()));
    executorService.submit(userTask);
    final List<User> users = userTask.get();
    System.out.println(users);
    Assert.assertTrue(System.currentTimeMillis() - startTime > 3000L);
    Assert.assertTrue(System.currentTimeMillis() - startTime < 4000L);
  }

  // ---------新方式是隐式创建,显式提交,隐式获取结果

  @Test
  public void testNewFindById() throws ExecutionException, InterruptedException {
    AsyncLoadConfig config = new AsyncLoadConfig(Executors.newCachedThreadPool(),3000L);
    AsyncLoadTemplate template = new AsyncLoadTemplate(config);
    long startTime = System.currentTimeMillis();
    User user = template.execute(new Callable<User>() {
      @Override
      public User call() throws Exception {
        Thread.sleep(1000);
        return User.mockUser();
      }
    });
    Thread.sleep(1000);
    System.out.println(user);
    Assert.assertTrue(System.currentTimeMillis() - startTime > 1000L);
    Assert.assertTrue(System.currentTimeMillis() - startTime < 1500L);
  }


  @Test
  public void testNewQueryByIds() throws ExecutionException, InterruptedException {
    long startTime = System.currentTimeMillis();
    AsyncLoadConfig config = new AsyncLoadConfig(Executors.newCachedThreadPool(),3000L);
    AsyncLoadTemplate template = new AsyncLoadTemplate(config);
    // 使用模板方式执行异步任务,返回代理类
    List<User> users = template.execute(new Callable<List<User>>() {
      @Override
      public List<User> call() throws Exception {
        Thread.sleep(2000);
        return Collections.singletonList(User.mockUser());
      }
    });
    Thread.sleep(2000);
    Assert.assertTrue(System.currentTimeMillis() - startTime > 2000L);
    Assert.assertTrue(System.currentTimeMillis() - startTime < 2500L);
    // 打印实际上调用 toString() 方法,其会作用到异步任务返回的真实类上
    System.out.println(users);
  }

}
