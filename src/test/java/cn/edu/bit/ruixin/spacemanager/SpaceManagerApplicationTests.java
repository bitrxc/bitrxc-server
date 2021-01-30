package cn.edu.bit.ruixin.spacemanager;

import cn.edu.bit.ruixin.spacemanager.domain.Room;
import cn.edu.bit.ruixin.spacemanager.repository.RoomsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpaceManagerApplicationTests {

    @Autowired
    RoomsRepository roomsRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testSave() {
        Room room = new Room();
        room.setName("活动室215");
        Room save = roomsRepository.save(room);
        System.out.println(save);
    }
}
