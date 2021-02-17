package cn.vgbhfive.doglicking;

/**
 * @author Vgbh
 * @date 2020/11/6 20:55
 */
public class DogLickStr {

    private Integer id;

    /**
     * 昵称
     */
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 天气
     */
    private String weath;

    /**
     * 日期
     */
    private String date;

    /**
     * 时间
     */
    private String time;

    public DogLickStr() {
    }

    public DogLickStr(Integer id, String name, String title, String content, String weath, String date, String time) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.content = content;
        this.weath = weath;
        this.date = date;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public DogLickStr setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DogLickStr setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DogLickStr setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DogLickStr setContent(String content) {
        this.content = content;
        return this;
    }

    public String getWeath() {
        return weath;
    }

    public DogLickStr setWeath(String weath) {
        this.weath = weath;
        return this;
    }

    public String getDate() {
        return date;
    }

    public DogLickStr setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTime() {
        return time;
    }

    public DogLickStr setTime(String time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "DogLickStr{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", weath='" + weath + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
