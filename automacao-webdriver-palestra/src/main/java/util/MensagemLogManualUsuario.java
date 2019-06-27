/**
 * 
 */
package util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import dominio.EnumLogManualUsuario;
import log.Log;

/**
 * Classe utilitaria para recuperação das mensagens de Log.
 * 
 * @since 06/06/2019
 */
public class MensagemLogManualUsuario implements Serializable {
	private static final long serialVersionUID = -1446392396251361570L;
	private static final String PROPERTY_FILE_NAME = "resource.mensagensLogManualUsuario";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(MensagemLogManualUsuario.PROPERTY_FILE_NAME);

	/**
	 * Retorna a mensagem de Log com os paramentros passados.
	 * */
	public static String getMensagem(final EnumLogManualUsuario eMsg, final Object... params) {
		try {
			return MessageFormat.format(MensagemLogManualUsuario.RESOURCE_BUNDLE.getString(eMsg.name()), params);
		} catch (final MissingResourceException e) {
			return "! Mensagem " + eMsg.name() + " não informado no arquivo de properties  !";
		}
	}

	/**
	 * Retorna a mensagem.
	 * */
	public static String getMensagem(final EnumLogManualUsuario eMsg) {
		try {
			return MensagemLogManualUsuario.RESOURCE_BUNDLE.getString(eMsg.name());
		} catch (final MissingResourceException e) {
			return "! Mensagem " + eMsg.name() + " não informado no arquivo de properties  !";
		}
	}
	
	public void informarCampo(String campo){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Informar_Campo, campo));
	}
	
	public void limparCampo(String campo){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Limpar_Campo, campo));
	}
	
	public void informarCampoComParametro(String campo, String valor){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Informar_Campo_Valor, campo, valor));
	}
	
	public void informarCampoComDoisParametro(String campo, String valor1, String valor2){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Informar_Campo_Dois_Valor, campo, valor1, valor2));
	}
	
	public void clicarBotao(String botao){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_Botao, botao));
	}
	
	/**
	 * Ex: Clicar no botão: "Salvar" da tela "Cadastro".
	 * 
	 */
	public void clicarBotaoComParametro(String botao, String tela){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_Botao_Com_Parametro, botao, tela));
	}
	
	public void clicarBotaoComParametro2(String botao, String valor){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_Botao_Parametro, botao, valor));
	}
	
	public void clicarBotaoComParametroGrid(String botao, String valor){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_Botao_Grid, botao, valor));
	}
	
	public void clicarCampo(String campo){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_Campo, campo));
	}
	
	public void marcarCheck(String check){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Marcar_Check, check));
	}
	
	public void desmarcarCheck(String check){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Desmarcar_Check, check));
	}
	
	public void selecionarRadio(String radio){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Selecionar_Radio, radio));
	}
	
	public void selecionarRegistro(String registro){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Selecionar_Registro, registro));
	}
	
	public void selecionarArquivo(String arquivo){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Selecionar_Arquivo, arquivo));
	}
	
	public void clicarMenu(String menu){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_Menu, menu));
	}
	
	public void clicarSubMenu(String subMenu){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_SubMenu, subMenu));
	}
	
	public void clicarItemSubMenu(String itemSubMenu){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_ItemSubMenu, itemSubMenu));
	}
	
	public void selecionarCombo(String valor, String valor2){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Selecionar_Combo, valor2, valor));
	}
	
	public void selecionarArquivo(){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Selecionar_Arquivo));
	}
	
	public void clicarLink(String link){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_Link, link));
	}
	
	public void clicarAba(String aba){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_Aba, aba));
	}
	
	public void clicarFlag(String flag, String tela){
		Log.infoManualUsuario(MensagemLogManualUsuario.getMensagem(EnumLogManualUsuario.MSG_Clicar_Flag, flag, tela));
	}

}
