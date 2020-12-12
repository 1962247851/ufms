package top.ordinaryroad.ufms.common.utils;

import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 分词工具类
 *
 * @author qq1962247851
 * @date 2020/12/5 19:35
 **/
public class JcsegUtil {

    public static ISegment segment;

    private static void init() {
        SegmenterConfig segmenterConfig = new SegmenterConfig();
        //设置过滤停止词
        segmenterConfig.setClearStopwords(true);
        //取消中文数字转数字
        segmenterConfig.setCnNumToArabic(false);
        ADictionary dictionary = DictionaryFactory.createSingletonDictionary(segmenterConfig, false);
        //加载自定义词典
        try {
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                dictionary.loadDirectory("/opt/jcseg/lexicon");
            } else {
                dictionary.loadDirectory("F:\\作业\\大三上\\软件工程\\大作业\\ufms\\src\\main\\resources\\lexicon");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //加载默认字典
//            dictionary.loadClassPath();
        //复杂模式
        segment = ISegment.COMPLEX.factory.create(segmenterConfig, dictionary);
    }

    /**
     * 词频向量化
     *
     * @param string 要分词的内容
     * @return 向量化后的Map
     */
    public static Map<String, Integer> segment(String string) {
        if (segment == null) {
            init();
        }
        Map<String, Integer> map = new HashMap<>();
        try {
            segment.reset(new StringReader(string));
            //读取结果
            IWord iWord = segment.next();
            while (iWord != null) {
                String value = iWord.getValue();
                var count = map.get(value);
                if (count == null) {
                    count = 1;
                } else {
                    count++;
                }
                map.put(value, count);
                iWord = segment.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, Integer> segmentString(String string) {
        return segment(string);
    }

}
