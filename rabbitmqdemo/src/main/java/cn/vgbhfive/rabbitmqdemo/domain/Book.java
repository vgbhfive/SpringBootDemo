package cn.vgbhfive.rabbitmqdemo.domain;

import java.io.Serializable;

/**
 * @time: 2018/12/07
 * @author: Vgbh
 */
public class Book implements Serializable {

    private Integer id;

    private String name;

    private String author;

    private String context;

    public Book() {
    }

    public Book(Integer id, String name, String author, String context) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
