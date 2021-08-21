package com.algaworks.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rafaell Estevam
 *
 */
public interface FotoStorage {

	
	public String salvarTemporariamente(MultipartFile[] files);
	
	public byte[] carregarFotoTemporaria(String nome);

	public void salvar(String foto);

	public byte[] carregarFoto(String nome);
	
}
