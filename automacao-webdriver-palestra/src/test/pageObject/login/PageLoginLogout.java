/**
 * 
 */
package login;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import apresentacao_apresentacao.GenericPageObject;
import apresentacao_apresentacao.WebDriverSingleton;
import dominio.EnumLog;
import parametrizacao.ParametroUtil;
import util.MensagemLog;

/**
 * Classe que mapea os elementos da tela e os instancia.
 * 
 * @since 28/12/2018
 */
public class PageLoginLogout extends GenericPageObject {

	private static Logger Log = Logger.getLogger("Inicializando");
	private static PageLoginLogout instancia = null;

	public PageLoginLogout() {
		PageFactory.initElements(WebDriverSingleton.getDriver(), this);
	}

	public static PageLoginLogout instancia() {
		synchronized (PageLoginLogout.class) {
			if (instancia == null) {
				instancia = new PageLoginLogout();
			}
		}
		return instancia;
	}

	@FindBy(xpath = "//*[@id='j_username']")
	private WebElement campoMatricula;

	@FindBy(xpath = "//*[@id='j_password']")
	private WebElement campoSenha;

	@FindBy(xpath = "//*[@id='botaoLaranja']")
	private WebElement botaoEntrar;


	public void informarUsuario() throws Exception {
		instancia().campoMatricula.sendKeys(ParametroUtil.getValueAsString("usuarioPadrao"));
		Log.info(MensagemLog.getMensagem(EnumLog.MSG_Informou_Usuario, ParametroUtil.getValueAsString("usuarioPadrao")));
	}

	public void informarSenha() throws Exception {
		delay(200);
		instancia().campoSenha.sendKeys(ParametroUtil.getValueAsString("senhaPadrao"));
		Log.info(MensagemLog.getMensagem(EnumLog.MSG_Informou_Senha, "*******"));	
	}

	public void solicitarLogin() throws Exception {
		coletarEvidencia();
		instancia().botaoEntrar.click();
		delay(1000);
		coletarEvidencia();
		Log.info(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Botao, "Entrar"));
	}
}
