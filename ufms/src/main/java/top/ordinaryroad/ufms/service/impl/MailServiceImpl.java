package top.ordinaryroad.ufms.service.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import top.ordinaryroad.ufms.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * 邮箱服务实现类
 *
 * @author qq1962247851
 * @date 2020/10/29 22:46
 **/
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * 发送Mime类型的邮件
     *
     * @param toEmail  接收者
     * @param subject  主题
     * @param content  内容
     * @param mimeType mimeType
     * @return 是否发送成功
     */
    @Override
    public Boolean sendMimeMessage(@NotNull String toEmail, @NotNull String subject, @NotNull String content, @NotNull String mimeType) {
        try {
            // 1. 创建一封邮件
            MimeMessage message = javaMailSender.createMimeMessage();


            // 设置环境信息
//             Session session = Session.getInstance(props);

            // 根据参数配置，创建会话对象（为了发送邮件准备的）
//            Session session = Session.getDefaultInstance(props);
            // 创建邮件对象
//            MimeMessage message = new MimeMessage(session);
            /*
             * 也可以根据已有的eml邮件文件创建 MimeMessage 对象
             * MimeMessage message = new MimeMessage(session, new FileInputStream("MyEmail.eml"));
             */

            // 2. From: 发件人
            //    其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
            //    真正要发送时, 邮箱必须是真实有效的邮箱。
            message.setFrom(new InternetAddress(fromEmail));

            // 3. To: 收件人
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toEmail));

            // 4. Subject: 邮件主题
            message.setSubject(subject, "UTF-8");

            // 5. Content: 邮件正文（可以使用html标签）
            message.setContent(content, mimeType);

            // 6. 设置显示的发件时间
            message.setSentDate(new Date());

            // 7. 保存前面的设置
            message.saveChanges();

            // 8. 将该邮件保存到本地
            //            OutputStream out = new FileOutputStream("MyEmail.eml");
            //            message.writeTo(out);
            //            out.flush();
            //            out.close();

            // 9. 发送
            // 会先调用saveChanges，第7步可省略
//            Transport.send(message);
            //不会saveChanges
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
