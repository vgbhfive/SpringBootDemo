package cn.vgbhfive.doglicking;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Vgbh
 * @date 2020/4/15 16:04
 */
@Entity
@Cacheable
public class DogLick {

    /**
     * id
     */
    @Id
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
    private Date date;

    /**
     * 时间
     */
    private String time;

    public DogLick() {
    }

    @Id
    public Integer getId() {
        return id;
    }

    public DogLick setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DogLick setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DogLick setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DogLick setContent(String content) {
        this.content = content;
        return this;
    }

    public String getWeath() {
        return weath;
    }

    public DogLick setWeath(String weath) {
        this.weath = weath;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public DogLick setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getTime() {
        return time;
    }

    public DogLick setTime(String time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "DogLick{" +
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
