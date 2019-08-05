/**
 * 
 */
package cenario.Apresentacao;


import org.junit.Test;
import org.junit.runner.RunWith;

import apresentacao_apresentacao.GenericCenario;
import driver.AutomacaoRunner;

/**
 * Validar a obrigatóriedade do campo "CPF".
 * 
 * @since 25/01/2019
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_005 extends GenericCenario {
	@Test
	public void casoTeste() throws Exception{
		super.casoTeste("envioDados");
	}
}