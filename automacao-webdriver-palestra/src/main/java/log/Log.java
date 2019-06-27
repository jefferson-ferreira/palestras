/**
 * 
 */
package log;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.xml.DOMConfigurator;

import dominio.EnumLog;
import util.MensagemLog;

/**
 * Classe que herda os metodos que inicializa e confugura o Log.
 * 
 * @since 28/12/2018
 */
public class Log extends GenericLog{
	
	public void logInicial(String url){
		setCaminhoConfiguracoesIniciaisDeLog();
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Inicializou_Navegador));
		Log.infoManualUsuario(MensagemLog.getMensagem(EnumLog.MSG_Inicializou_Navegador));
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Informou_URL) + url);
		Log.infoManualUsuario(MensagemLog.getMensagem(EnumLog.MSG_Informou_URL) + url);
		
	}

	private void setCaminhoConfiguracoesIniciaisDeLog()
			throws FactoryConfigurationError {
		//Fornecer as definições de configuração do Log4j
		DOMConfigurator.configure("./src/main/java/util/log4j.xml");
	}
}
