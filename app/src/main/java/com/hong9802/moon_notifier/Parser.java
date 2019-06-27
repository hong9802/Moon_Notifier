package com.hong9802.moon_notifier;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parser extends AsyncTask<String, Void, String> {
    public String getData(String address)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        StringBuffer buffer = new StringBuffer();
        String key = BuildConfig.OpenAPIKEY;
        String date = df.format(new Date());
        String location = convertURL(address);
        String queryURL = "http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getAreaRiseSetInfo?" +
                "serviceKey=" + key +
                "&locdate=" + date +
                "&location=" + location;
        buffer.append(location + ";");
        try {
            java.net.URL url = new URL(queryURL);
            InputStream stream = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(stream, "UTF-8"));
            String tag;

            xpp.next();
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        if(tag.equals("moonrise"))
                        {
                            buffer.append("월출 ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.insert(8, ":");
                        }
                        else if(tag.equals("moonset"))
                        {
                            buffer.append("월몰 ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.insert(18, ":");
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public String convertURL(String address)
    {
        String[] avLocal;
        if(address.contains("서울"))
            return "서울";
        else if(address.contains("경기"))
        {
            avLocal = new String[]{"고양", "광주", "부천", "시흥", "안산", "안양",
            "양평", "여주", "용인", "파주", "평택", "화성"};
            for(int i = 0; i < avLocal.length; i++)
            {
                if(address.contains(avLocal[i]))
                {
                    if(i == 1)
                        return "광주(경기)";
                    return avLocal[i];
                }
            }
            return "수원";
        }
        else if(address.contains("강원"))
        {
            avLocal = new String[]{"강릉", "고성", "대관령", "동해", "삼척", "양양", "영월",
            "원주", "주문진", "춘천", "태백"};
            for(int i = 0; i < avLocal.length; i++)
            {
                if(address.contains(avLocal[i]))
                {
                    if(i == 1)
                        return "고성(강원)";
                    return avLocal[i];
                }
            }
            return "속초";
        }
        else if(address.contains("충청북도"))
        {
            avLocal = new String[]{"단양", "제천", "청주", "영동"};
            for(int i = 0; i < avLocal.length; i++)
            {
                if(address.contains(avLocal[i]))
                {
                    if(i == 0)
                        return "소백산";
                    else if(i == 3)
                        return  "추풍령";
                    return avLocal[i];
                }
            }
            return "충주";
        }
        else if(address.contains("충청남도"))
        {
            avLocal = new String[]{"보령", "서산", "서천", "아산", "태안"};
            for(int i = 0; i < avLocal.length; i++)
            {
                if(address.contains(avLocal[i]))
                    return avLocal[i];
            }
            return "천안";
        }
        else if(address.contains("전라북도"))
        {
            avLocal = new String[]{"군산", "남원", "변산", "부안", "익산", "임실", "장수", "정읍"};
            for(int i = 0; i < avLocal.length; i++)
            {
                if(address.contains(avLocal[i]))
                    return avLocal[i];
            }
            return "전주";
        }
        else if(address.contains("전라남도"))
        {
            avLocal = new String[]{"신안", "고흥", "광양", "목포", "무안", "보성", "순천", "승주",
            "영광", "완도", "진도", "해남", "장흥"};
            for(int i = 0; i < avLocal.length; i++)
            {
                if(address.contains(avLocal[i]))
                {
                    if(i == 0)
                        return "흑산도";
                    return avLocal[i];
                }
            }
            return "여수";
        }
        else if(address.contains("경상북도"))
        {
            avLocal = new String[]{"영천", "경산", "경주", "구미", "김천", "독도", "상주",
            "영덕", "영주", "영천","울릉도", "울진", "의성", "포항"};
            for(int i = 0; i < avLocal.length; i++)
            {
                if(address.contains(avLocal[i]))
                {
                    if(i == 0)
                        return "보현산";
                    return avLocal[i];
                }
            }
            return "안동";
        }
        else if(address.contains("경상남도"))
        {
            avLocal = new String[]{"거제", "거창", "김해", "남해", "밀양", "사천", "진주",
                    "진해", "창원", "통영"};
            for(int i = 0; i < avLocal.length; i++)
            {
                if(address.contains(avLocal[i]))
                    return avLocal[i];
            }
            return "마산";
        }
        else if(address.contains("인천"))
        {
            if(address.contains("강화"))
                return "강화도";
            return "인천";
        }
        else if(address.contains("대전"))
        {
            if(address.contains("대덕"))
                return "대덕";
            return "대전";
        }
        else if(address.contains("광주"))
            return "광주";
        else if(address.contains("대구"))
            return "대구";
        else  if(address.contains("세종"))
            return "세종";
        else if(address.contains("광주"))
            return "광주";
        else if(address.contains("울산"))
            return "울산";
        else if(address.contains("부산"))
            return "부산";
        else if(address.contains("제주"))
            return "제주";
        else
            return "서울"; //오류나면 기본값으로 서울 return
    }
/*
    @Override
    protected String doInBackground(Void... voids) {
        return getData();
    }
*/
    @Override
    protected String doInBackground(String... strings) {
        return getData(strings[0]);
    }
}
