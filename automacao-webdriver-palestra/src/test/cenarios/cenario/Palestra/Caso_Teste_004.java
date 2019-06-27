/**
 * 
 */
package cenario.Palestra;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import driver.AbstractCenario;
import driver.AutomacaoRunner;

/**
 * Enviar os dados informando todos os campos.
 * 
 * @since 28/12/2018
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_004 extends AbstractCenario {
	@Test
	public void casoTeste() throws Exception{
		//Informa as notas do campo 1, 2 e 3
		webDriver.findElement(By.id("com.exemplo.calculamediafinal:id/txtNota1")).sendKeys("3");
		webDriver.findElement(By.id("com.exemplo.calculamediafinal:id/txtNota2")).sendKeys("2");
		webDriver.findElement(By.id("com.exemplo.calculamediafinal:id/txtNota3")).sendKeys("4");
		//Clica no botao Calcular
		webDriver.findElement(By.id("com.exemplo.calculamediafinal:id/btnCalcular")).click();
		//Compara o valor gerado no cmapo media final com o valor esperado pelo teste
		Assert.assertEquals("3.0", webDriver.findElement(By.id("com.exemplo.calculamediafinal:id/txtMediaFinal")).getText());
	}
}