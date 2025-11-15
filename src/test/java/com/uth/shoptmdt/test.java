package com.uth.shoptmdt;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {
    public static void main(String[] args) {
        String raw = "123456";
        // 10 là strength phổ biến; có thể tăng 12/14 nếu muốn chậm hơn & an toàn hơn
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        String hash = encoder.encode(raw);

        System.out.println("RAW   : " + raw);
        System.out.println("HASH  : " + hash);
        System.out.println("MATCH?: " + encoder.matches(raw, hash));
    }
}
