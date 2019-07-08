package cn.vgbhfive.redisdemo2.controller;

import cn.vgbhfive.redisdemo2.model.Book;
import cn.vgbhfive.redisdemo2.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @time: 2019/07/08
 * @author: Vgbh
 */
@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/{id}")
    public Book getBook (@PathVariable String id) {
        //TODO
        return null;
    }

    @PostMapping(value = "/add")
    public Book saveBook (@RequestBody Map<String, String> book) {
        //TODO
        return null;
    }

    @PostMapping(value = "/del/{id}")
    public void delBook (@PathVariable String id){
        //TODO
    }

    @PostMapping(value = "/update")
    public Book updateBook (@RequestBody Map<String, String> book) {
        //TODO
        return null;
    }

}
