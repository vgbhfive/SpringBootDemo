package cn.vgbhfive.redisdemo2.service;

import cn.vgbhfive.redisdemo2.model.Book;
import cn.vgbhfive.redisdemo2.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @time: 2019/07/08
 * @author: Vgbh
 */
@Service
@CacheConfig(cacheNames = "c1")
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Cacheable(key = "#id")
    public Book get (String id) {
        //TODO
        return null;
    }

    @CachePut(key = "#book.id")
    public Book update (Book book) {
        //TODO
        return null;
    }

    @CacheEvict
    public void delete (String id) {
        //TODO
    }

    public Book save (Book book) {
        //TODO
        return null;
    }

    /**
     * @CacheConfig：这个注解在类上使用，用于描述该类中所有方法使用的缓存名称。当然也可以不配置，直接在具体的缓存注解上配置名称。
     * @Cacheable：这个注解一般加在查询方法上，表示将一个方法的返回值缓存起来。key 时方法的参数，value 就是返回值。
     * @CachePut：这个注解一般加在更新方法上，当数据库中的数据更新后，缓存中的数据也会更新。
     * @CacheEvict：这个注解加在删除方法上，当数据库中的数据删除后，相关的缓存数据也会自动清除。
     *
     * 最后，这里使用的时Spring Cache统一提供的接口。
     * 也可以自定使用RedisTemplate来实现。
     */

}
