package ksmart.mybatis.encryption;

import static org.assertj.core.api.Assertions.assertThat;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EncruptionConfigTest {

	@Test
	@DisplayName("암호화 설정 확인")
	void test() {
		//given
		String url = "jdbc:log4jdbc:mysql://146.56.38.95:3306/ks46team00?serverTimezone=UTC&characterEncoding=UTF8";
		String userName = "ksmart46";
		String passWord = "ksmart46pw";
		
		StringEncryptor encryptor = stringEncryptor();
		String enUrl = encryptor.encrypt(url);
		String enUserName = encryptor.encrypt(userName);
		String enPassWord = encryptor.encrypt(passWord);
		
		assertThat(url).isEqualTo(encryptor.decrypt(enUrl));
		assertThat(userName).isEqualTo(encryptor.decrypt(enUserName));
		assertThat(passWord).isEqualTo(encryptor.decrypt(enPassWord));
		
		System.out.println(enUrl);
		System.out.println(enUserName);
		System.out.println(enPassWord);
	}
	
	public static StringEncryptor stringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("ksmart46");
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations(1000);
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor;
	}

}
