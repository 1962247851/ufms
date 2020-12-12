package top.ordinaryroad.ufms.config.security.handler;

import top.ordinaryroad.ufms.entity.SysPermission;
import top.ordinaryroad.ufms.entity.SysRequestPath;
import top.ordinaryroad.ufms.entity.SysRequestPathPermissionRelation;
import top.ordinaryroad.ufms.service.SysPermissionService;
import top.ordinaryroad.ufms.service.SysRequestPathPermissionRelationService;
import top.ordinaryroad.ufms.service.SysRequestPathService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class CustomizeFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    //    AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final SysPermissionService sysPermissionService;
    private final SysRequestPathService sysRequestPathService;
    private final SysRequestPathPermissionRelationService sysRequestPathPermissionRelationService;

    public CustomizeFilterInvocationSecurityMetadataSource(SysPermissionService sysPermissionService, SysRequestPathService sysRequestPathService, SysRequestPathPermissionRelationService sysRequestPathPermissionRelationService) {
        this.sysPermissionService = sysPermissionService;
        this.sysRequestPathService = sysRequestPathService;
        this.sysRequestPathPermissionRelationService = sysRequestPathPermissionRelationService;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        if (requestUrl.contains("?")) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }
        /*
        查询具体某个接口的权限
        /sport/query
        */
        SysRequestPath sysRequestPath = sysRequestPathService.queryByUrl(requestUrl);
        if (sysRequestPath == null) {
            return null;
        }
        List<SysRequestPathPermissionRelation> sysRequestPathPermissionRelations = sysRequestPathPermissionRelationService.queryListByUrlId(sysRequestPath.getId());
        if (sysRequestPathPermissionRelations.isEmpty()) {
            return null;
        }
        String[] attributes = new String[sysRequestPathPermissionRelations.size()];
        for (int i = 0; i < sysRequestPathPermissionRelations.size(); i++) {
            SysRequestPathPermissionRelation sysRequestPathPermissionRelation = sysRequestPathPermissionRelations.get(i);
            SysPermission sysPermission = sysPermissionService.queryById(sysRequestPathPermissionRelation.getPermissionId());
            attributes[i] = sysPermission.getCode();
        }
        return SecurityConfig.createList(attributes);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
