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

/**
 * Classe utilitaria para recuperação das mensagens do sistema.
 * 
 * @since 28/12/2018
 */
public class MensagemUtil implements Serializable {
	private static final long serialVersionUID = -1446392396251361570L;
	private static final String PROPERTY_FILE_NAME = "resource.mensagens";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(MensagemUtil.PROPERTY_FILE_NAME);

	/**
	 * Retorna a mensagem com os paramentros passados.
	 * */
	public static String getMensagem(final EnumMensagens eMsg, final Object... params) {
		try {
			return MessageFormat.format(MensagemUtil.RESOURCE_BUNDLE.getString(eMsg.name()), params);
		} catch (final MissingResourceException e) {
			return "! Mensagem " + eMsg.name() + " não informado no arquivo de properties  !";
		}
	}

	/**
	 * Retorna a mensagem.
	 * */
	public static String getMensagem(final EnumMensagens eMsg) {
		try {
			return MensagemUtil.RESOURCE_BUNDLE.getString(eMsg.name());
		} catch (final MissingResourceException e) {
			return "! Mensagem " + eMsg.name() + " não informado no arquivo de properties  !";
		}
	}
	
	public String obterMsgMapeamentoCheckIncorretoSemParametro(){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_do_Sem_Parametros, "Check");
	}
	
	public String obterMsgMapeamentoCheckIncorretoParametro(String valor){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_Com_Um_Parametro_do, "Check", valor);
	}
	
	public String obterMsgMapeamentoCheckIncorretoComUmParametro(String check, String valor){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_Com_Um_Parametro_do, "Check", check, valor);
	}
	
	public String obterMsgMapeamentoCampoIncorretoSemParametro(){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_do_Sem_Parametros, "Campo");
	}
	
	public String obterMsgMapeamentoCampoIncorreto(String campo, String tela){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_do, "Campo", campo, tela);
	}
	
	public String obterMsgMapeamentoCheckIncorreto(String check, String tela){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_do, "Check", check, tela);
	}
	
	public String obterMsgMapeamentoCampoIncorretoComParametro(String campo, String valor){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_do, "Campo", campo, valor);
	}
	
	public String obterMsgMapeamentoBotaoIncorreto(String botao, String tela){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_do, "Botão", botao, tela);
	}
	
	public String obterMsgMapeamentoBotaoIncorretoComUmParametro(String botao, String valor){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_Com_Um_Parametro_do, "Botão", botao, valor);
	}
	
	public String obterMsgMapeamentoBotaoIncorretoComDoisParametro(String botao, String valor1, String valor2){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_Com_Dois_Parametro_do, "Botão", botao, valor1, valor2);
	}
	
	public String obterMsgBotaoInexistente(String botao){
		return MensagemLog.getMensagem(EnumLog.MSG_DadoInexistente_do, "Botão", botao);
	}
	
	public String obterMsgMapeamentoGridIncorreto(){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_da, "Grid");
	}
	
	public String obterMsgComparacaoCampo(String campo, String tela){
		return MensagemUtil.getMensagem(EnumMensagens.MSG_Campo_Comparacao, campo, tela);
	}
	
	public String obterMsgComparacaoCampoComParametro(String campo, String valor){
		return MensagemUtil.getMensagem(EnumMensagens.MSG_Campo_Comparacao_Com_Parametro, campo, valor);
	}
	
	public String obterMsgComparacaoLabel(String label, String tela){
		return MensagemUtil.getMensagem(EnumMensagens.MSG_Label_Comparacao, label, tela);
	}
	
	public String obterMsgComparacaoCombo(String combo, String tela){
		return MensagemUtil.getMensagem(EnumMensagens.MSG_Combo_Comparacao, combo, tela);
	}
	
	public String obterMsgComparacaoComboComParametro(String combo, String valor){
		return MensagemUtil.getMensagem(EnumMensagens.MSG_Combo_Comparacao_Com_Parametro, combo, valor);
	}
	
	public String obterMsgComparacaoCheck(String check, String casoTeste, String tela){
		return MensagemUtil.getMensagem(EnumMensagens.MSG_Check_Comparacao, check, casoTeste, tela);
	}
	
	public String obterMsgMapeamentoMensagemIncorreto(String valor){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_da, valor);
	}
	
	public String obterMsgMensagemNaoEsperada(String mensagem){
		return MensagemUtil.getMensagem(EnumMensagens.MSG_Mensagem_Nao_Esperada, mensagem);
	}
	
	public String obterMsgMapeamentoLabelIncorreto(String alvo){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_da, "Label", alvo);
	}
	
	public String obterMsgMapeamentoComboIncorreto(String combo, String tela){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_da, "combo", combo, tela);
	}
	
	public String obterMsgMapeamentoComboIncorretoComParametro(String combo, String valor){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_da, "Combo", combo, valor);
	}
	
	public String obterMsgMapeamentoAbaIncorreto(String valor){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_da, "Aba", valor);
	}
	
	public String obterMsgMapeamentoPaginacaoIncorreto(){
		return MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_do_Sem_Parametros, "Componente de Paginação");
	}
	
	public String obterMsgRelatorioNaoApresentado(String valor){
		return MensagemLog.getMensagem(EnumLog.MSG_Janela_Relatorio, valor);
	}
	
}

