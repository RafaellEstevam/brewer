package com.algaworks.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Rafaell Estevam
 *
 */
@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}






/*
 Essa classe é a classe de configuração do Spring Security e começou a ser montada no final de 18.9. Apesar disso, não vamos configurar o Spring Security. Vamos somente 
 colocar um bean para podermos codificar as senhas. A classe retornada por esse bean é o que vamos usar para isso. Lembrando que até agora os passos foram:
 
  1. Colocar a dependência do Spring Security no pom.xml.
  2. Construir essa classe com o bean.
 
 
 
 
 Lembre-se: Essa classe de configuração vai ser lida quando a aplicação subir, os beans serão lidos logo em seguida e as classes retornadas por eles serão mantidas na RAM.
 Dessa forma, nós podemos usá-las em outras classes usando um @Autowired. O Spring eventualmente também as utiliza no background, quando necessário em outras classes.
 */
