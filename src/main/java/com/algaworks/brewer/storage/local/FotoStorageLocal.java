package com.algaworks.brewer.storage.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.algaworks.brewer.storage.FotoStorage;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

/**
 * @author Rafaell Estevam
 *
 */
public class FotoStorageLocal implements FotoStorage {

	private static final Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);

	private Path localDefault;
	private Path localTemporario;

	public FotoStorageLocal() {
		this.localDefault = FileSystems.getDefault().getPath(System.getProperty("user.home"), ".brewerFotos");
		this.localTemporario = FileSystems.getDefault().getPath(this.localDefault.toString(), "temp");
		criarPastas();

	}



	@Override
	public String salvarTemporariamente(MultipartFile[] files) {
		String novoNome = null;
		
		if (files != null && files.length > 0) {
			
			MultipartFile arquivo = files[0];
			novoNome = renomearFoto(arquivo.getOriginalFilename());
			
			try {
				arquivo.transferTo(new File(this.localTemporario.toAbsolutePath().toString()
						+ FileSystems.getDefault().getSeparator() + novoNome)); //4 
			} catch (IOException e) {
				throw new RuntimeException("Erro salvando foto na pasta temporária", e);
			}
		}
		
		return novoNome;
	}
	
	
	@Override
	public byte[] carregarFotoTemporaria(String nome) {
		try {
			return Files.readAllBytes(this.localTemporario.resolve(nome)); //1
		} catch (IOException e) {
			throw new RuntimeException("Erro ao ler foto temporária", e);
		} 
	}
	
	
	@Override
	public void salvar(String foto) {
		try {
			Files.move(this.localTemporario.resolve(foto), this.localDefault.resolve(foto)); //2
		} catch (IOException e) {
			throw new RuntimeException("Erro movendo a foto para o destino final", e);
		}		
		
		try {
			Thumbnails.of(this.localDefault.resolve(foto).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL); //3
		} catch (IOException e) {
			throw new RuntimeException("Erro ao gerar thumbnail", e);
		}
		
		
	}



	@Override
	public byte[] carregarFoto(String nome) {
				
		try {
			return Files.readAllBytes(this.localDefault.resolve(nome));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao carregar thumbnail", e);
		}
		
	}

	
	
	
	private void criarPastas() {

		try {
			Files.createDirectories(this.localDefault);
			Files.createDirectories(this.localTemporario);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Pasta default criada: " + this.localDefault.toAbsolutePath());
				LOGGER.debug("Pasta temporária criada: " + this.localTemporario.toAbsolutePath());
			}

		} catch (IOException e) {
			throw new RuntimeException("Erro ao criar pasta de imagens", e);
		}

	}
	
	
	
	private String renomearFoto(String nomeOriginal) {
		
		return UUID.randomUUID().toString() +"_"+ nomeOriginal;
		
	}



	


	
	
	
	
	
	
	

}



/*
1. (14.8) Files.readAllBytes(Path) é para ler um arquivo (imagem nesse caso). Temos que passar o caminho dessa imagem. 'this.localTemporario' devolve a pasta e resolve(nome)
indica qual o arquivo que queremos dentro dela. Na vdd, resolve(nome) vai concatenar o nome do arquivo com o restante do caminho já indicado por 'this.localTemporario'.

2. Movendo a imagem da pasta temporária para a pasta default.
 
3. Gerando o thumbnails da foto da cerveja. O thumbnails é salvo automaticamente na mesma pasta do arquivo original. 
Dentro do método Thumbnails.of() estamos passando o local da foto que queremos gerar o thumbnail; então chamamos o método size, passando o tamanho que queremos(olhar as
observações abaixo); por fim renomeamos esse thumbnail gerado, colocando o prefixo 'thumbnail.' para diferenciar do arquivo origninal.

 obs: O instrutor chegou nessas dimensões usando um site para descobrir qual a proporção correta de acordo com a largura que queremos. É só procurar por "Proporcional 
 scaler" no google ou acessar o link: https://scriptygoddess.com/resources/proportioncalc.htm (sim, é htm msm).
 
 
4. "this.localTemporario.toAbsolutePath().toString()" e "this.localTemporario.toString()" SÃO A MESMA COISA! 
 	Agora só "this.localTemporario" é diferente dos de cima, pois esse retorna um Path. 
 
 */





