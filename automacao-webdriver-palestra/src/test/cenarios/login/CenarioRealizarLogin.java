/**
 * 
 */
package login;

import org.junit.Test;
import org.junit.runner.RunWith;

import apresentacao_apresentacao.GenericCenario;
import driver.AutomacaoRunner;

/**
 * Classe que realiza o login.
 * 
 * @since 28/12/2018
 */

@RunWith(AutomacaoRunner.class)
public class CenarioRealizarLogin extends GenericCenario {
	
	@Test
	public void realizarLogin() throws Exception {
		PageLoginLogout login = new PageLoginLogout();
		login.informarUsuario();
		login.informarSenha();
		login.solicitarLogin();
	}

}

