package com.class10;

public class MainClass {
    public static void main(String[] args) {
        try {
            SampleManagement sampleManagement = new SampleManagement();
            //sampleManagement.readJSON();
            sampleManagement.readJSONAPI();
        }catch (Exception e){
            System.out.println(e.getMessage());

        }


    }
}
