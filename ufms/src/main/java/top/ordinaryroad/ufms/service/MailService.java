package top.ordinaryroad.ufms.service;

import org.jetbrains.annotations.NotNull;

/**
 * 邮箱服务类
 *
 * @author qq1962247851
 * @date 2020/10/29 22:44
 **/
public interface MailService {

    /**
     * 发送Mime类型的邮件
     *
     * @param toEmail  接收者
     * @param subject  主题
     * @param content  内容
     * @param mimeType mimeType
     * @return 是否发送成功
     */
    Boolean sendMimeMessage(@NotNull String toEmail, @NotNull String subject, @NotNull String content, @NotNull String mimeType);

}
