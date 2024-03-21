package org.util;


import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *   @desc : 读取所有类文件自动生成 readme
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class ReadmeUtil {


    public static String readInfo(String path){

        File clazz = new File(path);
        String info = "unknow";

        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(clazz),"UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                boolean stop = false;
                // 读取 @desc 或者 @Desc
                if(line.contains("@desc")){
                    info = line.substring(line.indexOf("desc")).replace("desc","").replace(":","").replace("：","");
                    stop = true;
                }
                else if(line.contains("@Desc")){
                    info = line.substring(line.indexOf("Desc")).replace("Desc","").replace(":","").replace("：","");
                    stop = true;
                }
                if(stop){
                    break;
                }
            }
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return info;
    }


    // 每4个图片组成一行
    public static List<List<String>> one2foure(List<String> imgs){

        // 每4个元素组成一个子列表
        List<List<String>> result = new ArrayList<>();
        List<String> subList = new ArrayList<>();

        for (String img : imgs) {
            subList.add(img);
            // 当子列表达到4个元素时，添加到结果列表并重置子列表
            if (subList.size() == 4) {
                result.add(new ArrayList<>(subList));
                subList.clear();
            }
        }

        // 如果最后一个子列表不足4个元素，添加到结果列表
        if (!subList.isEmpty()) {
            result.add(new ArrayList<>(subList));
        }

        return result;
    }


    public static void write(LinkedHashMap<String,String> infos,List<String> imgs,String readPath){

        StringBuffer sb = new StringBuffer();
        sb.append("# algorithm").append("\n");
        sb.append("\n\n\n");
        sb.append("Java 常用算法用例。\n" +
                "傅里叶变换、PID、卡尔曼滤波、分类、回归、聚类、频繁挖掘 、推荐、搜索、nlp(分词、提词、相关性)等任务。\n" +
                "以及 apache-math3、Weka、Spark-ML、Mahout、Smile、apache-OpenNlp 等算法工具库的使用。\n");
        sb.append("\n\n");
        List<List<String>> iimge = one2foure(imgs);
        sb.append("<table style=\"border-collapse: collapse; border: none;\">").append("\n");
        iimge.stream().forEach(n->{
            sb.append("<tr>").append("\n");
            n.stream().forEach(m->{
                sb.append(m).append("\n");
            });
            sb.append("</tr>").append("\n");
        });
        sb.append("</table>").append("\n");

        sb.append("\n\n\n");

        sb.append("|序号|测试类|说明|").append("\n");
        sb.append("|:---------|:---------|:------------------------------------------------------|").append("\n");

        AtomicInteger c = new AtomicInteger(1);
        infos.entrySet().forEach(n->{
            sb.append("|").append(c.getAndIncrement()).append("|").append(n.getKey()).append("|").append(n.getValue()).append("|").append("\n");
        });

        // 覆盖方式写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(readPath,false))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception{


        // 当前项目目录
        String root = new File("").getCanonicalPath();

        // 保存所有类的信息
        LinkedHashMap<String,String> infos = new LinkedHashMap<>();

        // 查找对应的图片
        List<String> imgs = new ArrayList<>();
        String img = root + "\\img";
        Arrays.stream(new File(img).listFiles()).sorted((o1, o2) -> {
            // 取文件名称后缀
            o1.getName().substring(0,2);
            o2.getName().substring(0,2);
            return Float.compare(Float.valueOf(o1.getName().substring(0,2)),Float.valueOf(o2.getName().substring(0,2)));
        }).forEach(n->{
            String nn = n.getName().split("\\.")[0];
            String r = "<th><div><p>"+nn.substring(2,nn.length())+"</p><img src=\"img/"+n.getName()+"\" width=\"200\" height=\"150\"></div></th>";
            imgs.add(r);
        });

        // 查找对应的代码
        String clazzs = root + "\\src\\main\\java\\org\\tyf";
        Arrays.stream(new File(clazzs).listFiles()).filter(n->n.getName().contains(".java")).forEach(n->{
            // 读取文本
            String info = readInfo(n.getAbsolutePath());
            infos.put(n.getName().replace(".java",""),info);
        });

        // 写入 readme
        String readPath = root+"\\README.md";;
        write(infos,imgs,readPath);

        System.exit(0);

    }


}
