package com.example.myapplication66.chat;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.StrictMode;

import com.example.myapplication66.GpsInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class ChatBot {
    static String output;
    static int func = 0;   //0:없음 1:메뉴 2:퀴즈
    static int a = 0;

    static node[] A = new node[20];

    static GpsInfo GPS;// GPSTracker class
    static Geocoder geocoder;
    static String address="";

    //챗봇기능 구현
    static public void Check(String a, Context mContext) {
        ArrayList<String> A = new ArrayList();
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        output = null;

        KomoranResult analyzeResultList = komoran.analyze(a);

        List<Token> tokenList = analyzeResultList.getTokenList();
        for (Token token : tokenList) {
            if (token.getPos().equals("NNG") || token.getPos().equals("NNP") || token.getPos().equals("VV") ||token.getPos().equals("SL")) { // 명사, 동사, 영어
                A.add(token.getMorph());
            }
        }

        if(func == 1){
            Menu(A);
        }else if(func == 2){
            QuizResult(A);
        }else if(func == 3){
            Bus2(mContext,a);
        }else {
            if (A.contains("해안")) {
                A.remove("해안");

                if (A.contains("선택") || A.contains("고르")) {
                    if (A.contains("메뉴") || A.contains("추천") || A.contains("아침") || A.contains("점심") || A.contains("저녁") || A.contains("밥") || A.contains("아침밥") || A.contains("점심밥") || A.contains("저녁밥")) {
                        //메뉴추천
                        Menu(A);
                    }
                    else {
                        choice(A);
                    }

                } else if (A.contains("메뉴") || A.contains("추천") || A.contains("아침") || A.contains("점심") || A.contains("저녁") || A.contains("밥") || A.contains("아침밥") || A.contains("점심밥") || A.contains("저녁밥")) {
                    //메뉴추천
                    Menu(A);
                } else if (A.contains("날씨")) {
                    //날씨
                } else if (A.contains("위치")) {
                    //위치
                    gps(A,mContext);
                } else if (A.contains("문제") || A.contains("퀴즈")) {
                    //퀴즈
                    Quiz(A);
                } else if (A.contains("버스")) {
                    //교통정보
                    Bus(mContext);
                } else if (A.contains("운세")) {
                    //운세
                    lucky(A);
                } else {
                    //사용법,메뉴얼 보여주기
                    menual();
                }
            }
        }

    }
    //a
    static void choice(ArrayList A){
        A.remove("고르");
        A.remove("선택");
        Random rand = new Random();
        int a = 0;
        a = A.size();
        a = rand.nextInt(a);
        output = (String) A.get(a);
    }

    //퀴즈 기능
    static void Quiz(ArrayList A) {
        ArrayList<String> Q1 = new ArrayList<String>(Arrays.asList("대한민국의 수도는 서울이다.", "미국의 수도는 LA다.","지섭이는 바보다.","수민이는 바보다.")); // O X O X
        Random rand = new Random();
        func = 2;
        a = Q1.size();
        a = rand.nextInt(a);
        output = "문제: "+Q1.get(a);
    }
    static void QuizResult(ArrayList A){
        if (A.contains("O")||A.contains("o")){

            if(a%2==0){
                output = "정답입니다!";
            }else{
                output = "틀렸습니다!";
            }
        }else{
            if(a%2==0){
                output = "틀렸습니다!";
            }else{
                output = "정답입니다!";
            }
        }
        func = 0;
    }

    //운세 기능
    static void lucky(ArrayList A) {
        ArrayList<String> luck = new ArrayList<String>(Arrays.asList("운세 좋음", "운세 그냥 그럼","운세 나쁨", "이지섭 조심", "최수민 조심","최유철 조심","임도연 조심"));
        Random rand = new Random();
        int a = 0;
        func = 0;
        a = luck.size();
        a = rand.nextInt(a);
        output = "오늘 운세는 " + luck.get(a);

    }
    //메뉴추천 기능
    static void Menu(ArrayList A) {
        ArrayList<String> Han = new ArrayList<String>(Arrays.asList("김치찌개", "부대찌개", "된장찌개", "비빔밥", "제육덮밥", "뼈해장국", "수육국밥", "백반", "불고기", "설렁탕"));
        ArrayList<String> Bun = new ArrayList<String>(Arrays.asList("떡볶이", "라볶이", "라면", "만두", "김밥", "순대", "튀김", "우동", "돈까스", "쫄면", "어묵"));
        ArrayList<String> Jung = new ArrayList<String>(Arrays.asList("짜장면", "짬뽕", "간짜장", "우동", "울면", "탕수육", "볶음밥", "잡채밥"));
        ArrayList<String> Il = new ArrayList<String>(Arrays.asList("우동", "초밥", "돈까스", "덮밥", "회", "라멘", "소바", "오니기리"));
        ArrayList<String> Asia = new ArrayList<String>(Arrays.asList("훠궈", "쌀국수", "마라탕", "팟타이", "분짜", "나시고렝"));
        ArrayList<String> Fast = new ArrayList<String>(Arrays.asList("피자", "치킨", "햄버거"));
        ArrayList<String> Ki = new ArrayList<String>(Arrays.asList("냉면", "고기", "곱창", "닭발", "족발", "보쌈", "전"));

        Random rand = new Random();
        int a = 0;
        func = 0;
        if (A.contains("한식")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Han.get(a) + "은 어떠신가요?";
        } else if (A.contains("분식")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Bun.get(a) + "은 어떠신가요?";
        } else if (A.contains("중식")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Jung.get(a) + "은 어떠신가요?";
        } else if (A.contains("일식")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Il.get(a) + "은 어떠신가요?";
        } else if (A.contains("아시아")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Asia.get(a) + "은 어떠신가요?";
        } else if (A.contains("패스트푸드")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Fast.get(a) + "은 어떠신가요?";
        } else if (A.contains("기타")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Ki.get(a) + "은 어떠신가요?";
        } else {
            output = "한식/분식/중식/일식/아시아/패스트푸드/기타 중 어떤 음식을 먹고 싶으신가요?";
            func=1;
        }

        A.clear();
    }

    //위치 기능
    static void gps(ArrayList A,Context mContext) {
        //Log.d("ChatBot", "gps++++++++++++++++++++++++++++++++++");

        GPS = new GpsInfo(mContext);
        geocoder = new Geocoder(mContext);

        if (GPS.isGetLocation()) {
            double latitude = GPS.getLatitude(); //GPS를 통해 위도 가져옴
            double longitude = GPS.getLongitude(); //경도

            List<Address> list = null; //주소로 변환하기 위한 리스트

            try {
                list = geocoder.getFromLocation(latitude, longitude, 10);


            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] splitStr = list.get(0).toString().split(","); //콤마를 기준으로 Split
            address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2);//주소

            //Log.d("Google_Maps 위도" + latitude, "Google_Maps 위도");
            //Log.d("Google_Maps 경도" + longitude, "Google_Maps 경도");


            output="현재위치는  "+address+" 입니다.";

        }else{
            // GPS 를 사용할수 없으므로
            GPS.showSettingsAlert();
        }
        //Log.d("ChatBot", "gps-------------------------------");
    }
    //버스 기능
    static void Bus(Context mContext){
        GPS = new GpsInfo(mContext);
        double latitude = GPS.getLatitude();
        double longitude = GPS.getLongitude();

        System.out.print(latitude +"----" + longitude);

        StrictMode.enableDefaults();
        int a = 0;

        boolean initem = false, inCity = false, inNodeid = false, inNodenm = false;
        String city = null, nodeid = null, nodenm = null;

        boolean inArrstation = false, inArrtime = false, inRouteno = false;
        String arrstation = null, arrtime = null, routeno = null;

        try{
            URL url = new URL("http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getCrdntPrxmtSttnList?"
                    +"serviceKey=h%2Fw4fonXjmMQ0JL6Bn7hhjzK7a8y%2BKLrPco06tq1UfO41%2F7UjcycRKFLWNzkCKpRrv4lN8Xe7oIF3nw5Fpn49A%3D%3D"
                    +"&gpsLati="+latitude+"&gpsLong="+longitude);

            System.out.println(url);
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(),null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("citycode")){ //citycode 만나면 내용을 받을수 있게 하자
                            inCity = true;
                        }
                        if(parser.getName().equals("nodeid")){ //nodeid 만나면 내용을 받을수 있게 하자
                            inNodeid = true;
                        }
                        if(parser.getName().equals("nodenm")){ //nodenm 만나면 내용을 받을수 있게 하자
                            inNodenm = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때

                        if(inCity){ //isTitle이 true일 때 태그의 내용을 저장.
                            city = parser.getText();
                            inCity = false;
                        }
                        if(inNodeid){ //isAddress이 true일 때 태그의 내용을 저장.
                            nodeid = parser.getText();
                            inNodeid = false;
                        }
                        if(inNodenm){ //isMapx이 true일 때 태그의 내용을 저장.
                            nodenm = parser.getText();
                            inNodenm = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            if(output == null) {
                                output = "\n\n 도시코드 : " + city + "\n 정류소ID : " + nodeid + "\n 정류소명 : " + nodenm;
                            }else{
                                output = output + "\n\n 도시코드 : " + city + "\n 정류소ID : " + nodeid + "\n 정류소명 : " + nodenm;
                            }
                            initem = false;
                            A[a] = new node(city,nodeid);
                            a += 1;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            System.out.println("에러남");
        }
        func = 3;
    }

    static void Bus2(Context mContext,String a){

        StrictMode.enableDefaults();

        int b = Integer.parseInt(a);

        String cityCode = A[b-1].CC;
        String nodeId = A[b-1].NI;


        boolean initem = false, inArrstation = false, inArrtime = false, inRouteno = false;
        String arrstation = null, arrtime = null, routeno = null;

        try{
            URL url = new URL("http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList?serviceKey=h%2Fw4fonXjmMQ0JL6Bn7hhjzK7a8y%2BKLrPco06tq1UfO41%2F7UjcycRKFLWNzkCKpRrv4lN8Xe7oIF3nw5Fpn49A%3D%3D&"+
                    "cityCode="+cityCode+"&nodeId="+nodeId);

            System.out.println(url);
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("두번째 파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("arrprevstationcnt")){ //title 만나면 내용을 받을수 있게 하자
                            inArrstation = true;
                        }
                        if(parser.getName().equals("arrtime")){ //address 만나면 내용을 받을수 있게 하자
                            inArrtime = true;
                        }
                        if(parser.getName().equals("routeno")){ //mapx 만나면 내용을 받을수 있게 하자
                            inRouteno = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            System.out.println("에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inArrstation){ //isTitle이 true일 때 태그의 내용을 저장.
                            arrstation = parser.getText();
                            inArrstation = false;
                        }
                        if(inArrtime){ //isAddress이 true일 때 태그의 내용을 저장.
                            arrtime = parser.getText();
                            inArrtime = false;
                        }
                        if(inRouteno){ //isMapx이 true일 때 태그의 내용을 저장.
                            routeno = parser.getText();
                            inRouteno = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            if(output == null) {
                                output = "\n\n 노선 번호 : " + routeno + "\n 남은 정류장 : "+ arrstation+"("+ Integer.parseInt(arrtime)/60+"분 후)";
                            }else{
                                output = output + "\n\n 노선 번호 : " + routeno + "\n 남은 정류장 : "+ arrstation+"("+ Integer.parseInt(arrtime)/60+"분 후)";
                            }
                            initem = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            System.out.println("에러가..났습니다...");
        }

        func = 0;

    }
    //사용법
    static void menual(){
        output = "'해안아' 키워드를 입력하신 후 원하시는 서비스를 입력해주세요. \n"+ "메뉴추천 \n"+"날씨 \n"+"위치 \n"
                +"퀴즈 \n"+"교통정보 \n"+"운세 \n"+"예시)해안 메뉴추천 \n";
    }

    static class node{
        String CC;
        String NI;
        public node(String a, String b){
            CC = a;
            NI = b;
        }
    }
}
