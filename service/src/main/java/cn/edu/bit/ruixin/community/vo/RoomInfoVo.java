package cn.edu.bit.ruixin.community.vo;

import cn.edu.bit.ruixin.community.domain.Room;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/1/29
 */
public class RoomInfoVo {

    private Integer id;
    private String name;
    private Integer gallery;
    private String description;

    public static Room convertToPo(RoomInfoVo infoVo) {
        Room room = new Room();
        room.setId(infoVo.getId());
        room.setName(infoVo.getName());
        room.setDescription(infoVo.getDescription());
        room.setGallery(infoVo.getGallery());
        return room;
    }

    public static RoomInfoVo convertToVo(Room room) {
        RoomInfoVo infoVo = new RoomInfoVo();
        infoVo.setId(room.getId());
        infoVo.setName(room.getName());
        infoVo.setDescription(room.getDescription());
        infoVo.setGallery(room.getGallery());
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

    public Integer getGallery() {
        return gallery;
    }

    public void setGallery(Integer gallery) {
        this.gallery = gallery;
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
                ", gallery='" + gallery + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
