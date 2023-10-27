package com.xrj.test;

import com.xrj.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author XRJ
 * @version 1.0
 * @Data 2023/10/25
 */
@SpringBootTest
public class sensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitive() {
        String text = "这里可以★赌★★博★，可以★嫖★★娼★、可以★开★★票★";
        System.out.println(sensitiveFilter.filter(text));
    }
}
