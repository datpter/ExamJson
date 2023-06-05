package com.class10;

import com.class10.Util.DBUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

public class SampleManagement {
    public void readJSON()throws Exception{
        try {
            //b1 tao doi tuong khi nhan vao 1 chuoi
            // nhan vao 1 chuoi dung jsonparser de bien no thanh doi tuong
            JSONParser jsonParser = new JSONParser();
            //b2 Lien ket file
            Object obj = jsonParser.parse(new FileReader("sample.json"));


            JSONObject jsonObject = (JSONObject) obj;
            Person person = new Person();


            String firstName = jsonObject.get("firstName").toString();
            String lastName = jsonObject.get("lastName").toString();
            int age = Integer.parseInt(jsonObject.get("age").toString());


            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setAge(age);
            System.out.println(person.toString());


            Map mapAddress= (Map) jsonObject.get("address");
            String streetAddress = mapAddress.get("streetAddress").toString();
            String city = mapAddress.get("city").toString();
            String state = mapAddress.get("state").toString();
            String postalCode = mapAddress.get("postalCode").toString();
            System.out.println(city);
            System.out.println(state);
            System.out.println(postalCode);


            JSONArray phoneNumbers = (JSONArray) jsonObject.get("phoneNumbers");
            Iterator phones = phoneNumbers.iterator();
            while(phones.hasNext()) {
                Iterator<Map.Entry> phone
                        = ((Map) phones.next()).entrySet().iterator();

                while(phone.hasNext()) {
                    Map.Entry pair = phone.next();
                    System.out.println(pair.getKey()+":"+pair.getValue());
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());

        }

    }





    public void readJSONAPI()throws Exception{
        try {
            // goi ket noi
            String apiUrl = "https://jsonplaceholder.typicode.com/posts";
            URL url =new URL(apiUrl);
            API api = new API();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // doc du lieu
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line ;
            while ((line= reader.readLine())!= null){
                response.append(line);
            }
            reader.close();
            org.json.JSONArray jsonArray = new org.json.JSONArray(response.toString());
            for (int i = 0 ; i<jsonArray.length();i++){
                org.json.JSONObject product= (org.json.JSONObject) jsonArray.get(i);
                int Id = Integer.parseInt(product.get("id").toString());
                int userId = Integer.parseInt(product.get("userId").toString());
                String title = product.get("title").toString();
                String body = product.get("body").toString();
                api.setId(Id);
                api.setUserId(userId);
                api.setTitle(title);
                api.setBody(body);


            }
            conn.disconnect();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean deleteApi ( int id ) throws Exception{
        try {
            Connection connection = DBUtil.getMySQlConnection();
            CallableStatement callableStatement = connection.prepareCall("{call sp_DeleteById(?)}");
            callableStatement.setInt(1,id);
            return (callableStatement.executeUpdate()>0);


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean addApi (API api) throws Exception{
        try {
            Connection connection = DBUtil.getMySQlConnection();
            CallableStatement callableStatement= connection.prepareCall("{call sp_post(?,?,?,?)}");
            callableStatement.setInt(1, api.getId());
            callableStatement.setInt(2, api.getUserId());
            callableStatement.setString(3, api.getTitle());
            callableStatement.setString(4, api.getBody());
            return (callableStatement.executeUpdate()>0);



        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }
    public void getALL()throws Exception{
        try {
            Connection connection = DBUtil.getMySQlConnection();
            Statement statement= connection.createStatement();
            ResultSet rs = statement.executeQuery("{call sp_GetAll}");
            while (rs.next()){
                System.out.println("API ALL");
                int id = rs.getInt("id");
                System.out.println(id);
                int userID = rs.getInt("userID");
                System.out.println(userID);
                String title = rs.getString("titile");
                System.out.println(title);
                String body = rs.getString("body");
                System.out.println(body);

            }



        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }

}
