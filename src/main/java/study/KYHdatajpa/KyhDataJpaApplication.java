package study.KYHdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

//@EnableJpaAuditing(modifyOnCreate = false)  // create에만 값이 들어가고 update에는 null컬럼 들어감
@EnableJpaAuditing
@SpringBootApplication
public class KyhDataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KyhDataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
//		return () -> Optional.of(UUID.randomUUID().toString()); 
		// return () -> Optional.of(UUID.randomUUID().toString()); 를 `Replace lambda with anonymous class`로 변환
		// 실서비스에서는 세션이나 Security등 에서 값을 꺼내서 활용하면 됨.
		return new AuditorAware<String>() {
			@Override
			public Optional<String> getCurrentAuditor() {
				return Optional.of(UUID.randomUUID().toString());
			}
		};
	}

}