package com.algaworks.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.FotoDTO;
import com.algaworks.brewer.storage.FotoStorage;
import com.algaworks.brewer.storage.FotoStorageRunnable;

/**
 * @author Rafaell Estevam
 *(módulo 14)
 */
@RestController
@RequestMapping("/fotos")
public class FotoController {
	
	
	
	@Autowired
	private FotoStorage fotoStorage;
	
		
	@PostMapping //6
	public DeferredResult<FotoDTO>upload(@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<FotoDTO>resultado = new DeferredResult<FotoDTO>();
		
		
		Thread thread = new Thread(new FotoStorageRunnable(files, resultado, fotoStorage));
		thread.start();
		
		return resultado;
	}
	
	@GetMapping("/temp/{nome:.*}") //5
	public byte[] carregarFotoTemporaria(@PathVariable String nome) {//4
		return fotoStorage.carregarFotoTemporaria(nome);
	}
	
	@GetMapping("/{nome:.*}")
	public byte[] carregarFoto(@PathVariable String nome){
	
		return fotoStorage.carregarFoto(nome);
	}
	
	
}
























/*
1.É um RestController pois vamos lidar somente com JSON em todos os métodos. Todos os métodos vão receber e enviar no formato JSON. O que a anotação '@RestController'
faz para nós é colocar automaticamente (por debaixo dos panos) as anotações '@RequestBody' e @ResponseBody'. Assim não temos que nos preocupar em fazer essas conversões
de Java para JSON e etc. Caso fosse um @Controller comum, teríamos que usar essas anotações, como usamos em 'EstiloController' para o cadastro rápido de estilo.

2.É o mesmo que @RequestMapping(method = RequestMethod.POST). O colocamos pois o '@RequestMapping("/fotos")' já está definindo o mapeamento do método. Faltava somente
indicar onde estava o post para esse mapeamento.

3.'@RequestParam("files[]")': Estamos dizendo: Pegue os arquivos do atributo 'files[]' que vêm dentro da requisição e joga nessa variável 'files'. Ou seja, os arquivos 
enviados via ajax ficam dentro desse atributo 'files[]', na requisição. Se não colocarmos essa anotação, ele n sabe em que qual atributo da requisição ele tem que pegar 
os arquivos.
'MultipartFile[] files' : Os arquivos recebidos serão convertidos em um MultipartFile(classe que representa um arquivo, seja texto, foto ou vídeo, aparentemente).
Só que temos que configurar esse MultipartFile, dizendo, por exemplo, onde eu quero que o servidor guarde os arquivos recebidos e etc. Por isso temos que fazer uma 
configuração lá no 'AppInitializer', aplicando um método que dá suporte ao servidor para receber um multipart. Ir em 'AppInitializer' e ver o método 
'customizeRegistration(Dynamic registration)'. 

4.'@PathVariable String nome' é para pegar a váriavel que está na URL e jogar na variável nome. Pode-se fazer também assim @PathVariable(value ="x") String nome,
 quando o nome da variável na URL é diferente do nome da variável onde queremos jogá-lo ->  "x" != "nome". Quando os nomes são iguais, não há necessidade. (14.8)

5. Bom, aqui temos um problema: Quando passamos o nome da imagem como variável, ela tem que ser passada com a extensão ".png". Só que o Spring por padrão corta o ".png"
por achar que é algo consumível(?) e a variável acaba indo sem ela. E sem esse ".png" não consegue-se localizar a imagem. Para informar que tem que ser o nome da váriável 
+ .algumacoisa, usamos uma expressão regular. Por isso fica 'nome:.*'. ':' é para indicar que vamos passar uma expressão regular. '.*' indica justamente "ponto alguma cosia". 

6. O MediaType SEMPRE vai depender do content type da requisição que foi feita. Aqui nesse caso o MediaType NÃO É "MediaType.APPLICATION_JSON_VALUE" pois o content type 
dessa requisição é "multipart/form-data" e não "application/json". Então teríamos que colocar ("consumes = MediaType.MULTIPART_FORM_DATA_VALUE"). Por algum motivo, 
não precisamos colocar e deu certo do mesmo jeito.


*/