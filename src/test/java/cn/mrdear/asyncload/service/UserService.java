package cn.mrdear.asyncload.service;


import cn.mrdear.asyncload.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 模拟耗时服务
 * @author Quding Ding
 * @since 2018/5/6
 */
public class UserService {


  public User findById(Long id) {
    try {
      // 模拟耗时操作
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return User.mockUser();
  }


  public List<User> queryByIds(Collection<Long> ids) {
    try {
      // 模拟耗时操作
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return Stream.generate(User::mockUser)
        .limit(10)
        .collect(Collectors.toList());
  }

}
