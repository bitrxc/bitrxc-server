package cn.edu.bit.ruixin.community.myenum;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/15
 */
public enum ImageType {
    JPG("image/jpeg"),
    PNG("image/png");

    private String type;

    ImageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
