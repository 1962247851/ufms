package top.ordinaryroad.ufms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.ordinaryroad.ufms.controller.FeedbackController;

@SpringBootTest
class UfmsApplicationTests {

    @Autowired
    FeedbackController feedbackController;

    @Test
    void contextLoads() {
        System.out.println(feedbackController.findAllFeedbacks("newest", 1, 10, 4L));
        System.out.println(feedbackController.findAllFeedbackReplies(1, 10, null, 4L, 1L));
        System.out.println(feedbackController.findAllFeedbackReplies(1, 10, 11, 4L, 1L));
    }

}
