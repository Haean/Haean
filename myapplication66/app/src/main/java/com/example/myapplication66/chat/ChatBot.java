package com.example.myapplication66.chat;

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
    static int func = 0;   //0:없음 1:메뉴 2:선택

    //챗봇기능 구현
    static public void Check(String a) {
        ArrayList<String> A = new ArrayList();
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        output = null;

        KomoranResult analyzeResultList = komoran.analyze(a);

        List<Token> tokenList = analyzeResultList.getTokenList();
        for (Token token : tokenList) {
            if (token.getPos().equals("NNG") || token.getPos().equals("NNP") || token.getPos().equals("VV")) {
                A.add(token.getMorph());
            }
        }

        if(func == 1){
            Menu(A);
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
                } else if (A.contains("문제") || A.contains("퀴즈")) {
                    //퀴즈
                } else if (A.contains("가위바위보")) {
                    //가위바위보
                } else if (A.contains("교통")) {
                    //교통정보
                } else if (A.contains("운세")) {
                    //운세
                } else {
                    //사용법,메뉴얼 보여주기
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


}
