package cn.vgbhfive.bookdemos.ch2.el;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @time:
 * @author: Vgbh
 */
@Configuration
@ComponentScan()
@PropertySource("classpath:application.properties")
public class ElConfig {

    @Value("I Love You!")
    private String normal;

    @Value("#{ systemProperties ['os.name'] }")
    private String osName;

    @Value("#{ T(java.lang.Math).random() * 100.0 }")
    private double randomNumber;

    @Value("#{ demoService.another}")
    private String fromAnother;

    @Value("classpath:test.txt")
    private Resource testFile;

    @Value("http://www.baidu.com")
    private Resource testUrl;

    @Value("${book.name}")
    private String bookName;

    @Autowired
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigure () {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public void outputSource () {

        System.out.print(normal + "\n");
        System.out.print(osName + "\n");
        System.out.print(randomNumber + "\n");
        System.out.print(fromAnother + "\n");

        try {
            System.out.print(IOUtils.toString((InputStream) testFile, "UTF-8") + "\n");
            System.out.print(IOUtils.toString((InputStream) testUrl, "UTF-8")+ "\n");
            System.out.print(bookName + "\n");
            System.out.print(environment.getProperty("book.author") + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
