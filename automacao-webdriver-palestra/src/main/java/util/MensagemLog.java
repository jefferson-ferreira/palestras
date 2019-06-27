/**
 * 
 */
package util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import dominio.EnumLog;
import dominio.EnumMensagens;
import log.Log;

/**
 * Classe utilitaria para recuperação das mensagens de Log.
 * 
 * @since 28/12/2018
 */
public class MensagemLog implements Serializable {
	private static final long serialVersionUID = -1446392396251361570L;
	private static final String PROPERTY_FILE_NAME = "resource.mensagensLog";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(MensagemLog.PROPERTY_FILE_NAME);

	/**
	 * Retorna a mensagem de Log com os paramentros passados.
	 * */
	public static String getMensagem(final EnumLog eMsg, final Object... params) {
		try {
			return MessageFormat.format(MensagemLog.RESOURCE_BUNDLE.getString(eMsg.name()), params);
		} catch (final MissingResourceException e) {
			return "! Mensagem " + eMsg.name() + " não informado no arquivo de properties  !";
		}
	}

	/**
	 * Retorna a mensagem.
	 * */
	public static String getMensagem(final EnumLog eMsg) {
		try {
			return MensagemLog.RESOURCE_BUNDLE.getString(eMsg.name());
		} catch (final MissingResourceException e) {
			return "! Mensagem " + eMsg.name() + " não informado no arquivo de properties  !";
		}
	}
	
	/**
	 * Ex: Comparou os dados do campo: "Nome" na tela de "Produto" com a massa de dados.
	 * 
	 */
	public void comparouCampo(String campo, String tela){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Comparacao_Campo, campo, tela));
	}
	
	public void comparouFlag(String flag, String tela){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Comparacao_Flag, flag, tela));
	}
	
	public void comparouCombo(String combo, String tela){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Comparacao_Combo, combo, tela));
	}
	
	public void comparouComboComParametro(String combo, String valor){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Comparacao_Combo_Com_Parametro, combo, valor));
	}
	
	public void comparouCheck(String check, String tela){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Comparacao_Check, check, tela));
	}
	
	public void informouCampo(String campo){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Informou_Campo, campo));
	}
	
	public void limparCampo(String campo){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Limpou_Campo, campo));
	}
	
	public void informouCampoComParametro(String campo, String valor){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Informou_Campo_Valor, campo, valor));
	}
	
	public void informouCampoComDoisParametro(String campo, String valor1, String valor2){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Informou_Campo_Dois_Valor, campo, valor1, valor2));
	}
	
	public void clicouBotao(String botao){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Botao, botao));
	}
	
	/**
	 * Ex: Clicou no botão: "Salvar" da tela "Cadastro".
	 * 
	 */
	public void clicouBotaoComParametro(String botao, String tela){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Botao_Com_Parametro, botao, tela));
	}
	
	public void clicouBotaoComParametro2(String botao, String valor){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Botao_Parametro, botao, valor));
	}
	
	public void clicouBotaoComParametroGrid(String botao, String valor){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Botao_Grid, botao, valor));
	}
	
	public void clicouCampo(String campo){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Campo, campo));
	}
	
	public void marcouCheck(String check){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Marcou_Check, check));
	}
	
	public void desmarcouCheck(String check){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Desmarcou_Check, check));
	}
	
	public void selecionouRadio(String radio){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Selecionou_Radio, radio));
	}
	
	public void selecionouRegistro(String registro){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Selecionou_Registro, registro));
	}
	
	public void selecionouArquivo(String arquivo){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Selecionou_Arquivo, arquivo));
	}
	
	public void verificouMensagemEsperada(String mensagem){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Mensagem_Validacao, mensagem));
	}
	
	public void verificouMensagemEsperadaTempoAtendimento(String mensagem){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Mensagem_Validacao_Tempo_Atendimento, mensagem));
	}
	
	public void verificouMensagemNaoEsperada(String mensagem){
		Log.infoLog(MensagemUtil.getMensagem(EnumMensagens.MSG_Mensagem_Nao_Esperada, mensagem));
	}
	
	public void verificouItemLista(String valor){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Verifica_Existencia_Item_Lista, valor));
	}
	
	public void clicouMenu(String menu){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Menu, menu));
	}
	
	public void clicouSubMenu(String subMenu){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_SubMenu, subMenu));
	}
	
	public void clicouItemSubMenu(String itemSubMenu){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_ItemSubMenu, itemSubMenu));
	}
	
	public void selecionouCombo(String valor, String valor2){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Selecionar_Combo, valor2, valor));
	}
	
	public void selecionouArquivo(){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Selecionar_Arquivo));
	}
	
	public void clicouLink(String link){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Link, link));
	}
	
	public void clicouAba(String aba){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Aba, aba));
	}
	
	public void clicouFlag(String flag, String tela){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Flag, flag, tela));
	}
	
	public void clicouPaginacao(String paginacao, String tela){
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Paginacao, paginacao, tela));
	}

}
