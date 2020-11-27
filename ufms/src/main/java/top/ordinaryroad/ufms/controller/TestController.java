package top.ordinaryroad.ufms.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 类的说明...
 *
 * @author qq1962247851
 * @date 2020/10/24 21:47
 **/
@RestController
public class TestController {

    @GetMapping(value = "hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject hello() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1L);
        jsonObject.put("name", "张三");
        return jsonObject;
    }

}
