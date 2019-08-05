/**
 * 
 */
package apresentacao_apresentacao;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import dominio.EnumLog;
import dominio.EnumLogManualUsuario;
import driver.AbstractCenario;
import log.Log;
import massadedados.LerXls;
import parametrizacao.ParametroUtil;
import util.MensagemLog;
import util.MensagemLogManualUsuario;

/**
 * Classe que herda os metodos de inicialização da automação e passa a URL para o browser.
 * 
 * @since 28/12/2018
 */
public class GenericCenario extends AbstractCenario {

	private static String MAIN_URL = ParametroUtil.getValueAsString("url");
	LerXls xls = new LerXls();

	public void casoTeste(String pathRTA) throws Exception{
		//GenericPageObject genericPageObject = new GenericPageObject();
		GenericType genericType = new GenericType();
		ExecucaoPassos execucao = new ExecucaoPassos();
		LerXls.setCasoTeste(AbstractCenario.getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString());
		execucao.pathRTA(pathRTA);
		//genericPageObject.acessarMenu();
		genericType.valoresRTA(xls.lerPlanilhaElementos());
	}
	
	@BeforeClass
	public static void inicializarAutomacao() {
		// Fornecer as definições de configuração do Log4j
		DOMConfigurator.configure(GenericCenario.class.getResource("/util/log4j.xml"));
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Inicializou_Navegador, ParametroUtil.getValueAsString("browser")));
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Inicializar_Navegador, ParametroUtil.getValueAsString("browser")));
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Informou_URL) + MAIN_URL);
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Informar_URL) + MAIN_URL);
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
