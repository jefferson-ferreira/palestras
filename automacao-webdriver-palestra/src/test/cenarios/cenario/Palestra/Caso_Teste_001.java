/**
 * 
 */
package cenario.Palestra;


import org.junit.Test;
import org.junit.runner.RunWith;

import apresentacao_palestra.GenericCenario;
import apresentacao_palestra.GenericPageObject;
import driver.AutomacaoRunner;
import massadedados.LerXls;

/**
 * Enviar os dados informando todos os campos.
 * 
 * @since 28/12/2018
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_001 extends GenericCenario {
	LerXls xls = new LerXls();
	@Test
	public void casoTeste() throws Exception{
		GenericPageObject cenario = new GenericPageObject();
		LerXls.setCasoTeste(Caso_Teste_001.class.getSimpleName());
		cenario.pathRTA("envioDados");
		cenario.executar(xls.lerPlanilhaElementos());
	}
}