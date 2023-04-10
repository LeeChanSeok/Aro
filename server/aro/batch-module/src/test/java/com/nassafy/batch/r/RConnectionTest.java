package com.nassafy.batch.r;

import com.nassafy.core.entity.Forecast;
import com.nassafy.core.respository.ForecastRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RConnectionTest {

    @Autowired
    ForecastRepository forecastRepository;


    @Test
    @DisplayName("r connection test")
    public void rConnectionTest ()  {
        RConnection conn = null;
        double[] x;
        try {
            conn = new RConnection("rstudio", 6311);  // 로컬에서는 host를 j8d106.p.ssafy.io로, 코드 올릴 때는 rstudio로 변경하기
            REXP exp = conn.eval("source('/home/rstudio/getAceLocData2.R')");
            RList rList = conn.eval("getAceLocData()").asList();

            x = rList.at("GES_X").asDoubles();

        } catch (RserveException | REXPMismatchException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        Assertions.assertThat(x[0]).isEqualTo(246.3);
    }

    @DisplayName("r 72시간 예측 kp data 삽입 test")
    public void test (){

        RConnection conn = null;
        try {
            conn = new RConnection("rstudio", 6311);  // // 로컬에서는 host를 j8d106.p.ssafy.io로, 코드 올릴 때는 rstudio로 변경하기

            // 현재 시간으로부터 1달 전 날짜 계산
            LocalDateTime now = LocalDateTime.now();
            now = now.minusMonths(1);
            String input_date = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();


            // R script를 실행해서 결과 받아오기
            conn.eval("source('/home/rstudio/threeDaysPredict.R')");
            conn.eval("kp_data <- crawlingKPData('" + input_date +"')");
            RList rList = conn.eval("kp_data").asList();
            double[] predictKp = conn.eval("predictThreeDayKP(kp_data)").asDoubles();


            // 마지막 시간 받아오기
            int[] years = rList.at("YEAR").asIntegers();
            int[] months = rList.at("MONTH").asIntegers();
            int[] days = rList.at("DAY").asIntegers();
            int[] times = rList.at("TIME").asIntegers();

            int year = years[years.length - 1];
            int month = months[months.length - 1];
            int day = days[days.length - 1];
            int time = times[times.length - 1] == 0 ? times[times.length - 1] : times[times.length - 1] / 100;

            LocalDateTime datetime = LocalDateTime.of(year, month, day, time, 0);

            System.out.println(datetime);


            // 데이터 생성
            List<Forecast> forecasts = new ArrayList<>();
            for (int i = 0; i < 72; i++) {
                datetime = datetime.plusHours(1);
                Forecast forecast = Forecast.builder().kp((float) predictKp[i]).dateTime(datetime).build();
                System.out.println(forecast.toString());
                forecasts.add(forecast);
            }

            // 테이블 비우기 및 데이터 삽입
            forecastRepository.deleteAllInBatch();
            forecastRepository.saveAll(forecasts);

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DisplayName("24시간 kp 예측")
    public void test2 (){
        RConnection conn = null;
        try {
            conn = new RConnection("rstudio", 6311);  // // 로컬에서는 host를 j8d106.p.ssafy.io로, 코드 올릴 때는 rstudio로 변경하기

            // input date 계산하기(1달 전)
            LocalDateTime now = LocalDateTime.now();
            now = now.minusMonths(1);
            String input_date = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();

            // R script 실행하기
            conn.eval("source('/home/rstudio/dailyPredict.R')");
            conn.eval("regulate <- crawlingData('" + input_date +"')");
            RList rList = conn.eval("regulate").asList();
            double[] predictKp = conn.eval("predictDailyKP(regulate, 24)").asDoubles();

            // 마지막 시간 받아오기
            String[] years = rList.at("YEAR").asStrings();
            int[] months = rList.at("MONTH").asIntegers();
            int[] days = rList.at("DAY").asIntegers();
            int[] times = rList.at("TIME").asIntegers();

            int year = Integer.parseInt(years[years.length - 1]);
            int month = months[months.length - 1];
            int day = days[days.length - 1];
            int time = times[times.length - 1] == 0 ? times[times.length - 1] : times[times.length - 1] / 100;

            LocalDateTime datetime = LocalDateTime.of(year, month, day, time, 0);  // 마지막 예측 시간

            now = LocalDateTime.now().minusHours(9); // 현재 한국 시간을 utc시간이랑 맞춰주기
            now = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0); // 분 정보는 삭제하기

            // 만약 저장할 데이터가 없다면 작업을 중지합니다.
            if (datetime.plusHours(24).isBefore(now)) {
                System.out.println("update할 data가 없습니다.");
                return;
            }

            // 저장할 데이터가 있는 경우에 저장합니다.
            for (int i = 0; i < 24; i ++) {
                datetime = datetime.plusHours(1);
                if (datetime.isAfter(now)) {
                    try {
                        Forecast forecast = forecastRepository.findByDateTime(datetime)
                                .orElseThrow(IllegalArgumentException::new);
                        forecast.updateKp((float) predictKp[i]);
                        forecastRepository.save(forecast);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
