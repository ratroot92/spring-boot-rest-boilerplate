package com.evergreen.evergreenmedic.repositories;

import com.evergreen.evergreenmedic.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface UserRepository extends JpaRepository<UserEntity, Short> {
    UserEntity findByEmail(String email);

    //    @Lock(LockModeType.PESSIMISTIC_READ)
    UserEntity save(UserEntity userEntity);


}
