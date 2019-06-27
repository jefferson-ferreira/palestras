/**
 * 
 */
package apresentacao_palestra;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import driver.AbstractCenario;
import massadedados.LerXls;
import parametrizacao.MassaDadosUtil;

/**
 * 
 * @since 28/12/2018
 */
public class ExecucaoPassos extends AbstractCenario {

	private LerXls xls = new LerXls();
	private GenericPageObject GenericPageObject = new GenericPageObject();

	public void pathRTA(String rta) throws Exception {
		LerXls.setPath(MassaDadosUtil.getValueAsString(rta));
		String versao = "Indefinida";
		try {
			aguardarElementoEstarPresente(webDriver.findElement(By.xpath("//*[@id='rodape']/span")), 5);
			String rodape = webDriver.findElement(By.xpath("//*[@id='rodape']/span")).getText();
			if(rodape != null && rodape.contains("Versão: ") && rodape.contains(" - Instância")) {
				versao = rodape.split("Versão: ")[1].split(" - Instância")[0];
			}
		}catch (NoSuchElementException e) {
			xls.inserirPlanVersao("Versão: "+versao);
		}

	}

public void executar() throws Exception {
		
		switch (GenericType.getTipoElemento() != null ? GenericType.getTipoElemento() : "") {
		case "frame":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.frame();
			break;
		case "acoes":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.acoes();
			break;
		case "geraTexto":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.geraText();
			break;
		case "geraTexto ckeditor javaScript":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.geraTextoCkeditorJavaScript();
			break;
		case "geraNumero":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.geraNumero();
			break;
		case "text":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.text();
			break;
		case "label":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.label();
			break;
		case "upload":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.upload();
			break;
		case "button":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.button();
			break;
		case "buttonList":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.buttonList();
			break;
		case "data":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.data();
			break;
		case "labelData":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.labelData();
			break;
		case "arrastaSoltaGrid":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.arrastaSoltaGrid();
			break;
		case "click":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.click();
			break;
		case "combo":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.combo();
			break;
		case "check":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.check();
			break;
		case "checkGrid":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.checkGrid();
			break;
		case "checkGridSemPaginacao":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.checkGridSemPaginacao();
			break;
		case "mensagem":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.mensagem();
			break;
		case "popup":
			GenericPageObject.popup();
			break;
		case "inserirPlan":
			GenericPageObject.inserirPlan();
			break;
		case "clicarGrid":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.clicarGrid();
			break;
		case "verificarItemGrid":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.verificarItemGrid();
			break;
		case "clickItemGrid":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.clickItemGrid();
			break;
		case "clickItemGridVinculacoes":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.clickItemGridVinculacoes();
			break;
		case "radio":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.radio();
			break;
		case "radioGrid":
			aguardarElementoEstarPresente(GenericType.getElemento(), 5);
			GenericPageObject.radioGrid();
			break;
		case "path":
			GenericPageObject.path();
			break;
		default:
			break;
		}
	}
}