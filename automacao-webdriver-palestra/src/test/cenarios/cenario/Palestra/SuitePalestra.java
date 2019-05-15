/**
 * 
 */
package cenario.Palestra;

import org.junit.Test;
import org.junit.runner.RunWith;

import apresentacao_palestra.GenericCenario;
import driver.AutomacaoRunner;
import precondicoes.PreCondicao;
import precondicoes.PreCondicoes;

/**
 * Executa todos os casos de teste do RTA.
 * 
 * @since 28/12/2018
 */
@RunWith(AutomacaoRunner.class)
public class SuitePalestra extends GenericCenario{
	
	@Test
	@PreCondicoes(preCondicoes = {
		    @PreCondicao(alvo = Caso_Teste_001.class, casoDeTeste = "casoTeste"),
		    @PreCondicao(alvo = Caso_Teste_002.class, casoDeTeste = "casoTeste"),
			})
	public void suiteTest(){
	}

}
