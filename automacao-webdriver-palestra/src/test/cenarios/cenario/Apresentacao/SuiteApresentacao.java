/**
 * 
 */
package cenario.Apresentacao;

import org.junit.Test;
import org.junit.runner.RunWith;

import apresentacao_apresentacao.GenericCenario;
import driver.AutomacaoRunner;
import precondicoes.PreCondicao;
import precondicoes.PreCondicoes;

/**
 * Executa todos os casos de teste do RTA.
 * 
 * @since 28/12/2018
 */
@RunWith(AutomacaoRunner.class)
public class SuiteApresentacao extends GenericCenario{
	
	@Test
	@PreCondicoes(preCondicoes = {
		    @PreCondicao(alvo = Caso_Teste_001.class, casoDeTeste = "casoTeste"),
		    @PreCondicao(alvo = Caso_Teste_002.class, casoDeTeste = "casoTeste"),
		    @PreCondicao(alvo = Caso_Teste_003.class, casoDeTeste = "casoTeste"),
			})
	public void suiteTest(){
	}

}
