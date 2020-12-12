package top.ordinaryroad.ufms;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.StringUtils;
import top.ordinaryroad.ufms.dao.*;
import top.ordinaryroad.ufms.entity.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * @author 19622
 */
@EnableJpaAuditing
@SpringBootApplication
public class UfmsApplication {

    @Resource
    private SysUserDao userDao;
    @Resource
    private SysRoleDao roleDao;
    @Resource
    private SysUserRoleRelationDao userRoleRelationDao;
    @Resource
    private SysPermissionDao permissionDao;
    @Resource
    private SysRequestPathDao requestPathDao;
    @Resource
    private SysRolePermissionRelationDao rolePermissionRelationDao;
    @Resource
    private SysRequestPathPermissionRelationDao requestPathPermissionRelationDao;

    public static void main(String[] args) {
        SpringApplication.run(UfmsApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            //初始化数据库，dev环境下每次启动都会清空数据库
            if (userDao.count() == 0) {
                System.out.println("正在初始化数据库");

                //开发者
                userDao.saveAndFlush(
                        new SysUser(
                                1L, "1", "admin", true, null, "1962247851@qq.com",
                                null, "$2a$10$4tlsyA.OVQk3zIEv/yHxw.VDeUVmKTQdRXexOHZV7KTgNZ2YehSOO",
                                null, null, true, true, true,
                                true, new Date(), null, true)
                );
                //角色
                roleDao.saveAndFlush(new SysRole("DEVELOPER", "开发者", "可以创建产品等"));
                roleDao.saveAndFlush(new SysRole("USER", "用户", "开发者下不同产品对应的用户"));
                //用户角色关系
                userRoleRelationDao.saveAndFlush(new SysUserRoleRelation(1L, 1L));

                //权限
                permissionDao.saveAndFlush(new SysPermission("PRODUCT_CUD", "增改删产品"));
                permissionDao.saveAndFlush(new SysPermission("FEEDBACK_CUD", "改删反馈"));
                permissionDao.saveAndFlush(new SysPermission("STATISTIC", "产品数据统计"));
                permissionDao.saveAndFlush(new SysPermission("USER_U", "改用户"));
                permissionDao.saveAndFlush(new SysPermission("USER_D", "删用户"));

                //角色权限关系
                rolePermissionRelationDao.saveAndFlush(new SysRolePermissionRelation(1L, 1L));
                rolePermissionRelationDao.saveAndFlush(new SysRolePermissionRelation(1L, 2L));
                rolePermissionRelationDao.saveAndFlush(new SysRolePermissionRelation(1L, 3L));
                rolePermissionRelationDao.saveAndFlush(new SysRolePermissionRelation(1L, 4L));

                //请求路径
                requestPathDao.saveAndFlush(new SysRequestPath(1L, "/api/product/insert", "添加产品"));
                requestPathDao.saveAndFlush(new SysRequestPath(2L, "/api/product/delete", "删除产品"));
                requestPathDao.saveAndFlush(new SysRequestPath(3L, "/api/product/update", "修改产品"));
                requestPathDao.saveAndFlush(new SysRequestPath(4L, "/api/product/find", "根据ID查询产品"));
                requestPathDao.saveAndFlush(new SysRequestPath(5L, "/api/product/findAll", "分页查找所有产品"));

                requestPathDao.saveAndFlush(new SysRequestPath(6L, "/api/feedback/insert", "添加反馈"));
                requestPathDao.saveAndFlush(new SysRequestPath(7L, "/api/feedback/delete", "删除反馈"));
                requestPathDao.saveAndFlush(new SysRequestPath(8L, "/api/feedback/update", "更新反馈"));
                requestPathDao.saveAndFlush(new SysRequestPath(9L, "/api/feedback/find", "根据ID查询反馈"));
                requestPathDao.saveAndFlush(new SysRequestPath(10L, "/api/feedback/findAllByForeignKey", "根据产品ID分页查询所有反馈"));
                requestPathDao.saveAndFlush(new SysRequestPath(11L, "/api/feedback/findAllFeedbacks", "分页查找产品的所有反馈"));
                requestPathDao.saveAndFlush(new SysRequestPath(12L, "/api/feedback/findAllFeedbackReplies", "分页查找反馈的所有回复"));
                requestPathDao.saveAndFlush(new SysRequestPath(13L, "/api/feedback/findAllUserFeedbackAndReply", "分页查找用户的所有反馈和回复"));
                requestPathDao.saveAndFlush(new SysRequestPath(14L, "/api/feedback/like", "点赞反馈"));
                requestPathDao.saveAndFlush(new SysRequestPath(15L, "/api/feedback/toggleIsProperty", "切换反馈的置顶、锁帖等状态"));

                requestPathDao.saveAndFlush(new SysRequestPath(16L, "/api/statistic/queryFeedbackCount", "查询新的反馈个数"));
                requestPathDao.saveAndFlush(new SysRequestPath(17L, "/api/statistic/queryFeedbackReplyCount", "查询回复个数"));
                requestPathDao.saveAndFlush(new SysRequestPath(18L, "/api/statistic/queryRecommendFeedbackCount", "查询好问题个数"));
                requestPathDao.saveAndFlush(new SysRequestPath(19L, "/api/statistic/queryAdminFeedbackReplyCount", "查询官方回复个数"));
                requestPathDao.saveAndFlush(new SysRequestPath(20L, "/api/statistic/querySevenDaysFeedbackAndAdminReply", "查询七天发帖回帖趋势"));
                requestPathDao.saveAndFlush(new SysRequestPath(21L, "/api/statistic/queryFeedbackWordFrequency", "查询发帖内容词频"));

                requestPathDao.saveAndFlush(new SysRequestPath(22L, "/api/captcha/generate", "生成验证码"));
                requestPathDao.saveAndFlush(new SysRequestPath(23L, "/api/captcha/generateEmailCode", "生成邮箱验证码"));

                requestPathDao.saveAndFlush(new SysRequestPath(24L, "/api/file/download", "文件下载"));
                requestPathDao.saveAndFlush(new SysRequestPath(25L, "/api/file/upload", "文件上传"));

                requestPathDao.saveAndFlush(new SysRequestPath(26L, "/api/user/login", "登录"));
                requestPathDao.saveAndFlush(new SysRequestPath(27L, "/api/user/logout", "注销"));
                requestPathDao.saveAndFlush(new SysRequestPath(28L, "/api/user/register", "注册"));
                requestPathDao.saveAndFlush(new SysRequestPath(29L, "/api/user/checkUsername", "检查用户名"));
                requestPathDao.saveAndFlush(new SysRequestPath(30L, "/api/user/resetPassword", "重置密码"));
                requestPathDao.saveAndFlush(new SysRequestPath(31L, "/api/user/update", "更新用户"));
                requestPathDao.saveAndFlush(new SysRequestPath(32L, "/api/user/generateUser", "生成随机用户"));

                //请求路径权限关系
                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(1L, 1L));
                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(2L, 1L));
                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(3L, 1L));

                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(7L, 2L));
                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(8L, 2L));

                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(15L, 2L));

                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(16L, 3L));
                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(17L, 3L));
                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(18L, 3L));
                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(19L, 3L));
                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(20L, 3L));
                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(21L, 3L));

                requestPathPermissionRelationDao.saveAndFlush(new SysRequestPathPermissionRelation(31L, 4L));
                System.out.println("数据库初始化完成");
            } else {
                System.out.println("数据库无需初始化");
            }
        };
    }

    @Bean
    public Connector connector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(80);
        connector.setSecure(false);
        connector.setRedirectPort(1116);
        return connector;
    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StdDeserializer<>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String result = StringDeserializer.instance.deserialize(p, ctxt);
                if (StringUtils.isEmpty(result)) {
                    return null;
                }
                return result;
            }
        });
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

}
