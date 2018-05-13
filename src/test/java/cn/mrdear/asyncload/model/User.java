package cn.mrdear.asyncload.model;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Quding Ding
 * @since 2018/5/6
 */
@Data
@ToString
public class User {

  private String username;

  private String passwd;

  private String avatar;


  public static User mockUser() {
    User user = new User();
    user.setUsername("张三-" + ThreadLocalRandom.current().nextInt());
    user.setPasswd("123456");
    user.setAvatar("http://quding.png");
    return user;
  }

}
