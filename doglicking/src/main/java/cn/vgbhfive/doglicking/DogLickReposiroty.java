package cn.vgbhfive.doglicking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vgbh
 * @date 2020/4/15 16:07
 */
@Repository
public interface DogLickReposiroty extends JpaRepository<DogLick, Integer> {

    /**
     * 查询所有信息
     * @return
     */
    @Query(value = "SELECT a from DogLick as a ORDER BY a.date desc ")
    List<DogLick> findAlls();

}
