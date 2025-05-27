package com.example.taskmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 以下を参照して作成したプログラム(#15まで)
 * https://www.youtube.com/playlist?list=PLTw8Dy-j8WMhSwBeAcbrA3N5TScGqxkw1
 * 
 * MyBatisに関しては、動画ではXMLに定義していたので、MyBatis Dynamic SQLに直して書いている
 *
 */
@SpringBootApplication
public class TaskMateDynamicApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskMateDynamicApplication.class, args);
	}

}
