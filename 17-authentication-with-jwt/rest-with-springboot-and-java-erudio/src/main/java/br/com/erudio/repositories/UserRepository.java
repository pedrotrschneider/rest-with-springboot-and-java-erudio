package br.com.erudio.repositories;

import br.com.erudio.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query("SELECT u FROM User WHERE u.userName =: userName")
    UserModel findByUsername(@Param("userName") String userName);
}
