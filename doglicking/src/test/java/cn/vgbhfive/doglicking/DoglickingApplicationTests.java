package cn.vgbhfive.doglicking;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class DoglickingApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(DoglickingApplicationTests.class);

    @Autowired
    private DogLickReposiroty dogLickReposiroty;

    @Test
    void contextLoads() {
        List<DogLick> dogs = dogLickReposiroty.findAll();
        log.info(dogs.toString());

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");

        for (DogLick dogLick : dogs) {
            DogLickStr dogLickStr = new DogLickStr();
            dogLickStr.setId(dogLick.getId());
            dogLickStr.setContent(dogLick.getContent());
            dogLickStr.setName(dogLick.getName());
            dogLickStr.setTitle(dogLick.getTitle());
            dogLickStr.setWeath(dogLick.getWeath());

            Date date = dogLick.getDate();
            String d = simpleDateFormat1.format(date);
            String t = simpleDateFormat2.format(date);
            log.info("d : " + d + "    t : " + t);
            dogLickStr.setDate(d);
            dogLickStr.setTime(t);
        }

    }

    @Test
    void save() {
        String x = "2020-10-10" + " 12:30:10";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(x);
            log.info("Date : " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DogLick dog = new DogLick();
        dog.setId(UUID.randomUUID().hashCode());
        dog.setName("name1");
        dog.setTitle("title1");
        dog.setContent("content1");
        dog.setWeath("晴");
        dog.setDate(date);
        dog.setTime("");

        log.info("id : " + dogLickReposiroty.save(dog));
    }

}
