package cn.edu.bit.ruixin.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.edu.bit.ruixin.community.domain.Meeting;
import cn.edu.bit.ruixin.community.domain.User;

/** */
public interface MeetingRepository extends JpaRepository<Meeting, Integer>, JpaSpecificationExecutor<Meeting> {
    
    List<Meeting> findMeetingByLauncher(User launcher);
}
