package cn.vgbhfive.doglicking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Vgbh
 * @date 2020/4/15 16:03
 */
@RestController
@RequestMapping("/dog")
public class DogController {

    private static final Logger log = LoggerFactory.getLogger(DogController.class);

    @Autowired
    private DogLickReposiroty dogLickReposiroty;

    @PostMapping("/all")
    public ResponseEntity<?> findAll() {
        List<DogLick> dogs = dogLickReposiroty.findAll();

        dogs = dogLickReposiroty.findAlls();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");

        List<DogLickStr> list = new ArrayList<>();
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

            list.add(dogLickStr);
        }

        log.info("Size : " + dogs.size());

        return ResponseEntity.ok(list);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Map<String, Object> map) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(map.get("date").toString() + " " + map.get("time").toString());
            log.info("Date : " + date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DogLick dog = new DogLick();
        dog.setId(UUID.randomUUID().hashCode());
        dog.setName(map.get("name").toString());
        dog.setTitle(map.get("title").toString());
        dog.setContent(map.get("content").toString());
        dog.setWeath(map.get("weath").toString());
        dog.setDate(date);
        dog.setTime("");

        DogLick dogLick = dogLickReposiroty.save(dog);

        log.info("DogLick Insert: " + dogLick.toString());

        return ResponseEntity.ok(dogLick.getId());
    }

    @GetMapping("/test")
    public String test() {
        return "Hello World!";
    }

}
