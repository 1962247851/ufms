package top.ordinaryroad.ufms.config.security.service;

import io.netty.util.internal.StringUtil;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.ordinaryroad.ufms.entity.*;
import top.ordinaryroad.ufms.exception.NullUserNameException;
import top.ordinaryroad.ufms.service.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysPermissionService sysPermissionService;
    private final SysUserRoleRelationService sysUserRoleRelationService;
    private final SysRolePermissionRelationService sysRolePermissionRelationService;

    public UserDetailsServiceImpl(SysUserService sysUserService, SysRoleService sysRoleService, SysPermissionService sysPermissionService, SysUserRoleRelationService sysUserRoleRelationService, SysRolePermissionRelationService sysRolePermissionRelationService) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.sysPermissionService = sysPermissionService;
        this.sysUserRoleRelationService = sysUserRoleRelationService;
        this.sysRolePermissionRelationService = sysRolePermissionRelationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtil.isNullOrEmpty(username)) {
            throw new NullUserNameException("用户名不能为空");
        }
        //根据用户名查询用户
        SysUser sysUser = sysUserService.selectByUserName(username);
        if (sysUser == null) {
            throw new InternalAuthenticationServiceException("账号不存在");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<SysUserRoleRelation> sysUserRoleRelations = sysUserRoleRelationService.queryAllByUserId(sysUser.getId());
        for (SysUserRoleRelation sysUserRoleRelation : sysUserRoleRelations) {
            SysRole sysRole = sysRoleService.queryById(sysUserRoleRelation.getRoleId());
            List<SysRolePermissionRelation> sysRolePermissionRelation = sysRolePermissionRelationService.queryAllByRoleId(sysRole.getId());
            for (SysRolePermissionRelation rolePermissionRelation : sysRolePermissionRelation) {
                SysPermission sysPermission = sysPermissionService.queryById(rolePermissionRelation.getPermissionId());
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysPermission.getCode());
                grantedAuthorities.add(grantedAuthority);
            }
        }
        return new MyUserDetails(sysUser, grantedAuthorities);
    }

    public static class MyUserDetails implements UserDetails {
        private Collection<? extends GrantedAuthority> authorities;
        private SysUser sysUser;

        public MyUserDetails(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
            this.sysUser = sysUser;
            this.authorities = authorities;
        }

        public SysUser getSysUser() {
            return sysUser;
        }

        public void setSysUser(SysUser sysUser) {
            this.sysUser = sysUser;
        }

        public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
        }

        /**
         * Returns the authorities granted to the user. Cannot return <code>null</code>.
         *
         * @return the authorities, sorted by natural key (never <code>null</code>)
         */
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        /**
         * Returns the password used to authenticate the user.
         *
         * @return the password
         */
        @Override
        public String getPassword() {
            return sysUser.getPassword();
        }

        /**
         * Returns the username used to authenticate the user. Cannot return <code>null</code>.
         *
         * @return the username (never <code>null</code>)
         */
        @Override
        public String getUsername() {
            return sysUser.getUsername();
        }

        /**
         * Indicates whether the user's account has expired. An expired account cannot be
         * authenticated.
         *
         * @return <code>true</code> if the user's account is valid (ie non-expired),
         * <code>false</code> if no longer valid (ie expired)
         */
        @Override
        public boolean isAccountNonExpired() {
            return sysUser.getNotExpired();
        }

        /**
         * Indicates whether the user is locked or unlocked. A locked user cannot be
         * authenticated.
         *
         * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
         */
        @Override
        public boolean isAccountNonLocked() {
            return sysUser.getNotLocked();
        }

        /**
         * Indicates whether the user's credentials (password) has expired. Expired
         * credentials prevent authentication.
         *
         * @return <code>true</code> if the user's credentials are valid (ie non-expired),
         * <code>false</code> if no longer valid (ie expired)
         */
        @Override
        public boolean isCredentialsNonExpired() {
            return sysUser.getPasswordNotExpired();
        }

        /**
         * Indicates whether the user is enabled or disabled. A disabled user cannot be
         * authenticated.
         *
         * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
         */
        @Override
        public boolean isEnabled() {
            return sysUser.getEnabled();
        }
    }

}
