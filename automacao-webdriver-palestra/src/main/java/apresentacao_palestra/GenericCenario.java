/**
 * 
 */
package apresentacao_palestra;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import dominio.EnumLog;
import driver.AbstractCenario;
import parametrizacao.ParametroUtil;
import util.MensagemLog;

/**
 * Classe que herda os metodos de inicialização da automação e passa a URL para o browser.
 * 
 * @since 28/12/2018
 */
public class GenericCenario extends AbstractCenario {

	private static String MAIN_URL = ParametroUtil.getValueAsString("url");

	private static Logger Log = Logger.getLogger("Inicializando");

	@BeforeClass
	public static void inicializarAutomacao() {
		// Fornecer as definições de configuração do Log4j
		DOMConfigurator.configure(GenericCenario.class.getResource("/util/log4j.xml"));
		Log.info(MensagemLog.getMensagem(EnumLog.MSG_Inicializou_Navegador, ParametroUtil.getValueAsString("browser")));
		Log.info(MensagemLog.getMensagem(EnumLog.MSG_Informou_URL) + MAIN_URL);
		webDriver.manage().window().maximize();
		webDriver.navigate().to(MAIN_URL);
	}

	public static String getMAIN_URL() {
		return MAIN_URL;
	}

	public static void setMAIN_URL(String mAIN_URL) {
		MAIN_URL = mAIN_URL;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		JUnitCore jUnitCore = new JUnitCore();
		jUnitCore.addListener(new TextListener(System.out));
		Class<?> classe = null;
		if (args != null && args.length > 1) {
			classe = Class.forName(args[0]);
			setMAIN_URL(args[1]);
		}

		jUnitCore.run(classe);
		System.exit(0);
	}

}
