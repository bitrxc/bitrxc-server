package cn.edu.bit.ruixin.spacemanager.vo;

import cn.edu.bit.ruixin.spacemanager.domain.Room;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/1/29
 */
public class RoomInfoVo {

    private Integer id;
    private String name;
    private String image;
    private String description;

    public static Room convertToPo(RoomInfoVo infoVo) {
        Room room = new Room();
        room.setId(infoVo.getId());
        room.setName(infoVo.getName());
        room.setDescription(infoVo.getDescription());
        room.setImage(infoVo.getImage());
        return room;
    }

    public static RoomInfoVo convertToVo(Room room) {
        RoomInfoVo infoVo = new RoomInfoVo();
        infoVo.setId(room.getId());
        infoVo.setName(room.getName());
        infoVo.setDescription(room.getDescription());
        infoVo.setImage(room.getImage());
        return infoVo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RoomInfoVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
