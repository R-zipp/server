package com.mtvs.arzip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate redisTemplate;
    private final UserService userService;

    private final String ePw = createKey();

    public String createKey() {

        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int)(random.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int)(random.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((random.nextInt(10)));
                    break;
            }
        }

        log.info("인증 키 : {}", key);
        log.info("인증 키 객체 주소 : {}", System.identityHashCode(key));

        return key.toString();
    }

    // 회원 가입 인증 메일
    private MimeMessage createMessage(String to) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("[R-집] 본인 인증 메일");

        String msgg = "";

        msgg += "<div style='margin:100px'>";
        msgg += "<h1>이메일 인증번호 안내</h1>";
        msgg += "<br>";
        msgg += "<p>본 메일은 R-집 사이트의 회원가입을 위한 이메일 인증입니다.</p>";
        msgg += "<p>아래의 [이메일 인증번호]를 입력하여 본인확인을 해주시기 바랍니다.</p>";
        msgg += "<br>";
        msgg += "<p>감사합니다.</p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br />";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("crimecapturetv@gmail.com", "R-집"));

        return message;
    }


    // 회원 가입 인증 메시지 발송
    public String sendLoginAuthMessage(String to) throws Exception{
        log.info("email : {} ", to);
        MimeMessage message = createMessage(to);

        try {
            javaMailSender.send(message);
        }catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        setDataExpire(ePw, to, 60 * 5L); // 유효 시간 5분

        return "인증 메일이 발송되었습니다.";
    }


    // Redis
    // 인증 번호 확인
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

}
