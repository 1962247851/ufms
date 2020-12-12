package top.ordinaryroad.ufms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.ordinaryroad.ufms.common.utils.JcsegUtil;
import top.ordinaryroad.ufms.dao.UfmsFeedbackDao;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class UfmsApplicationTests {

    @Autowired
    UfmsFeedbackDao feedbackDao;

    @Test
    void generatePwd(){
        System.out.println(new BCryptPasswordEncoder().encode("admin").length());
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }

//    @Test
    void contextLoads() {
        Map<CharSequence, Integer> map = new HashMap<>();
        feedbackDao.findAll().forEach(feedback -> {
            System.out.println(feedback.getContent());
            Map<String, Integer> charSequenceIntegerMap = JcsegUtil.segmentString(feedback.getContent());
            charSequenceIntegerMap.forEach((charSequence, integer) -> {
                if (map.containsKey(charSequence)) {
                    map.put(charSequence, map.get(charSequence) + integer);
                } else {
                    map.put(charSequence, integer);
                }
            });
            System.out.println(charSequenceIntegerMap);
            System.out.println();
        });
        System.out.println(map);
    }

}
