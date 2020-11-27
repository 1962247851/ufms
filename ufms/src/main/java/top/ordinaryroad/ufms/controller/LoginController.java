package top.ordinaryroad.ufms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ordinaryroad.ufms.service.SysMenuService;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("login")
public class LoginController {
    @Resource
    private SysMenuService sysMenuService;

    @GetMapping("/menu")
    public Map<String, Object> menu() {
        return sysMenuService.menu();
    }
}