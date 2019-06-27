/**
 * 
 */
package cenario.Palestra;


import org.junit.Test;
import org.junit.runner.RunWith;

import apresentacao_palestra.GenericCenario;
import driver.AutomacaoRunner;

/**
 * Validar a obrigatóriedade do campo "Nome".
 * 
 * @since 25/01/2019
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_002 extends GenericCenario {
	@Test
	public void casoTeste() throws Exception{
		super.casoTeste("envioDados");
	}
}