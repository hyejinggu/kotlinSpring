package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /*
  * -- JPA의 변경 감지(Dirty Checking) --
    1. @Transactional이 적용된 서비스 메서드 실행
    2. repository 메서드로 엔티티를 조회
    3. JPA가 이 엔티티를 영속 상태(Persistence Context 안에 존재)로 관리
    4. 해당 엔티티의 필드 값들을 변경 -> 이 변경 사항을 JPA가 감지
    5. 메서드 실행이 끝나고 트랜잭션이 종료될 때 flush()가 호출됨
    6. 변경된 필드를 UPDATE 쿼리로 DB에 반영
    즉, save()를 호출하지 않아도, JPA가 트랜잭션 종료 시점에 변경된 엔티티를 자동으로 감지하고 반영함.
  * */
  @Transactional
  public void saveUser(UserCreateRequest request) {
    User newUser = new User(request.getName(), request.getAge());
    userRepository.save(newUser);
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getUsers() {
    return userRepository.findAll().stream()
        .map(UserResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateUserName(UserUpdateRequest request) {
    User user = userRepository.findById(request.getId()).orElseThrow(IllegalArgumentException::new);
    user.updateName(request.getName());
  }

  @Transactional
  public void deleteUser(String name) {
    User user = userRepository.findByName(name).orElseThrow(IllegalArgumentException::new);
    userRepository.delete(user);
  }

}
