package top.ordinaryroad.ufms.common.utils;

/**
 * 常量
 *
 * @author qq1962247851
 * @date 2020/4/21 17:55
 */
public class Constant {
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";
    /**
     * 上传文件目录类型
     */
    public static String UPLOAD_FILE_DIRECTORY_TYPE = "product,";

    public static String getPictureBasePath() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return "e:\\桌面\\";
        } else {
            return "/opt/ufms/data/file/picture/";
        }
    }

    public static void main(String[] args) {
        System.out.println(getPictureBasePath());
    }

    public static boolean checkFilePathType(String type) {
        return UPLOAD_FILE_DIRECTORY_TYPE.contains(type);
    }
}
