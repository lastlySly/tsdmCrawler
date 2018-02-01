package cn.lastlySly.myutils;

import cn.lastlySly.model.Comic;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
/**
 * @author lastlySly
 * @GitHub https://github.com/lastlySly
 * @create 2018-02-01 13:03
 **/
public class GetComicData {
    public static SimpleDateFormat formatter =  new SimpleDateFormat("HH:mm:ss");
    public static List<Comic> list = new LinkedList<Comic>();
    public static File file = new File("G:\\工作备份\\网络爬虫数据\\天使动漫\\tsdm.json");
    public static void main(String[] args){

        try {
            long startTime = System.currentTimeMillis();
            list = getData();
            long endTime = System.currentTimeMillis();    //获取结束时间
            long time = endTime - startTime;
            String hms = timeFormat(time);
            System.out.println("数据已全部抓取完毕，共历时：" + hms);    //输出程序运行时间

        }catch (Exception e){
            intoFile(list,file);
        }

    }


    public static List<Comic> getData(){

        String rooturl = "http://dm.tsdm.tv";
        int pagenum = 1;
        String initial = "";
        int totalcount = 1;

        //按字母检索，ASCLL码转换
//        A ==> 65  Q ==> 81
        for (int ch = 65;ch <= 90;ch++){
            initial = (char)ch + "";
            System.out.println("按首字母检索，首字母为 " + initial + " 开始检索抓取数据：(每次完成一类首字母的抓取则写入一次文件)");
                //第一次加载获取查询到的总页数
            String homeurl ="http://dm.tsdm.tv/search.asp?searchtype=4&searchword=" + initial;
            Document root = myConnectUtil(homeurl);//连接加载
//            Element homeroot = root.select(".movie-chrList").first();
            Element pagecount = root.select("body > div:nth-child(13) > div.box700.fl > div > div.pages > span > span:nth-child(1)").first();
//            System.out.println(pagecount.text());
            String pagenumstr = pagecount.text().split("/")[1];
            pagenum = Integer.parseInt(pagenumstr.split("页")[0]);
             System.out.println("标题首字母为" +initial+ "共有"+ pagenum + "页数据");
            for (int i = 1; i <= pagenum; i++) {
                String pageurl = "http://dm.tsdm.tv/search.asp?page=" + i + "&searchtype=4&searchword=" + initial;

                Document newroot = myConnectUtil(pageurl);//连接加载
//                System.out.println(root.html());

                Element targetroot = newroot.select(".movie-chrList").first();//获取目标根节点
//                System.out.println(targetroot.html());

                Elements items = targetroot.select("li");//获取目标数据集合
                System.out.println("动漫标题首字母为" +initial+",第" + i + "页（共"+ pagenum +"页）共有" + items.size() + "条数据");//16

                for (int ii = 0; ii < items.size(); ii++) {

                    long start = System.currentTimeMillis();

                    Element item = items.get(ii);//获取每个项的根
                    Comic comic = new Comic();
                    //设置按首字母分类
                    comic.setComic_initial(initial);
//      System.out.println(comic.getComic_initial());

                    //设置ID
                    String uuid = String.valueOf(UUID.randomUUID());
                    comic.setComic_id(uuid);
                    //获取并设置标题图片
                    comic.setComic_titleimgurl(rooturl + item.select("div.cover > a > img").first().attr("src"));
//             System.out.println(comic.getComic_titleimgurl()) ;
                    //获取并设置标题
                    comic.setComic_title(item.select("div.intro > h6 > a").first().text());
//              System.out.println(comic.getComic_title());
                    //获取并设置状态
                    comic.setComic_state((item.select("div.intro > em").get(0)).select("abbr").first().text());
//            System.out.println(comic.getComic_state());
                    //获取并设置类型
                    if (item.select("div.intro > em").get(1).text().split("：").length>1){
                        comic.setComic_type(item.select("div.intro > em").get(1).text().split("：")[1]);
                    }
//            System.out.println(comic.getComic_type());
                    //获取并设置人气
                    int popularity = 0;
                    if(item.select("div.intro > em").get(2).text().split("：").length>1){
                        popularity = Integer.parseInt(item.select("div.intro > em").get(2).text().split("：")[1]);
                    }
//        System.out.println("======" + popularity);
                    comic.setComic_popularity(popularity);
                    //获取并设置更新日期
                    if (item.select("div.intro > em").get(3).text().split("：").length>1){
                        comic.setComic_update(item.select("div.intro > em").get(3).text().split("：")[1]);
//System.out.println(comic.getComic_update());
                    }
                    //获取资源所在路径
                    String itemurl =rooturl + item.select("div.cover > a").first().attr("href");
//                        System.out.println(itemurl);

                    //链接字符串准备
                    String comic_resourceurl = "";

                    //进入路径加载页面
                    Document itemroot = myConnectUtil(itemurl);
//                    System.out.println(itemroot.html());

                    //获取并设置描述介绍
                    comic.setComic_describe(itemroot.select(".ctext").first().html());
//                    System.out.println(itemroot.select(".ctext").first().html());

                    //找到资源下载点
                    Element resourceRoot = null;
                    resourceRoot = itemroot.select("#forum_btn").next().first();
                    if (resourceRoot!=null){
//                        System.out.println(resourceRoot.html());
                        //获取所有下载点
                        Elements points = resourceRoot.select(".playurl .bfdz");
                        System.out.println(totalcount++ +"，ID：" + comic.getComic_id() + "=====" + comic.getComic_title() + "：" + itemurl + "有" + points.size() + "个下载点，数据开始抽取...");

                        //遍历下载点
                        int pointnum = 1;
                        for (Element el : points){
                            //获取当前下载点下所有链接
                            comic_resourceurl += "下载点" +pointnum+ "：";
                            Elements urls = el.select("a");
                            //获取下载链接
                            for (Element u : urls){
                                String uurl =u.text() + "=+=" + u.attr("href");
                                comic_resourceurl += uurl;
                            }
                            pointnum++;
                        }

                        long end = System.currentTimeMillis();
                        long tt = end -start;
                        String hms2 = timeFormat(tt);
                        System.out.println("此项数据抽取完毕，历时" + tt + "毫秒，即" + hms2 );

                    }else{
                        System.out.println("暂无下载链接");
                    }
//                    System.out.println(comic_resourceurl);
                    //设置下载点
                    comic.setComic_resourceurl(comic_resourceurl);

                    //加入集合
                    list.add(comic);
                }

            }
            intoFile(list,file);
        }

        //写入文件
        intoFile(list,file);
        return list;
    }

    //写入文件
    public static void intoFile(List<Comic> list,File file){
        FileWriter out = null;
        try {
            out = new FileWriter(file);
            ObjectMapper om = new ObjectMapper();
            String json = "";

            for (Comic cc : list){
                String jsonstr = om.writeValueAsString(cc);
                json = json + jsonstr + "\r\n";
            }
            //写入文件
            out.write(json);
            System.out.println("写入成功");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Document myConnectUtil(String url){
        Document dom = null;
        try {
            dom = Jsoup.connect(url).get();
            return dom;
        } catch (IOException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            System.out.println(url+"请求超时，正在尝试重新连接...");
            return myConnectUtil(url);
        }
    }

    public static String timeFormat(long time){
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(time);
        String[] hmsArr = hms.split(":");
        return hmsArr[0] + "时" + hmsArr[1] + "分" + hmsArr[2] + "秒";
    }

}
