package org.tyf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import smile.sequence.HMM;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;


/**
 *   @desc : Smile HMM 隐马尔可夫模型
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileHMM {

    // 隐马尔可夫模型
    // 这里实现一个命名实体任务

    // 处理命名实体
    public static String[] lstr = new String[]{
            "-", // 没有类别
            "a", // address
            "b", // book
            "c", // company
            "g", // game
            "go",// goverment
            "m", // movie
            "n", // name
            "or", // organization
            "p", // position
            "s" // scene
    };


    // 加载 train.json
    public static List<JSONObject> loadTrainJson(String file) throws Exception{
        // 每一行一个json、text是语料、label下面是实体
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
        String line = null;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        // 所有实体对象
        List<JSONObject> objs = lines.stream().map(n->JSONObject.parseObject(n)).collect(Collectors.toList());
        objs.stream().forEach(obj->{
            // 预料
            String text = obj.getString("text");
            // 实体
            JSONObject label = obj.getJSONObject("label");
            if(label!=null){
                // 10种类型的实体
                JSONObject address = label.getJSONObject("address");// 地址
                JSONObject book = label.getJSONObject("book");// 书名
                JSONObject company = label.getJSONObject("company");// 公司
                JSONObject game = label.getJSONObject("game");// 游戏
                JSONObject goverment = label.getJSONObject("goverment");// 政府
                JSONObject movie = label.getJSONObject("movie");// 电影
                JSONObject name = label.getJSONObject("name");// 姓名
                JSONObject organization = label.getJSONObject("organization");// 组织机构
                JSONObject position = label.getJSONObject("position");// 职位
                JSONObject scene = label.getJSONObject("scene");// 景点
//            System.out.println(text
//                    + "，" + (address==null?"":address)
//                    + "，" + (book==null?"":book)
//                    + "，" + (company==null?"":company)
//                    + "，" + (game==null?"":game)
//                    + "，" + (goverment==null?"":goverment)
//                    + "，" + (movie==null?"":movie)
//                    + "，" + (name==null?"":name)
//                    + "，" + (organization==null?"":organization)
//                    + "，" + (position==null?"":position)
//                    + "，" + (scene==null?"":scene)
//            );
            }
        });
        return objs;
    }

    // 读取每个字符进行保存
    public static Map<String,Integer> loadDict4String(String trainJson,String testJson){
        Map<String, Integer> charMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(trainJson), "UTF-8"))) {
            int charIndex = 0;
            int codePoint;
            while ((codePoint = br.read()) != -1) {
                // 转换为字符串
                String character = new String(Character.toChars(codePoint));
                // 将字符映射到整数
                if(!charMap.containsKey(character)){
                    charMap.put(character, charIndex++);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(testJson), "UTF-8"))) {
            int charIndex = 0;
            int codePoint;
            while ((codePoint = br.read()) != -1) {
                // 转换为字符串
                String character = new String(Character.toChars(codePoint));
                // 将字符映射到整数
                if(!charMap.containsKey(character)){
                    charMap.put(character, charIndex++);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charMap;
    }

    // 读取每个字符进行保存
    public static Map<Integer,String> loadDict4Integer(Map<String,Integer> file){
        Map<Integer,String> rt = new HashMap<>();
        file.entrySet().forEach(n->{
            rt.put(n.getValue(),n.getKey());
        });
        return rt;
    }

    // 样本转为输入
    public static class DataTransfer{
        int[][] sequences;
        int[][] labels;
        public DataTransfer(List<JSONObject> lines,Map<String,Integer> map4String,Map<Integer,String> map4Integer) {
            List<int[]> toples = new ArrayList<>();
            List<int[]> ints = new ArrayList<>();
            // 遍历每一行
            for (JSONObject obj : lines) {
                String text = obj.getString("text");
                // 每个字符
                List<String> chars = Arrays.asList(text.split(""));
                List<Integer> charIndex = chars.stream().map(n->map4String.get(n)).collect(Collectors.toList());
                int[] x = new int[charIndex.size()];
                for (int i = 0; i < charIndex.size(); i++) {
                    x[i] = charIndex.get(i);
                }
                // 添加 x
                toples.add(x);
                JSONObject label = obj.getJSONObject("label");
                if(label==null){
                    continue;
                }
                JSONObject address = label.getJSONObject("address");// 地址
                JSONObject book = label.getJSONObject("book");// 书名
                JSONObject company = label.getJSONObject("company");// 公司
                JSONObject game = label.getJSONObject("game");// 游戏
                JSONObject goverment = label.getJSONObject("goverment");// 政府
                JSONObject movie = label.getJSONObject("movie");// 电影
                JSONObject name = label.getJSONObject("name");// 姓名
                JSONObject organization = label.getJSONObject("organization");// 组织机构
                JSONObject position = label.getJSONObject("position");// 职位
                JSONObject scene = label.getJSONObject("scene");// 景点

                // 先全部设置为 NO
                String[] xx = new String[x.length];
                for (int i = 0; i < x.length; i++) {
                    xx[i] = "-";
                }

                if(address!=null){
                    address.keySet().forEach(key->{
                        JSONArray ja = address.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "a";
                            }
                        }
                    });
                }
                if(book!=null){
                    book.keySet().forEach(key->{
                        JSONArray ja = book.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "b";
                            }
                        }
                    });
                }
                if(company!=null){
                    company.keySet().forEach(key->{
                        JSONArray ja = company.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "c";
                            }
                        }
                    });
                }
                if(game!=null){
                    game.keySet().forEach(key->{
                        JSONArray ja = game.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "g";
                            }
                        }
                    });
                }
                if(goverment!=null){
                    goverment.keySet().forEach(key->{
                        JSONArray ja = goverment.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "go";
                            };
                        }
                    });
                }
                if(movie!=null){
                    movie.keySet().forEach(key->{
                        JSONArray ja = movie.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "m";
                            }
                        }
                    });
                }
                if(name!=null){
                    name.keySet().forEach(key->{
                        JSONArray ja = name.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "n";
                            }
                        }
                    });
                }
                if(organization!=null){
                    organization.keySet().forEach(key->{
                        JSONArray ja = organization.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "or";
                            }
                        }
                    });
                }
                if(position!=null){
                    position.keySet().forEach(key->{
                        JSONArray ja = position.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "p";
                            };
                        }
                    });
                }
                if(scene!=null){
                    scene.keySet().forEach(key->{
                        JSONArray ja = scene.getJSONArray(key);
                        for (int b = 0; b < ja.size(); b++) {
                            JSONArray jaa = ja.getJSONArray(b);
                            // 实体
                            int start = jaa.getIntValue(0);
                            int end = jaa.getIntValue(1);
                            // 替换 xx
                            for (int i = start; i <= end; i++) {
                                xx[i] = "s";
                            }
                        }
                    });
                }
                int[] y = Arrays.stream(xx).mapToInt(n->{
                    int rt = 0;
                    for (int i = 0; i < lstr.length; i++) {
                        if(n.equals(lstr[i])){
                            rt = i;
                            break;
                        }
                    }
                    return rt;
                }).toArray();
                ints.add(y);
            }
            this.sequences = new int[toples.size()][];
            this.labels = new int[ints.size()][];
            for (int i = 0; i < toples.size(); i++) {
                sequences[i] = toples.get(i);
            }
            for (int i = 0; i < ints.size(); i++) {
                labels[i] = ints.get(i);
            }
        }
    }


    public static void main(String[] args) throws Exception{

        // 下面实现一个命名实体识别任务，数据集下载链接
        // https://storage.googleapis.com/cluebenchmark/tasks/cluener_public.zip


        // 训练和测试样本
        String trainJson = "C:\\Users\\tyf\\Downloads\\cluener_public\\train.json";
        String testJson = "C:\\Users\\tyf\\Downloads\\cluener_public\\test.json";

        // 预料样本行
        List<JSONObject> trainLines = loadTrainJson(trainJson);
        List<JSONObject> testLines = loadTrainJson(testJson);

        // 构造中文字典、每个汉字对应一个int
        Map<String,Integer> map4String = loadDict4String(trainJson,testJson);
        Map<Integer,String> map4Integer = loadDict4Integer(map4String);

        // 将样本转为int数组
        DataTransfer trainData = new DataTransfer(trainLines,map4String,map4Integer);
        DataTransfer testData = new DataTransfer(testLines,map4String,map4Integer);

        // 打印一下看是否转换正确
        System.out.println("训练样本：");
        for (int i = 0; i < 3; i++) {
            int[] xx = trainData.sequences[i];
            int[] yy = trainData.labels[i];
            System.out.println("Text："+Arrays.stream(xx).mapToObj(n->map4Integer.get(n)).collect(Collectors.joining()));
            System.out.println("Label（int）："+Arrays.toString(yy));
            System.out.println("Label（str）："+Arrays.toString(Arrays.stream(yy).mapToObj(n-> lstr[n]).toArray()));

        }

        System.out.println("测试样本：");
        for (int i = 0; i < 3; i++) {
            int[] xx = testData.sequences[i];
            System.out.println("Text："+Arrays.stream(xx).mapToObj(n->map4Integer.get(n)).collect(Collectors.joining()));
        }

        // 训练
        HMM hmm = HMM.fit(trainData.sequences,trainData.labels);

        // 使用测试样本进行推理
        for (int i = 0; i < testData.sequences.length; i++) {

            int[] in = testData.sequences[i];
            int[] out = hmm.predict(in);
            String text = Arrays.stream(in).mapToObj(n->map4Integer.get(n)).collect(Collectors.joining());
            StringBuffer textFilter = new StringBuffer();
            for (int j = 0; j < in.length; j++) {
                String charOne = map4Integer.get(in[j]);
                String entity = lstr[out[j]];
                // 如果是实体
                if(entity!="-"){
                    textFilter.append(charOne);
                }else{
                    textFilter.append(" ");
                }
            }
            System.out.println("-------------------------------");
            System.out.println("Text："+text);
            System.out.println("Out："+Arrays.toString(out));
            System.out.println("textFilter："+Arrays.toString(Arrays.stream(textFilter.toString().split(" ")).filter(n->n.trim()!="").toArray()));
        }


    }

}
