package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findUserByUsername(String username);
    User findUserBySchoolId(String schoolId);
}
