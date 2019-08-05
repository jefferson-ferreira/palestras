/**
 * 
 */
package cenario.Apresentacao;


import org.junit.Test;
import org.junit.runner.RunWith;

import apresentacao_apresentacao.GenericCenario;
import driver.AutomacaoRunner;

/**
 * Enviar os dados informando todos os campos.
 * 
 * @since 28/12/2018
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_001 extends GenericCenario {
	@Test
	public void casoTeste() throws Exception{
		super.casoTeste("envioDados");
	}
}