package cn.edu.bit.secondclass.repository;

import cn.edu.bit.secondclass.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    @Query(nativeQuery = true, value = "SELECT `point` FROM `user` WHERE `id` = :id")
    Integer findPointById(@Param("id") int id);

    User findUserById(int id);

    User findUserBySchoolId(String schoolId);
}
