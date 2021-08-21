package com.algaworks.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.helper.cerveja.CervejaRepositoryQueries;

/**
 * @author Rafaell Estevam
 *
 */
@Repository
public interface CervejaRepository extends JpaRepository<Cerveja, Long>, CervejaRepositoryQueries{

	
	
}


/*
 Abaixo a forma de adicionar meus métodos customizados em um repositório:
(15.4) ATENÇÃO! Muito importante essa parte onde extendemos a interface "CervejaRepositoryQueries". Ela é uma interface que nós criamos para levar os métodos de consulta
customizados. A situação é a seguinte: Temos que fazer uma consulta que não dá pra fazer nem com derived Queries nem com JPQL, como criteria, por exemplo. O método que 
usa o criteria precisa de uma implementação, certo? E essa implementação não pode ser feita aqui, correto? Então o que fazemos é o seguinte: Criamos uma interface que vai
levar todos os métodos de consulta customizados da CervejaRepository. Por isso o nome "CervejaRepositoryQueries"(é muito importante que o nome seja o mesmo do repositório).
Porém onde está a implementação dos métodos dessa interface? Elas vão ficar em uma outra classe, chamada "CervejaRepositoryImpl". E aqui uma observação. É EXTREMAMENTE
IMPORTANTE QUE A CLASSE QUE LEVA A IMPLEMENTAÇÃO DOS MÉTODOS CUSTOMIZADOS LEVEM O NOME DO REPOSITORIO + O SUFIXO "Impl". Isso porque o Spring, ao inicializar um reposi-
tório, vai procurar classes de implementação de métodos customizados que esse repositório possa ter, e ele vai procurar exatamente a classe que leve o nome do repositório
+Imp. Então para "CervejaRepository", ele vai procurar por "CervejaRepositoryImpl". É possível trocar o "Impl" por outro sufixo, basta ir em JPAConfig e na anotação
"@EnableJpaRepositories" incluir repositoryImplementationPostfix = "sufixoDesejado". Tudo isso é mostrado na aula, em +-12min.  
*/
