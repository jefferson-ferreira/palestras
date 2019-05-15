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
 * Validar a obrigatóriedade do campo "Nome".
 * 
 * @since 25/01/2019
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_002 extends GenericCenario {
	LerXls xls = new LerXls();
	@Test
	public void casoTeste() throws Exception{
		GenericPageObject cenario = new GenericPageObject();
		LerXls.setCasoTeste(Caso_Teste_002.class.getSimpleName());
		cenario.pathRTA("envioDados");
		cenario.executar(xls.lerPlanilhaElementos());
	}
}